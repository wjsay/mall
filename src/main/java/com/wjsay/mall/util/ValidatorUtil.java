package com.wjsay.mall.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern mobild_patten = Pattern.compile("1\\d{10}");
    public static boolean isMobileNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        Matcher matcher = mobild_patten.matcher(number);
        return matcher.matches();
    }
}
