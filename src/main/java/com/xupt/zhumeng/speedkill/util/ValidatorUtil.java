package com.xupt.zhumeng.speedkill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    /**
     * 手机号的正则表达式
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(phone);
        return m.matches();
    }

}
