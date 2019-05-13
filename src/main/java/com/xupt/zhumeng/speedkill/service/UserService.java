package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.controller.LoginController;
import com.xupt.zhumeng.speedkill.dao.UserDao;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.execption.GlobalException;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.SpeedKey;
import com.xupt.zhumeng.speedkill.redis.key.UserKey;
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

    /**
     * 数据库查询用户
     *
     * @param id
     * @return
     */
    public User getById(Long id) {

        //对象进行缓存
        User user = redisService.get(UserKey.getById, "" + id, User.class);

        if (user != null) {
            return user;
        }
        user = userDao.getById(id);
        if (user != null) {
            redisService.set(UserKey.getById, "" + user.getId(), user);
        }
        return user;

    }

    public boolean updatePassword(String token, long id, String formPassword) {

        User user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        User toUpdateUser = new User();
        toUpdateUser.setId(id);
        toUpdateUser.setPassword(MD5Util.formPassToDBPass(formPassword, user.getSalt()));
        //更新密码
        userDao.update(user);
        //处理缓存
        redisService.delete(UserKey.getById, "" + user.getId());
        user.setPassword(toUpdateUser.getPassword());
        redisService.set(UserKey.token, token, user);

        return true;
    }

    /**
     * 账号登录验证
     *
     * @param loginVO
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVO loginVO) {
//        log.info(loginVO.getMobile());
//        log.info(loginVO.getPassword());

        if (loginVO == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        log.info("after"+loginVO.getMobile());
        log.info(loginVO.getPassword());

        String mobile = loginVO.getMobile();
        String formPassword = loginVO.getPassword();
//        判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
//        验证密码
        String dbSalt = user.getSalt();
        String dbPass = user.getPassword();

        if (!dbPass.equals(MD5Util.formPassToDBPass(formPassword, dbSalt))) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        log.info("addcookie");
        //生成token加入response
        addCookie(response, user);
        return true;
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //
        User user = redisService.get(SpeedKey.token, token, User.class);

        if (user != null) {
            //用户存在才设置
            //延迟token有效期，重新生成
            addCookie(response, user);
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, User user) {
        //生成token加入response
        String token = UUIDUtil.uuid();
        redisService.set(SpeedKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SpeedKey.token.expireSeconds());
        cookie.setPath("/");
        log.info("AddToken:{}", token);
        response.addCookie(cookie);
    }
}
