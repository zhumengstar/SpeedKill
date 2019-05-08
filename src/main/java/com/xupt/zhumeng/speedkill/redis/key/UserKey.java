package com.xupt.zhumeng.speedkill.redis.key;

import com.xupt.zhumeng.speedkill.redis.BasePrefix;

/**
 * @author:zhumeng
 * @desc:
 **/
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
