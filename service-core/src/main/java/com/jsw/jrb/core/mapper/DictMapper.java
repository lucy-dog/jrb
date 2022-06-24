package com.jsw.jrb.core.mapper;

import com.jsw.jrb.core.pojo.dto.ExcelDictDTO;
import com.jsw.jrb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
public interface DictMapper extends BaseMapper<Dict> {
    void insertBatch(List<ExcelDictDTO> list);
}
