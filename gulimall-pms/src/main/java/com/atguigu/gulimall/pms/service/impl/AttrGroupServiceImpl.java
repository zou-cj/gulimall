package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageListCategory(QueryCondition queryCondition, Integer catId) {
        //获取分页条件
        IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(queryCondition);

        //构造查询条件
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);
        IPage<AttrGroupEntity> data = this.page(page, queryWrapper);

        return new PageVo(data);
    }

    @Override
    public AttrGroupWithAttrsVo queryAllGroupsInfoByGroupId(Integer attrGroupId) {
        AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();

        //查出当前组的信息
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        BeanUtils.copyProperties(attrGroupEntity,attrGroupWithAttrsVo);

        //查询当前组和属性的所有关联关系

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",attrGroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(queryWrapper);
        attrGroupWithAttrsVo.setRelationEntities(relationEntities);

        //查询当前分组的所有属性
        ArrayList<Long> attrIds = new ArrayList<>();
        relationEntities.forEach(item ->{
            attrIds.add(item.getAttrId());
        });

        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);

        attrGroupWithAttrsVo.setAttrEntities(attrEntities);
        return attrGroupWithAttrsVo;
    }

}