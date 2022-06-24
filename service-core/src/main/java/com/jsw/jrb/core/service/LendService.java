package com.jsw.jrb.core.service;

import com.jsw.jrb.core.pojo.entity.BorrowInfo;
import com.jsw.jrb.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsw.jrb.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface LendService extends IService<Lend> {

    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);

    List<Lend> selectList();

    Map<String, Object> getLendDetail(Long id);

    BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod);

    /**
     * 满标放款
     * @param lendId
     */
    void makeLoan(Long lendId);
}
