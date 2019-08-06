package com.atguigu.gulimall.pms.vo.savespu;

import lombok.Data;

@Data
public class SaleAttrVo {
    /**
     * "attrId": 0, //属性id
     * "attrValue": "string" //属性值
     */
    private Long attrId;
    private String attrValue;
}
