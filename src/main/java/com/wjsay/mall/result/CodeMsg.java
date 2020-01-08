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
    public static final CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常");

    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
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
