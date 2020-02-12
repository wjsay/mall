package com.wjsay.mall.validator;

import com.wjsay.mall.domain.OrderInfo;

public class OrderDetailVo {
    private GoodsVo goodsVo;
    private OrderInfo orderInfo;

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }
}
