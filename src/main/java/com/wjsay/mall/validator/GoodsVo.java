package com.wjsay.mall.validator;

import com.wjsay.mall.domain.Goods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public void setStartDate(String startDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        this.startDate = format.parse(startDate);
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        this.endDate = format.parse(endDate);
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
