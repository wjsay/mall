package com.wjsay.mall.controller;

import com.wjsay.mall.access.AccessLimit;
import com.wjsay.mall.domain.MiaoshaOrder;
import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.rabbitmq.MQSender;
import com.wjsay.mall.rabbitmq.MiaoshaMessage;
import com.wjsay.mall.redis.GoodsKey;
import com.wjsay.mall.redis.OrderKey;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.CodeMsg;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.GoodsService;
import com.wjsay.mall.service.MiaoshaService;
import com.wjsay.mall.service.MiaoshaUserService;
import com.wjsay.mall.service.OrderService;
import com.wjsay.mall.validator.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    MQSender sender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods: goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        try {
            List<GoodsVo> goodsList = goodsService.listGoodsVo();
            for (GoodsVo goods : goodsList) {
                goods.setStockCount(10);
                redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), 10);
                localOverMap.put(goods.getId(), false);
            }
            orderService.deleteOrders();
            redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
            int count = 50;
            Date start = new Date(System.currentTimeMillis() + 15 * 1000);
            Date end = new Date(start.getTime() + 60 * 4 * 1000);
            goodsService.updateMiaoshaGoods(count, start, end);
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

//    @RequestMapping("/do_miaosha")
//    public String list(Model model,MiaoshaUser user,
//                       @RequestParam("goodsId")long goodsId) {
//        model.addAttribute("user", user);
//        if(user == null) {
//            return "login";
//        }
//        //判断库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
//            return "miaosha_fail";
//        }
//        //判断是否已经秒杀到了
//        MiaoshaOrder order = orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        if (orderInfo == null) return "miaosha_fail";
//        model.addAttribute("orderInfo", orderInfo);
//        if (goods == null) return "miaosha_fail";
//        model.addAttribute("goods", goods);
//        return "order_detail";
//    }

    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @PathVariable("path")String path,
                                   @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user",  user);
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        Boolean over = localOverMap.get(goodsId);
        if (over != null && over) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        MiaoshaOrder order = orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {  // 可能两个用户走到这，不怕
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SELL_OUT);
        }
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0); //排队中
    }

    // -1秒杀失败，0排队中
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    // 隐藏秒杀地址
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0")int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.RESULT_ERROR);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user,
                                               @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }


}
