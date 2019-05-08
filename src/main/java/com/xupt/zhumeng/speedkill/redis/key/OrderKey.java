package com.xupt.zhumeng.speedkill.redis.key;

import com.xupt.zhumeng.speedkill.redis.BasePrefix;

/**
 * @author:zhumeng
 * @desc:
 **/
public class OrderKey extends BasePrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
