package com.wjsay.mall.execption;

import com.wjsay.mall.result.CodeMsg;

public class GlobalExecption extends RuntimeException {
    private static final long serivalVersionUID = 1L;

    private CodeMsg cm;

    public GlobalExecption(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }
    public CodeMsg getCm() {
        return cm;
    }
}
