package com.xupt.zhumeng.speedkill.vo;

import com.xupt.zhumeng.speedkill.entity.Goods;

import java.util.Date;

/**
 * 秒杀商品信息
 */
public class GoodsVO extends Goods {
    private Double msPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public Double getMsPrice() {
        return msPrice;
    }

    public void setMsPrice(Double msPrice) {
        this.msPrice = msPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
