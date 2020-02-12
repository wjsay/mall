package com.wjsay.mall.rabbitmq;

import com.wjsay.mall.domain.MiaoshaOrder;
import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.service.GoodsService;
import com.wjsay.mall.service.MiaoshaService;
import com.wjsay.mall.service.OrderService;
import com.wjsay.mall.validator.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message: "+ message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        MiaoshaOrder order = orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        miaoshaService.miaosha(user, goods);
    }
}
