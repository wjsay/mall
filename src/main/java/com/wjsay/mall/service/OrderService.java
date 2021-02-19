package com.wjsay.mall.service;

import com.wjsay.mall.dao.OrderDao;
import com.wjsay.mall.domain.MiaoshaOrder;
import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.domain.OrderInfo;
import com.wjsay.mall.redis.OrderKey;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.validator.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoOrderByUserIdGoodsId(int userId, long goodsId) {
        MiaoshaOrder order = null;
        order = redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
        if(order != null) {
            return order;
        } else {
            order = orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        }
        return order;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = null;
        try {
            orderInfo = new OrderInfo();
            orderInfo.setCreateDate(new Date());
            orderInfo.setDeliveryAddressId(0L);
            orderInfo.setGoodsCount(1);
            orderInfo.setGoodsId(goods.getId());
            orderInfo.setGoodsName(goods.getGoodsName());
            orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
            orderInfo.setOrderChannel(1);
            orderInfo.setStatus(0);
            orderInfo.setUserId(user.getId());
            orderDao.insert(orderInfo);
            MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
            miaoshaOrder.setGoodsId(goods.getId());
            miaoshaOrder.setOrderId(orderInfo.getId());
            miaoshaOrder.setUserId(user.getId());
            orderDao.insertMiaoshaOrder(miaoshaOrder);
            redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getId() + "_" + goods.getId(), miaoshaOrder);
            return orderInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMiaoshaOrders();
    }

    public List<OrderInfo> getOrderInfoByUserId(int userId) {
        return orderDao.getOrderByUserId(userId);
    }
}
