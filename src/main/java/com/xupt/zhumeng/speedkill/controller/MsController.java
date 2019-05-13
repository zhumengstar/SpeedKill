package com.xupt.zhumeng.speedkill.controller;

import com.xupt.zhumeng.speedkill.entity.MsOrder;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import com.xupt.zhumeng.speedkill.entity.User;
import com.xupt.zhumeng.speedkill.rabbitmq.MQSender;
import com.xupt.zhumeng.speedkill.rabbitmq.MsMessage;
import com.xupt.zhumeng.speedkill.redis.RedisService;
import com.xupt.zhumeng.speedkill.redis.key.GoodsKey;
import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.result.Result;
import com.xupt.zhumeng.speedkill.service.GoodsService;
import com.xupt.zhumeng.speedkill.service.MsService;
import com.xupt.zhumeng.speedkill.service.OrderService;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zhumeng
 * @desc:
 **/
@Controller
@RequestMapping("/ms")
public class MsController implements InitializingBean {
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MsService msService;

    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();


    //系统初始化
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        if(goodsList == null) {
            return;
        }
        for (GoodsVO goods : goodsList) {
            redisService.set(GoodsKey.getMsGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false); //初始 没结束
        }
    }

    //静态化页面
    @RequestMapping("/do_ms")
    public String doMs(Model model, User user, @RequestParam("goodsId") Long goodsId) {

        if (user == null) {
            return "login";
        }

        //判断是否还有秒杀商品库存
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goodsVO.getGoodsStock();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MS_OVER);
            return "ms_fail";
        }
        //判断是否已经秒杀到商品
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsVO.getId());
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MS);
            return "ms_fail";
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return "order_detail";
    }






    @PostMapping(value = "/ms")
    @ResponseBody
    public Result<OrderInfo> ms(Model model, User user, @RequestParam("goodsId") Long goodsId) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断是否还有秒杀商品库存
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        int stock = goodsVO.getGoodsStock();
        if (stock <= 0) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //判断是否已经秒杀到商品
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsVO.getId());
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MS);
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = msService.ms(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return Result.success(orderInfo);
    }

    @RequestMapping(value = "/{path}/do_ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> path_ms(Model model, User user,
                              @RequestParam("goodsId")long goodsId,
                              @PathVariable("path")String path) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = msService.checkPath(user, goodsId, path);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //内存标记，减少Redis访问
        boolean over = localOverMap.get(goodsId); //false 没结束 true 结束
        if(over) {
            return Result.error(CodeMsg.MS_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMsGoodsStock, ""+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true); //结束
            return Result.error(CodeMsg.MS_OVER);
        }
        //判断是否已经秒杀到了
        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MS);
        }
        //入队
        MsMessage mm = new MsMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMsMessage(mm);
        return Result.success(0); //0代表排队中


//        //判断库存
//        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            return Result.error(CodeMsg.MS_OVER);
//        }
//        //判断是否已经秒杀到了
//        MsOrder order = orderService.getMsOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            return Result.error(CodeMsg.REPEATE_MS);
//        }
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = msService.ms(user, goods);
//        return Result.success(orderInfo);

    }

}
