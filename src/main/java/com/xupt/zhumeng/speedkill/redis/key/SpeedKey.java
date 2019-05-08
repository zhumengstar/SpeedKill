package com.xupt.zhumeng.speedkill.redis.key;

import com.xupt.zhumeng.speedkill.redis.BasePrefix;

/**
 * @author:zhumeng
 * @desc:
 **/
public class SpeedKey extends BasePrefix {


    private static final int TOKEN_EXPIRE_SECONDS = 2000;

    public SpeedKey(String prefix) {
        super(prefix);
    }

    public SpeedKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpeedKey token = new SpeedKey(TOKEN_EXPIRE_SECONDS, "tk");
}
