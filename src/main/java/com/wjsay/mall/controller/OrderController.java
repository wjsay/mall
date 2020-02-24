package com.wjsay.mall.controller;

import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.domain.OrderInfo;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.CodeMsg;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.GoodsService;
import com.wjsay.mall.service.MiaoshaUserService;
import com.wjsay.mall.service.OrderService;
import com.wjsay.mall.validator.GoodsVo;
import com.wjsay.mall.validator.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/order")
@Controller
public class OrderController {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
                                      @RequestParam("orderId")long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(order);
        vo.setGoodsVo(goods);
        return Result.success(vo);
    }
    @RequestMapping("/my")
    public String queryOrder(Model model, MiaoshaUser user) {
        List<OrderInfo> orderList = orderService.getOrderInfoByUserId(user.getId());
        model.addAttribute("orderList", orderList);
        return "myorders";
    }
}
