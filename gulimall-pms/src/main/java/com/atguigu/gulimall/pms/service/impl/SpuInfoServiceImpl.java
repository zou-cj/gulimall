package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.utils.ArrayToStringUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;
import com.atguigu.gulimall.pms.feign.SmsSkuSaleInfoFeignService;
import com.atguigu.gulimall.pms.service.SpuInfoService;
import com.atguigu.gulimall.pms.vo.savespu.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.savespu.SaleAttrVo;
import com.atguigu.gulimall.pms.vo.savespu.SkuVo;
import com.atguigu.gulimall.pms.vo.savespu.SpuAllSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDao spuInfoDao;

    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    ProductAttrValueDao proAttrValDao;

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuImagesDao skuImagesDao;

    @Autowired
    AttrDao attrDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    SmsSkuSaleInfoFeignService smsSkuSaleInfoFeignService;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryByConditionAndCatId(QueryCondition queryCondition, Long catId) {
        //1.封装查询条件
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        if (catId != null) {
            queryWrapper.eq("catalog_id", catId);

            if (queryCondition!=null&&StringUtils.isNotEmpty(queryCondition.getKey())) {
                queryWrapper.and(obj -> {
                    obj.like("spu_name", queryCondition.getKey()).or().like("id", queryCondition.getKey());
                    return obj;
                });
            }
        }
        //2.封装分页条件对象
        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(queryCondition);

        //3、去数据库查询
        IPage<SpuInfoEntity> data = this.page(page,queryWrapper);

        PageVo pageVo = new PageVo(data);
        return pageVo;
    }

    @GlobalTransactional(rollbackFor ={Exception.class} )
    @Override
    public void saveAllVo(SpuAllSaveVo spuAllSaveVo) {
        //1.保存spu基本信息，spu图片信息
        //1.1）、存spu的基本信息
        Long spuId = this.saveSpuBaseInfo(spuAllSaveVo);

        //1.2存spu的图片信息//将地址以字符串的形式存贮
        String[] spuImages = spuAllSaveVo.getSpuImages();
        this.saveSpuInfoImages(spuId,spuImages);
        //2.保存spu的基本属性信息
        List<BaseAttrVo> baseAttrs = spuAllSaveVo.getBaseAttrs();
        this.saveSpuBaseAttrs(spuId, baseAttrs);

        //3.保存下面的sku信息，sku图片信息到相应数据库及下面的下面的销售属性信息发送给微服务sms
        this.saveSkuInfos(spuId, spuAllSaveVo.getSkus());



        //3.保存积分折扣信息
    }

    /***
     *
     * 辅助saveAllVo方法完成spu下的sku的所有详情处理,及远程调用sms的方法
     * @param spuId
     * @param skus
     */
    private void saveSkuInfos(Long spuId, List<SkuVo> skus) {
        SpuInfoEntity spuInfoEntity = this.getById(spuId);
        //1.保存sku的info信息
        for (SkuVo skuVo:skus) {
            String[] images = skuVo.getImages();
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setPrice(skuVo.getPrice());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0,5));
            if (images != null&&images.length>0) {
                skuInfoEntity.setSkuDefaultImg(skuVo.getImages()[0]);

            }
            skuInfoEntity.setSkuDesc(skuVo.getSkuDesc());
            skuInfoEntity.setSkuName(skuVo.getSkuName());
            skuInfoEntity.setSkuSubtitle(skuVo.getSkuSubtitle());
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setSkuTitle(skuVo.getSkuTitle());
            skuInfoEntity.setWeight(skuVo.getWeight());
            //插入数据库
            skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();
            //保存sku对应的所有图片
            for (int i = 0; i <images.length ; i++) {
                SkuImagesEntity imagesEntity = new SkuImagesEntity();
                imagesEntity.setSkuId(skuId);
                imagesEntity.setDefaultImg(i == 0?1:0);
                imagesEntity.setImgUrl(images[i]);
                imagesEntity.setImgSort(0);
                skuImagesDao.insert(imagesEntity);
            }
            //3.当前所有的销售属性组合保存起来
            List<SaleAttrVo> saleAttrVos = skuVo.getSaleAttrs();

            for (SaleAttrVo attrVo:saleAttrVos) {
                //查询当前属性的信息
                SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                //查出这个属性的真正信息
                AttrEntity attrEntity = attrDao.selectById(attrVo.getAttrId());

                entity.setAttrId(attrVo.getAttrId());
                entity.setAttrName(attrEntity.getAttrName());
                entity.setAttrSort(0);
                entity.setAttrValue(attrVo.getAttrValue());
                entity.setSkuId(skuId);
                //插入数据库
                skuSaleAttrValueDao.insert(entity);

                //以上都是pms系统完成的工作

                List<SkuSaleInfoTo> skuSaleInfoTos = new ArrayList<>();
                //以下需要由sms完成，保存每一个sku的相关优惠数据
                SkuSaleInfoTo skuSaleInfoTo = new SkuSaleInfoTo();
                BeanUtils.copyProperties(skuVo,skuSaleInfoTo);
                skuSaleInfoTo.setSkuId(skuId);
                skuSaleInfoTos.add(skuSaleInfoTo);

                //发送给远程方法处理
                smsSkuSaleInfoFeignService.saveSkuSaleInfos(skuSaleInfoTos);


            }


        }


    }

    /***
     * 辅助saveAllVo方法完成spu基本属性信息的保存
     * @param spuId
     * @param baseAttrs
     */
    private void saveSpuBaseAttrs(Long spuId, List<BaseAttrVo> baseAttrs) {
        List<ProductAttrValueEntity> attrValueEntities = new ArrayList<>();
        for (BaseAttrVo baseAttrVo : baseAttrs){
            ProductAttrValueEntity entity = new ProductAttrValueEntity();
            entity.setAttrId(baseAttrVo.getAttrId());
            String[] selected = baseAttrVo.getValueSelected();
            entity.setAttrValue(ArrayToStringUtils.getString(selected,","));
            entity.setAttrSort(0);
            entity.setQuickShow(1);
            entity.setSpuId(spuId);
            attrValueEntities.add(entity);
        }
        proAttrValDao.insertBatch(attrValueEntities);

    }

    /***
     * 辅助saveAllVo方法完成spu图片信息的保存
     * @param spuId
     * @param spuImages
     */
    private void saveSpuInfoImages(Long spuId, String[] spuImages) {
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(ArrayToStringUtils.getString(spuImages,","));
        spuInfoDescDao.insertAndId(spuInfoDescEntity);
    }

    /**
     * 辅助saveAllVo方法完成spu基本信息保存(不包括图片)
     * 不重写，好辨别
     * @param spuAllSaveVo
     * @return
     */
    private Long saveSpuBaseInfo(SpuAllSaveVo spuAllSaveVo) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuAllSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(new Date());
        spuInfoDao.insert(spuInfoEntity);

        return spuInfoEntity.getId();
    }

}