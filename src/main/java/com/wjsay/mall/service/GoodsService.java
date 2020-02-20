package com.wjsay.mall.service;

import com.wjsay.mall.dao.GoodsDao;
import com.wjsay.mall.domain.MiaoshaGoods;
import com.wjsay.mall.validator.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for (GoodsVo goods: goodsList) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStackCount(goods.getStockCount());
            goodsDao.reduceStock(g);
        }
    }

    public void updateMiaoshaGoods(int count, Date startDate, Date endDate) {
        goodsDao.updateMiaoshaGoods(count, startDate, endDate);
    }

    public void addMiaoshaGoods(GoodsVo goodsVo) {
        goodsDao.addGoods(goodsVo);
        goodsDao.addMiaoshaGoods(goodsVo);
    }
}
