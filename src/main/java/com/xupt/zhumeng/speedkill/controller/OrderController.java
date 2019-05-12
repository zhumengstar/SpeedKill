package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.result.Result;
import com.xupt.zhumeng.speedkill.service.GoodsService;
import com.xupt.zhumeng.speedkill.service.OrderService;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import com.xupt.zhumeng.speedkill.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }



        long goodsId = orderInfo.getGoodsId();
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderInfo(orderInfo);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
