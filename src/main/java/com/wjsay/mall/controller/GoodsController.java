package com.wjsay.mall.controller;

import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.redis.GoodsKey;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.GoodsService;
import com.wjsay.mall.service.MiaoshaUserService;
import com.wjsay.mall.validator.GoodsDetailVo;
import com.wjsay.mall.validator.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

    // 194 TPS。用了redis反而慢了
    @RequestMapping("/to_list")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
//        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        SpringWebContext context = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),
                model.asMap(), applicationContext);
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html); // 页面换存
        }
        return html;
    }

//    @RequestMapping("/to_detail/{goodsId}")
//    public String detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId")long goodsid) {
//        model.addAttribute("user", miaoshaUser);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsid);
//        model.addAttribute("goods", goods);
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        int miaoshaStatus = 0;
//        int remainSeconds = 0;
//        if (now < startAt) {
//            miaoshaStatus = 0;
//            remainSeconds = (int)((startAt - now) / 1000);
//        } else if (now > endAt) {
//            miaoshaStatus = 2;
//            remainSeconds = -1;
//        } else {
//            miaoshaStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";
//    }  // templates/goods_detali.html 可删除

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId") Long goodsId) {
        if (goodsId == null) {
            GoodsDetailVo vo = new GoodsDetailVo();
            return Result.success(vo);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (start > now) {
            miaoshaStatus = 0;
            remainSeconds = (int)((start - now) / 1000);
        } else if (now > end) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(user);
        return Result.success(vo);
    }
}
