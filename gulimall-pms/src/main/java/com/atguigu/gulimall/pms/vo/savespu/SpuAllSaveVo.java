package com.atguigu.gulimall.pms.vo.savespu;


import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;
@Data
public class SpuAllSaveVo  extends SpuInfoEntity {

    private String spuDescription;  //spu描述

    private String[] spuImages; //spu图片

    private List<BaseAttrVo> baseAttrs;//spu的基本属性

    private List<SkuVo> skus;//spu的基本属性

}
