package com.wjsay.mall.validator;

import com.wjsay.mall.domain.MiaoshaUser;

public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private  int remainSeconds = 0;
    private GoodsVo goods;
    private MiaoshaUser user;

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public MiaoshaUser getUser() {
        return user;
    }
}
