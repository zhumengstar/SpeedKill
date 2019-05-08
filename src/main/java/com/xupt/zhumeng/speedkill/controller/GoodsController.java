package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.GoodsKey;
import com.xupt.zhumeng.speedkill.service.GoodsService;
import com.xupt.zhumeng.speedkill.service.UserService;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //查询商品列表
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) { //如果模版不是空,就保存到缓存中
            redisService.set(GoodsKey.getGoodsList, "", html);
        }


        return html;
    }

    //snowflake
    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable("goodsId") Long goodsId) {
        model.addAttribute("user", user);
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//
//        User user = userService.getByToken(response, token);
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //
        long startTime = goods.getStartDate().getTime();
        long endStart = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status = 0;
        int remainSeconds = 0;

        if (now < startTime) {
            //秒杀未开始
            status=0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endStart) {
            //秒杀已结束
            status=2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            status = 1;
            remainSeconds = 0;
        }
        model.addAttribute("status", status);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }


}
