package com.jsw.jrb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsw.jrb.core.pojo.entity.UserLoginRecord;
import com.jsw.jrb.core.mapper.UserLoginRecordMapper;
import com.jsw.jrb.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

    @Override
    public List<UserLoginRecord> listTop50(Long userId) {
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id", userId)
                .orderByDesc("id")
                .last("limit 50");
        List<UserLoginRecord> userLoginRecords = baseMapper.selectList(userLoginRecordQueryWrapper);
        return userLoginRecords;
    }
}
