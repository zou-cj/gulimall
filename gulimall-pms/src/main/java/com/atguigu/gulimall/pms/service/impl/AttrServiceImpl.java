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
import com.atguigu.gulimall.pms.service.AttrService;
import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import com.atguigu.gulimall.pms.vo.SaveAttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrDao attrDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageByCatelogId(QueryCondition queryCondition, Long catelogId, Integer attrType) {
        //获取分页条件
        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(queryCondition);
        //构造查询条件
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type",attrType).eq("catelog_id",catelogId);

        IPage<AttrEntity> data = this.page(page, queryWrapper);


        return new PageVo(data);
    }

    @Override
    public void saveAttrAndRelation(SaveAttrVo attrVo) {
        //将vo里的属性分别保存到AttrEntity对象和他们的关系对象中存入数据库
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        attrDao.insert(attrEntity);

        //构建关联表对象存入数据库
        Long attrId = attrEntity.getAttrId();
        Long attrGroupId = attrVo.getAttrGroupId();
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrId);
        relationEntity.setAttrGroupId(attrGroupId);
        relationDao.insert(relationEntity);
    }

    @Override
    public AttrWithGroupVo queryGroupByAttrId(Long attrId) {
        AttrWithGroupVo attrWithGroupVo = new AttrWithGroupVo();
        //查询attr的信息,查出关系表的信息  根据关系表查出groupattr的信息
        AttrEntity attrEntity = attrDao.selectById(attrId);
        BeanUtils.copyProperties(attrEntity,attrWithGroupVo);

        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));


        if (relationEntity!=null){
            Long attrGroupId = relationEntity.getAttrGroupId();

            AttrGroupEntity groupEntity = attrGroupDao.selectById(attrGroupId);
            attrWithGroupVo.setAttrGroupEntity(groupEntity);
        }

        return attrWithGroupVo;
    }

}