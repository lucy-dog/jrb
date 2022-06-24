package com.jsw.jrb.core.service;

import com.jsw.jrb.core.pojo.dto.ExcelDictDTO;
import com.jsw.jrb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;


/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface DictService extends IService<Dict> {
    //导入数据字典
    void importData(InputStream inputStream);

    //导出数据字典
    List<ExcelDictDTO> listDictData();

    //根据上级id展示数据字典
    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
