package com.xupt.zhumeng.speedkill.dao;

import com.xupt.zhumeng.speedkill.entity.MsOrder;
import com.xupt.zhumeng.speedkill.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    /**
     * 通过用户id和商品id查询秒杀订单
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from tb_order_ms where user_id = #{userId} and goods_id = #{goodsId}")
    MsOrder getMsOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * 将订单信息插入数据库
     *
     * @param orderinfo
     * @return
     */
    @Insert("insert into tb_orderinfo(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insertOrderInfo(OrderInfo orderinfo);

    /**
     * 将秒杀订单插入数据库
     *
     * @param msOrder
     * @return
     */
    @Insert("insert into tb_order_ms(user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    int insertMsOrder(MsOrder msOrder);

    /**
     * 通过订单id获取订单信息
     *
     * @param orderId
     * @return
     */
    @Select("select * from tb_orderinfo where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);

    /**
     * 删除所有订单
     */
    @Delete("truncate table tb_orderinfo")
    void deleteOrders();

    /**
     * 删除所有秒杀订单
     */
    @Delete("truncate table tb_order_ms")
    void deleteMsOrders();
}
