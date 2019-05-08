package com.xupt.zhumeng.speedkill.vo;

import com.xupt.zhumeng.speedkill.entity.OrderInfo;

/**
 * 订单详情
 */
public class OrderDetailVO {
    private GoodsVO goods;
    private OrderInfo orderInfo;

    public GoodsVO getGoods() {
        return goods;
    }

    public void setGoods(GoodsVO goods) {
        this.goods = goods;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
