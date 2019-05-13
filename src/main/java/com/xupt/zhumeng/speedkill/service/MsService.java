package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.MsKey;
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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo ms(User user, GoodsVO goodsVO) {
        //减少库存 下订单 写入秒杀订单
        goodsService.reduceStock(goodsVO);
        return orderService.createOrder(user, goodsVO);
    }

    public boolean checkPath(User user, long goodsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MsKey.getMsPath, ""+user.getId()+"_"+goodsId, String.class);
        return path.equals(pathOld);
    }
}
