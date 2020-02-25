package com.wjsay.mall.controller;

import com.wjsay.mall.domain.MiaoshaGoods;
import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.redis.GoodsKey;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.CodeMsg;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.GoodsService;
import com.wjsay.mall.service.MiaoshaUserService;
import com.wjsay.mall.validator.GoodsDetailVo;
import com.wjsay.mall.validator.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
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
    @Value("${spring.http.multipart.location}")
    private String location;

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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> addMiaoshaGoods(HttpServletRequest request,
                @RequestParam(value = "image") MultipartFile imageFile, GoodsVo goodsVo) { // 去掉@ResponseBody
        try {
            if (imageFile == null || goodsVo == null) {
                return Result.error(CodeMsg.REQUEST_ILLEGAL);
            }
            //String prefix =  ResourceUtils.getURL("classpath:").getPath()+"static";
            //logger.debug(prefix);
            // 如果是/images/name.jpg。执行transferTo时，linux会在根目录创建文件，而windows会在spring.http.multipart.location下创建文件
            String name = "images/" + System.currentTimeMillis() + "-" + imageFile.getOriginalFilename();
            File path = new File(location + "images/");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(name);
            imageFile.transferTo(file);  // 文件上传问题
            goodsVo.setGoodsImg("/" + name);
            goodsService.addMiaoshaGoods(goodsVo);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            MiaoshaController.updateCache(goodsVo); // 增加了耦合度，updateCache应该放在miaoshaService中
            return Result.success(CodeMsg.SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

}
