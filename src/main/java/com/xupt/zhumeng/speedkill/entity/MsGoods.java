package com.xupt.zhumeng.speedkill.entity;

import java.util.Date;

/**
 * 秒杀商品
 */
public class MsGoods {
    private Long id;//id
    private Long goodsId;//商品id
    private Double msPrice;//秒杀价格
    private Integer stockCount;//库存数
    private Date startDate;//开始日期
    private Date endDate;//结束日期

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

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
