package com.wjsay.mall.domain;

import java.util.Date;

public class MiaoshaGoods {
    private long id;
    private long goodsId;
    private double price;
    private int stackCount;
    private Date startDate;
    private Date endDate;

    public void setId(long id) {
        this.id = id;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStackCount(int stackCount) {
        this.stackCount = stackCount;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public double getPrice() {
        return price;
    }

    public int getStackCount() {
        return stackCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
