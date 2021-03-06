package com.xupt.zhumeng.speedkill.config;

import com.xupt.zhumeng.speedkill.controller.LoginController;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//方法参数解析器
@Service//注入Service也必须由框架管理
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    //参数解析的顶级父类
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    UserService userService;

    //判断参数类型是否是支持,支持则范返回true
    //若参数包含User类型，则解析参数
    public boolean supportsParameter(MethodParameter parameter) {
        Class clazz = parameter.getParameterType();
        if (clazz == User.class) {
            return true;
        } else {
            return false;
        }
    }

    //处理参数
    //1.获得response
    //2.获取token
    //3.查询user信息,将user信息注入到User类型参数中

    /**
     * 解析参数是否由token
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        //param>cookie
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            //什么都不做
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        //返回Token
        return userService.getByToken(response, token);
    }

    //得到cookie值
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {

                log.info("tokenFromBrowser:{}", cookie.getValue());
                return cookie.getValue();
            }
        }
        return null;
    }
}
