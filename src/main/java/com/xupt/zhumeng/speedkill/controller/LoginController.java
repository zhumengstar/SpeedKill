package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.result.Result;
import com.xupt.zhumeng.speedkill.service.UserService;
import com.xupt.zhumeng.speedkill.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) {

//        log.info(loginVO.toString());

//        String passInput = loginVO.getPassword();
//        String mobile = loginVO.getMobile();
//        if (StringUtils.isEmpty(passInput)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobile)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录
        userService.login(response, loginVO);
        return Result.success(true);
    }


}
