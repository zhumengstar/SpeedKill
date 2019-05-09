package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.MsOrder;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.service.GoodsService;
import com.xupt.zhumeng.speedkill.service.OrderService;
import com.xupt.zhumeng.speedkill.service.SpeedKillService;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/speedkill")
public class SpeedKillController {
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SpeedKillService speedKillService;

    @RequestMapping("/do_speedkill")
    public String list(Model model, User user, @RequestParam("goodsId") Long goodsId) {

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
        MsOrder order = orderService.getSpeedKillOrderByUserIdGoodsId(user.getId(), goodsVO.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MS);
            return "ms_fail";
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = speedKillService.speedKill(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return "goods_list";
    }

}
