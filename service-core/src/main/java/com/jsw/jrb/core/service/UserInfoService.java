package com.jsw.jrb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsw.jrb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsw.jrb.core.pojo.query.UserInfoQuery;
import com.jsw.jrb.core.pojo.vo.LoginVO;
import com.jsw.jrb.core.pojo.vo.RegisterVO;
import com.jsw.jrb.core.pojo.vo.UserIndexVO;
import com.jsw.jrb.core.pojo.vo.UserInfoVO;


/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO, String ip);

    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);

    UserIndexVO getIndexUserInfo(Long userId);
}
