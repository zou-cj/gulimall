package com.atguigu.gulimall.pms.dao;

import com.atguigu.gulimall.pms.entity.SpuInfoDescEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息介绍
 * 
 * @author zoucj
 * @email 1095768010@qq.com
 * @date 2019-08-01 21:18:15
 */
@Mapper
public interface SpuInfoDescDao extends BaseMapper<SpuInfoDescEntity> {

    void insertAndId(@Param("entity") SpuInfoDescEntity entity);
}
