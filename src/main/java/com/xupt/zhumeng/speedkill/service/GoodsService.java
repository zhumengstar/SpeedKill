package com.xupt.zhumeng.speedkill.service;

import com.xupt.zhumeng.speedkill.dao.GoodsDao;
import com.xupt.zhumeng.speedkill.entity.MsGoods;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    OrderService orderService;


    public List<GoodsVO> listGoodsVO() {
        return goodsDao.listGoodsVO();
    }


    public GoodsVO getGoodsVOByGoodsId(long goodsId) {
        return goodsDao.getGoodsVOByGoodsId(goodsId);
    }


    public boolean reduceStock(GoodsVO goodsVO) {


//        OrderInfo orderInfo = orderService.createOrder(user, goodsVO);
//
//        MsGoods g = new MsGoods();
//        g.setGoodsId(goodsVO.getId());
//        g.setStockCount(goodsVO.getGoodsStock());

        int ret = goodsDao.reduceStock(goodsVO.getId());
        return ret > 0;
    }

    public void resetStock(List<GoodsVO> goodsList) {
        for (GoodsVO goods : goodsList) {
            MsGoods g = new MsGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
