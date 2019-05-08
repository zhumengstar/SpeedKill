package com.xupt.zhumeng.speedkill.entity;


/**
 * 秒杀订单，关联订单信息
 */
public class MsOrder {
    private Long id;//秒杀订单id
    private Long userId;//用户id
    private Long orderId;//订单id
    private Long goodsId;//商品id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
