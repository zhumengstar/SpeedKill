package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.dao.GoodsDao;
import com.xupt.zhumeng.speedkill.dao.OrderDao;
import com.xupt.zhumeng.speedkill.entity.MsOrder;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.OrderKey;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: zhumeng
 * @desc: 订单
 **/
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    RedisService redisService;

    public MsOrder getMsOrderByUserIdGoodsId(Long userId, Long goodsId) {
//        return orderDao.getMsOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMsOrderByUidGid, "" + userId + "_" + goodsId, MsOrder.class);

    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVO goodsVO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);

        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsPrice(goodsVO.getMsPrice());
        orderInfo.setStatus(0);
        long orderId = orderDao.insertOrderInfo(orderInfo);

        MsOrder msOrder = new MsOrder();
        msOrder.setGoodsId(goodsVO.getId());
        msOrder.setOrderId(orderId);
        msOrder.setUserId(user.getId());
        orderDao.insertMsOrder(msOrder);

        redisService.set(OrderKey.getMsOrderByUidGid, "" + user.getId() + "_" + goodsVO.getId(), MsOrder.class);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
