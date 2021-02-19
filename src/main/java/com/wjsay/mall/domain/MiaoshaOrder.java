package com.wjsay.mall.domain;

public class MiaoshaOrder {
    private Long id;
    private Integer userId;
    private Long orderId;
    private Long goodsId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }
}
