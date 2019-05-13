package com.xupt.zhumeng.speedkill.redis;

import com.alibaba.fastjson.JSON;
import com.xupt.zhumeng.speedkill.redis.key.GoodsKey;
import com.xupt.zhumeng.speedkill.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: zhumeng
 * @desc: RedisService
 **/
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取key对应的value
     *
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;


            String str = jedis.get(realKey);

            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);

        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) return null;
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(str);
        } else return JSON.toJavaObject(JSON.parseObject(str), clazz);
    }

    /**
     * 返回到连接池
     *
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 设置key
     *
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();


            String str = beanToString(value);
            if (str == null) return false;

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;

            int seconds = keyPrefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }


            return true;
        } finally {
            returnToPool(jedis);

        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) return null;
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == Long.class || clazz == long.class) {
            return "" + value;
        } else
            return JSON.toJSONString(value);
    }

    /**
     * 判断key是否存在
     *
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Boolean exists(KeyPrefix keyPrefix, String key, Class<T> clazz) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;

            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * 增加值
     *
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix keyPrefix, String key, Class<T> clazz) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;

            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值原子操作
     *
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix keyPrefix, String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;

            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除缓存
     */
    public boolean delete(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey = keyPrefix.getPrefix() + key;

            return jedis.del(realKey) > 0;
        } finally {
            returnToPool(jedis);
        }

    }
}
