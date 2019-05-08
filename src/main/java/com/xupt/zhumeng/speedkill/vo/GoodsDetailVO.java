package com.xupt.zhumeng.speedkill.vo;

import com.xupt.zhumeng.speedkill.entity.User;

/**
 * 商品详细信息
 */
public class GoodsDetailVO {

    private int msStatus = 0;
    private int remainSeconds = 0;
    private GoodsVO goods;
    private User user;

    public int getMsStatus() {
        return msStatus;
    }

    public void setMsStatus(int msStatus) {
        this.msStatus = msStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVO getGoods() {
        return goods;
    }

    public void setGoods(GoodsVO goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
