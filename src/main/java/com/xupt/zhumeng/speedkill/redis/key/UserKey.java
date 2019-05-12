package com.xupt.zhumeng.speedkill.redis.key;

import com.xupt.zhumeng.speedkill.redis.BasePrefix;

/**
 * @author:zhumeng
 * @desc:
 **/
public class UserKey extends BasePrefix {


    public UserKey(String prefix) {
        super(prefix);
    }

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey(2000,"id");
    public static UserKey getByName = new UserKey(2000,"name");
    public static UserKey token = new UserKey(2000,"token");
}
