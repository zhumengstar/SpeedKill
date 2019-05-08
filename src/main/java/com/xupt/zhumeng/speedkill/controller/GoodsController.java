package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        return "goods_list";
    }

    @RequestMapping("/to_detail")
    public String toDetail(Model model, User user) {

//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//
//        User user = userService.getByToken(response, token);
//
//        model.addAttribute("user", user);
        return "goods_list";
    }


}
