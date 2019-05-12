package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.MsOrder;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.result.Result;
import com.xupt.zhumeng.speedkill.service.GoodsService;
import com.xupt.zhumeng.speedkill.service.MsService;
import com.xupt.zhumeng.speedkill.service.OrderService;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/ms")
public class MsController {
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @RequestMapping("/do_ms")
    public String doMs(Model model, User user, @RequestParam("goodsId") Long goodsId) {

        if (user == null) {
            return "login";
        }

        //判断是否还有秒杀商品库存
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goodsVO.getGoodsStock();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MS_OVER);
            return "ms_fail";
        }
        //判断是否已经秒杀到商品
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsVO.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MS);
            return "ms_fail";
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return "order_detail";
    }

    @PostMapping(value = "/ms")
    @ResponseBody
    public Result<OrderInfo> ms(Model model, User user, @RequestParam("goodsId") Long goodsId) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断是否还有秒杀商品库存
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goodsVO.getGoodsStock();
        if (stock <= 0) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //判断是否已经秒杀到商品
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsVO.getId());
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MS);
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return Result.success(orderInfo);
    }

}
