package com.xupt.zhumeng.speedkill.dao;

import com.xupt.zhumeng.speedkill.entity.MsGoods;
import com.xupt.zhumeng.speedkill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    /**
     * 秒杀商品列表
     *
     * @return
     */
    @Select("select g.*,mg.ms_price,mg.stock_count,mg.start_date,mg.end_date from tb_goods_ms mg left join tb_goods g on mg.goods_id = g.id")
    List<GoodsVO> listGoodsVO();

    /**
     * 秒杀商品
     *
     * @param goodsId
     * @return
     */
    @Select("select g.*,mg.ms_price,mg.stock_count,mg.start_date,mg.end_date from tb_goods_ms mg left join tb_goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVO getGoodsVOByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 减库存
     *
     * @param goodsId
     * @return
     */
    @Update("update tb_goods_ms set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(@Param("goodsId") long goodsId);

    /**
     * 重制某商品库存
     *
     * @param g
     * @return
     */
    @Update("update tb_goods_ms set stock_count = #{stockCount} where goods_id = #{goodsId}")
    int resetStock(MsGoods g);
}
