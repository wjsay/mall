package com.wjsay.mall.result;

public class CodeMsg {
    private int code;
    private String msg;

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private CodeMsg() {}

    // 通用错误代码
    public static final CodeMsg SUCCESS = new CodeMsg(0, "surress");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务器内部错误");
    public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500101, "参数校验异常：%s");
    public static final CodeMsg BIND_ERROR = new CodeMsg(500102, "参数校验异常");
    public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500103, "访问频繁");
    public static final CodeMsg RESULT_ERROR = new CodeMsg(500104, "计算错误，刷新  ");
    public static final CodeMsg SELL_OUT = new CodeMsg(500105, "售空");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg USER_EXIST = new CodeMsg(500216, "用户已存在");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

    // 秒杀模块 5005XX
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "商品秒杀完毕");
    public static CodeMsg REPEAT_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败");

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public CodeMsg fillArgs(Object... args) {
        String message = String.format(this.msg, args);
        return new CodeMsg(this.code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
