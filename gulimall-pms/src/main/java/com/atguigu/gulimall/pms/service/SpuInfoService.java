package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.savespu.SpuAllSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import org.springframework.stereotype.Service;


/**
 * spu信息
 *
 * @author zoucj
 * @email 1095768010@qq.com
 * @date 2019-08-01 21:18:15
 */

public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryByConditionAndCatId(QueryCondition queryCondition, Long catId);

    void saveAllVo(SpuAllSaveVo spuAllSaveVo);

}

