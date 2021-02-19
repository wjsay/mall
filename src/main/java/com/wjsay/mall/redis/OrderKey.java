package com.wjsay.mall.redis;

public class OrderKey extends BasePrefix {
    public OrderKey(String prifix) {
        super(prifix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
