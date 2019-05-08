package com.xupt.zhumeng.speedkill.redis.key;

/**
 * @author:zhumeng
 * @desc:
 **/

public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();


}
