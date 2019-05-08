package com.xupt.zhumeng.speedkill.validator;

import com.xupt.zhumeng.speedkill.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    //默认值false,用于接收注解上自定义的required
    private boolean required = false;

    //初始化方法拿到注解，自定义手机号验证器
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    //判断是否合法
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //如果允许为空 直接返回结果
        if (required) {
            //验证是否为合法手机号
            return ValidatorUtil.isMobile(value);
        } else {
            //如果不允许为空
            //验证是否为空
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
