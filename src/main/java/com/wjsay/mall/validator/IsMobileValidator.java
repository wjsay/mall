package com.wjsay.mall.validator;

import com.wjsay.mall.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobileNumber(s);
        }
        if(StringUtils.isEmpty(s)) {
            return true;
        }
        return ValidatorUtil.isMobileNumber(s);
    }
}
