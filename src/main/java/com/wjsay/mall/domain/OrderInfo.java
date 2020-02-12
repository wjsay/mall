package com.wjsay.mall.domain;

import java.util.Date;

public class OrderInfo {
    private Long id;
    private int userId;
    private Long goodsId;
    private Long deliveryAddressId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setOrderChannel(Integer orderChannel) {
        this.orderChannel = orderChannel;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public Integer getOrderChannel() {
        return orderChannel;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
