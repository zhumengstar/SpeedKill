package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:zhumeng
 * @desc:
 **/
@Service
public class MsService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo ms(User user, GoodsVO goodsVO) {
        //减少库存 下订单 写入秒杀订单
        goodsService.reduceStock(goodsVO);
        return orderService.createOrder(user, goodsVO);
    }
}
