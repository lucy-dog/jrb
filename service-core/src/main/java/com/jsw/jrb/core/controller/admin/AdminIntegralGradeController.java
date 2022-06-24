package com.jsw.jrb.core.controller.admin;


import com.jsw.common.exception.Assert;
import com.jsw.common.exception.BusinessException;
import com.jsw.common.result.R;
import com.jsw.common.result.ResponseEnum;
import com.jsw.jrb.core.pojo.entity.IntegralGrade;
import com.jsw.jrb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author JinSiWen
 * @since 2022-06-17
 */
@Api(tags="积分等级管理")
//@CrossOrigin //支持跨域
@Slf4j
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    /**
     *  获取积分登记表信息
     * @return 返回所有信息
     */
    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R listAll(){
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list",list).message("获取列表成功");
    }

    /**
     * 逻辑删除接口
     * @param id 主键id
     * @return 成功返回ture
     */
    @ApiOperation(value = "根据id删除", notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable String id){
        boolean b = integralGradeService.removeById(id);
        if (b){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("新增积分等级对象")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade){ //RequestBody json格式数据

        //如果借款额度为空就手动抛出一个自定义的异常
       /* if (integralGrade.getBorrowAmount() == null){
            //BORROW_AMOUNT_NULL_ERROR(-201, "借款额度不能为空"),
            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        }
        */

        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean result = integralGradeService.save(integralGrade);
        return result ? R.ok().message("新增成功") :  R.error().message("新增失败");
    }

    @ApiOperation("根据id修改积分等级对象")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade){ //RequestBody json格式数据
        boolean result = integralGradeService.updateById(integralGrade);
        return result ? R.ok().message("修改成功") :  R.error().message("修改失败");
    }

    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "数据id", required = true, example = "1")
            @PathVariable Long id){ //RequestBody json格式数据
        IntegralGrade integralGrade = integralGradeService.getById(id);
        return integralGrade != null ? R.ok().data("record", integralGrade) :  R.error().message("数据不存在");
    }
}

