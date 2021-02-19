package com.wjsay.mall.domain;

public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsStock(Integer goodsStock) {
        this.goodsStock = goodsStock;
    }

    public Long getId() {
        return id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public Integer getGoodsStock() {
        return goodsStock;
    }
}
