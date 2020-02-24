package com.wjsay.mall.dao;

import com.wjsay.mall.domain.MiaoshaGoods;
import com.wjsay.mall.validator.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(MiaoshaGoods g);

    @Update("update miaosha_goods set stock_count = #{stockCount}, start_date=#{startDate}, end_date=#{endDate}")
    int updateMiaoshaGoods(@Param("stockCount")int stockCount, @Param("startDate")Date startDate, @Param("endDate")Date endDate);

    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false, statement = "select last_insert_id()")
    @Insert("insert into goods (goods_name, goods_img, goods_price, stock)" +
            " values(#{goodsName}, #{goodsImg}, #{goodsPrice}, #{stockCount})")
    int addGoods(GoodsVo goods); // 返回的修改数据

    @Insert("insert into miaosha_goods (goods_id, miaosha_price, stock_count, start_date, end_date) " +
            "values(#{id}, #{miaoshaPrice}, #{stockCount}, #{startDate}, #{endDate})")
    void addMiaoshaGoods(GoodsVo goods);

}
