package com.jsw.jrb.core.service;

import com.jsw.jrb.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsw.jrb.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface UserBindService extends IService<UserBind> {
    /**
     * 账户绑定提交到托管平台的数据
     */
    String commitBindUser(UserBindVO userBindVO, Long userId);

    void notify(Map<String, Object> paramMap);

    String getBindCodeByUserId(Long userId);

}
