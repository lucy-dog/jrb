package com.jsw.jrb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsw.common.exception.Assert;
import com.jsw.common.result.ResponseEnum;
import com.jsw.jrb.core.enums.BorrowInfoStatusEnum;
import com.jsw.jrb.core.enums.BorrowerStatusEnum;
import com.jsw.jrb.core.enums.UserBindEnum;
import com.jsw.jrb.core.mapper.BorrowerMapper;
import com.jsw.jrb.core.mapper.IntegralGradeMapper;
import com.jsw.jrb.core.mapper.UserInfoMapper;
import com.jsw.jrb.core.pojo.entity.BorrowInfo;
import com.jsw.jrb.core.mapper.BorrowInfoMapper;
import com.jsw.jrb.core.pojo.entity.Borrower;
import com.jsw.jrb.core.pojo.entity.IntegralGrade;
import com.jsw.jrb.core.pojo.entity.UserInfo;
import com.jsw.jrb.core.pojo.vo.BorrowInfoApprovalVO;
import com.jsw.jrb.core.pojo.vo.BorrowerDetailVO;
import com.jsw.jrb.core.service.BorrowInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsw.jrb.core.service.BorrowerService;
import com.jsw.jrb.core.service.DictService;
import com.jsw.jrb.core.service.LendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    /**
     * 借款信息
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getBorrowAmount(Long userId) {

        //获取用户积分
        UserInfo userInfo = userInfoMapper.selectById(userId);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();

        //根据积分查询借款额度
        QueryWrapper<IntegralGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("integral_start", integral);
        queryWrapper.ge("integral_end", integral);
        IntegralGrade integralGradeConfig = integralGradeMapper.selectOne(queryWrapper);
        if(integralGradeConfig == null){
            return new BigDecimal("0");
        }
        return integralGradeConfig.getBorrowAmount();
    }

    /**
     * 提交借款申请
     * @param borrowInfo
     * @param userId
     */
    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {

        //获取userInfo的用户数据
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //判断用户绑定状态
        Assert.isTrue(
                userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_BIND_ERROR);

        //判断用户信息是否审批通过
        Assert.isTrue(
                userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_AMOUNT_ERROR);

        //判断借款额度是否足够
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR);

        //存储数据
        borrowInfo.setUserId(userId);
        //百分比转成小数
        borrowInfo.setBorrowYearRate( borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    /**
     * 获取借款申请审批状态
     * @param userId
     * @return
     */
    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> borrowInfoQueryWrapper = new QueryWrapper<>();
        borrowInfoQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowInfoQueryWrapper);

        if (objects.size() == 0){
            //借款人尚未提交信息
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }

        return (Integer)objects.get(0);
    }

    @Resource
    private DictService dictService;

    @Override
    public List<BorrowInfo> selectList() {
        List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoList();
        borrowInfoList.forEach(borrowInfo -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
            String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
            borrowInfo.getParam().put("returnMethod", returnMethod);
            borrowInfo.getParam().put("moneyUse", moneyUse);
            borrowInfo.getParam().put("status", status);
        });

        return borrowInfoList;
    }

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    /**
     * 获取借款信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {

        //查询借款对象
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        //组装数据
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
        borrowInfo.getParam().put("returnMethod", returnMethod);
        borrowInfo.getParam().put("moneyUse", moneyUse);
        borrowInfo.getParam().put("status", status);

        //根据user_id获取借款人对象
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<Borrower>();
        borrowerQueryWrapper.eq("user_id", borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        //组装借款人对象
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

        //组装数据
        Map<String, Object> result = new HashMap<>();
        result.put("borrowInfo", borrowInfo);
        result.put("borrower", borrowerDetailVO);
        return result;
    }

    @Resource
    private LendService lendService;

    /**
     * 审批借款信息
     * @param borrowInfoApprovalVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {

        //修改借款信息状态
        Long borrowInfoId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);

        //审核通过则创建标的
        if (borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()) {
            //创建标的
            lendService.createLend(borrowInfoApprovalVO, borrowInfo);
        }
    }
}
