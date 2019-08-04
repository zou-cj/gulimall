package com.atguigu.gulimall.pms.controller;

import java.util.Arrays;
import java.util.Map;


import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import com.atguigu.gulimall.pms.vo.SaveAttrVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;




/**
 * 商品属性
 *
 * @author zoucj
 * @email 1095768010@qq.com
 * @date 2019-08-01 21:18:15
 */
@Api(tags = "商品属性 管理")
@RestController
@RequestMapping("pms/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * /pms/attr/info/{attrId}
     * 查出某个属性，以及这个属性所属的分组信息
     */
    @ApiOperation("查出某个属性，以及这个属性所属的分组信息")
    @GetMapping("info/{attrId}")
    public Resp<AttrWithGroupVo> listInfo(@PathVariable Long attrId) {
        AttrWithGroupVo attrWithGroupVo = attrService.queryGroupByAttrId(attrId);

        return Resp.ok(attrWithGroupVo);
    }


    /**
     * 查出商品的销售属性/pms/attr/sale/226?t=1564887269020&limit=10&page=1
     * 与查询基本属性的方法一样，通过传入的属性类型不同分别
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("sale/{attrId}")
//    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> attrSale(@PathVariable(required = true) Long attrId,
                                  @RequestParam(defaultValue ="0")Integer attrType,
                                  QueryCondition queryCondition) {
        PageVo page = attrService.queryPageByCatelogId(queryCondition,attrId,attrType);

        return Resp.ok(page);
    }


    /**
     * 查出商品的基本属性/pms/attr/base/225
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("base/{attrId}")
//    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> attrBase( @PathVariable(required = true) Long attrId,
                                  @RequestParam(defaultValue ="1")Integer attrType,
                                  QueryCondition queryCondition) {
        PageVo page = attrService.queryPageByCatelogId(queryCondition,attrId,attrType);

        return Resp.ok(page);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrService.queryPage(queryCondition);

        return Resp.ok(page);
    }


//    /**
//     * 信息
//     */
//    @ApiOperation("详情查询")
//    @GetMapping("/info/{attrId}")
//    @PreAuthorize("hasAuthority('pms:attr:info')")
//    public Resp<AttrEntity> info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
//
//        return Resp.ok(attr);
//    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attr:save')")
    public Resp<Object> save(@RequestBody SaveAttrVo attrVo){
		attrService.saveAttrAndRelation(attrVo);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attr:update')")
    public Resp<Object> update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attr:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return Resp.ok(null);
    }

}
