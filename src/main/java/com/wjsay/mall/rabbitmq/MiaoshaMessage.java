package com.wjsay.mall.rabbitmq;

import com.wjsay.mall.domain.MiaoshaUser;

public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public long getGoodsId() {
        return goodsId;
    }
}
