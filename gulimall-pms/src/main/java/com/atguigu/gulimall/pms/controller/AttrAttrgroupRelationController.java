package com.atguigu.gulimall.pms.controller;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.pms.vo.AttrRelationDeleteVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 属性&属性分组关联
 *
 * @author zoucj
 * @email 1095768010@qq.com
 * @date 2019-08-01 21:18:15
 */
@Api(tags = "属性&属性分组关联 管理")
@RestController
@RequestMapping("pms/attrattrgrouprelation")
public class AttrAttrgroupRelationController {
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 删除关联关系
     * 请求地址：/pms/attrattrgrouprelation/delete/attr 请求方式：POST
     * 请求参数：【数组方式】
     */
    @ApiOperation("删除关联关系")
    @PostMapping("/delete/attr")
//    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:delete')")
    public Resp<Object> removeRelation(@RequestBody AttrRelationDeleteVo[] vos) {
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        if (vos!=null&&vos.length>0){
            for (AttrRelationDeleteVo vo:vos) {
                queryWrapper.eq("attr_id",vo.getAttrId()).eq("attr_group_id",vo.getAttrGroupId());
            }

        }
        attrAttrgroupRelationService.remove(queryWrapper);
        return Resp.ok(null);
    }


    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrAttrgroupRelationService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:info')")
    public Resp<AttrAttrgroupRelationEntity> info(@PathVariable("id") Long id) {
        AttrAttrgroupRelationEntity attrAttrgroupRelation = attrAttrgroupRelationService.getById(id);

        return Resp.ok(attrAttrgroupRelation);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:save')")
    public Resp<Object> save(@RequestBody AttrAttrgroupRelationEntity attrAttrgroupRelation) {
        attrAttrgroupRelationService.save(attrAttrgroupRelation);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:update')")
    public Resp<Object> update(@RequestBody AttrAttrgroupRelationEntity attrAttrgroupRelation) {
        attrAttrgroupRelationService.updateById(attrAttrgroupRelation);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attrattrgrouprelation:delete')")
    public Resp<Object> delete(@RequestBody Long[] ids) {
        attrAttrgroupRelationService.removeByIds(Arrays.asList(ids));

        return Resp.ok(null);
    }

}
