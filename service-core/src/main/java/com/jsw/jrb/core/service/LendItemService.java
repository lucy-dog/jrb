package com.jsw.jrb.core.service;

import com.jsw.jrb.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsw.jrb.core.pojo.vo.InvestVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface LendItemService extends IService<LendItem> {

    String commitInvest(InvestVO investVO);

    void notify(Map<String, Object> paramMap);

    //根据lendId获取投资记录
    List<LendItem> selectByLendId(Long lendId, Integer status);

    List<LendItem> selectByLendId(Long lendId);
}
