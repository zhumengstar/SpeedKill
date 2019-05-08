package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.controller.LoginController;
import com.xupt.zhumeng.speedkill.dao.UserDao;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.execption.GlobalException;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.SpeedKey;
import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.util.MD5Util;
import com.xupt.zhumeng.speedkill.util.UUIDUtil;
import com.xupt.zhumeng.speedkill.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:zhumeng
 * @desc:
 **/
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    RedisService redisService;
    @Autowired
    UserDao userDao;

    public User getById(Long id) {
        return userDao.getById(id);
    }

    /**
     * 账号登录验证
     *
     * @param loginVO
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVO loginVO) {
        if (loginVO == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVO.getMobile();
        String formPassword = loginVO.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbSalt = user.getSalt();
        String dbPass = user.getPassword();

        if (!dbPass.equals(MD5Util.formPassToDBPass(formPassword, dbSalt))) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成token加入response
        String token = UUIDUtil.uuid();
        redisService.set(SpeedKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SpeedKey.token.expireSeconds());
        cookie.setPath("/");
        log.info(token);
        response.addCookie(cookie);
        return true;
    }

    public User getByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redisService.get(SpeedKey.token, token, User.class);
    }
}
