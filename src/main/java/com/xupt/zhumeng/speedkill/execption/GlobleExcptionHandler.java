package com.xupt.zhumeng.speedkill.execption;

import com.xupt.zhumeng.speedkill.result.CodeMsg;
import com.xupt.zhumeng.speedkill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:zhumeng
 * @desc: 自定义异常的处理器
 **/
@ControllerAdvice
@ResponseBody
public class GlobleExcptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<String> excptionHandle(HttpServletRequest request, Exception e) {
        /**
         * 是绑定异常
         */
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getAllErrors();

            ObjectError error = allErrors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }


}
