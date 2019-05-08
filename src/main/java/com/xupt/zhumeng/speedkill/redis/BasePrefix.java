package com.xupt.zhumeng.speedkill.redis;

import com.xupt.zhumeng.speedkill.redis.key.KeyPrefix;

/**
 * @author:zhumeng
 * @desc:
 **/
public abstract class BasePrefix implements KeyPrefix {


    private int expireSeconds;//过期时间

    private String prefix;//前缀

    //默认0代表永不过期
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String simpleName = getClass().getSimpleName();
        return simpleName + ":" + prefix;
    }
}
