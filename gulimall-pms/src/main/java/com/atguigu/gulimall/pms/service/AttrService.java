package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import com.atguigu.gulimall.pms.vo.SaveAttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author zoucj
 * @email 1095768010@qq.com
 * @date 2019-08-01 21:18:15
 */
public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryPageByCatelogId(QueryCondition queryCondition, Long catelogId, Integer attrType);

    void saveAttrAndRelation(SaveAttrVo attr);

    AttrWithGroupVo queryGroupByAttrId(Long attrId);
}

