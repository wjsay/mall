package com.wjsay.mall.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
