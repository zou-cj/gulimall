package com.atguigu.gulimall.pms.vo.savespu;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuVo {

    private String skuName;//sku名字

    private String skuDesc; //sku描述

    private String skuTitle; //sku标题

    private String skuSubtitle; //sku副标题

    private BigDecimal weight; //sku重量

    private BigDecimal price; //商品价格

    private String[] images;
    //=========以上sku的基本信息===========

    //当前sku对应得销售属性组合
    private List<SaleAttrVo> saleAttrs;
    //=========以上sku对应得销售属性组合===========


    /**
     * "buyBounds": 0, //赠送的购物积分   v
     * "growBounds": 0, //赠送的成长积分  v
     * "work": [0,1,1,0], //积分生效情况
     * <p>
     * <p>
     * "fullCount": 0, //满几件
     * "discount": 0, //打几折
     * "ladderAddOther": 0, //阶梯价格是否可以与其他优惠叠加
     * <p>
     * <p>
     * "fullPrice": 0, //满多少
     * "reducePrice": 0, //减多少
     * "fullAddOther": 0, //满减是否可以叠加其他优惠
     */
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    //优惠生效情况[1111（四个状态位，从右到左）;
    // 0 - 无优惠，成长积分是否赠送;
    // 1 - 无优惠，购物积分是否赠送;
    // 2 - 有优惠，成长积分是否赠送;
    // 3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]
    private Integer[] work;
    //上面是  积分设置的信息


    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;
    //上面是  阶梯价格的信息

    /**
     * "fullPrice": 0, //满多少
     * "reducePrice": 0, //减多少
     * "fullAddOther": 0, //满减是否可以叠加其他优惠
     */
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;
}


