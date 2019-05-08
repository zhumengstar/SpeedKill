package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.UserKey;
import com.xupt.zhumeng.speedkill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    RedisService redisService;


    @ResponseBody
    @RequestMapping("/redis/getById")
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @ResponseBody
    @RequestMapping("/redis/setById")
    public Result<Boolean> redisSet() {

        User user = new User();
        user.setId(1L);
        user.setNickname("hhy");

        Boolean b = redisService.set(UserKey.getById, "" + user.getId(), user);
        System.out.println(redisService.get(UserKey.getById, "" + user.getId(), User.class));
        return Result.success(b);
    }


    @ResponseBody
    @RequestMapping("/redis/setByName")
    public Result<Boolean> redisSet1() {
        Boolean b = redisService.set(UserKey.getByName, "hhy", "{\"id\":1,\"nickname\":\"hhy\"}");
        System.out.println(redisService.get(UserKey.getByName, "hhy", User.class));
        return Result.success(b);
    }

//    @ResponseBody
//    @RequestMapping("/redis/set2")
//    public Result<Boolean> redisSet2(@Param(value = "key") String key, @Param(value = "value") String value) {
//        Boolean b = redisService.set(UserKey.getByName, key, value);
//        System.out.println(redisService.get(UserKey.getByName, key, User.class));
//        return Result.success(b);
//    }
}
