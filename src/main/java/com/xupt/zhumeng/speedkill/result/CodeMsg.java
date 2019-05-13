package com.xupt.zhumeng.speedkill.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(50100, "服务端---异常");
    /**
     * 带参数错误码
     */
    public static CodeMsg BIND_ERROR = new CodeMsg(50101, "参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(50102, "请求非法");

    //登录模块 502XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(50210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(50211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(50212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(50213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(50214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(50215, "密码错误");

    //商品模块 503XX

    //订单模块 504XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(50400, "订单不存在");

    //秒杀模块 505XX
    public static CodeMsg MS_OVER = new CodeMsg(50500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_MS = new CodeMsg(50501, "你已经秒杀过了");
    public static CodeMsg MS_FAIL = new CodeMsg(50502, "秒杀失败");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    //带参数错错误码
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        //将原始的msg填充参数形成新msg
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
