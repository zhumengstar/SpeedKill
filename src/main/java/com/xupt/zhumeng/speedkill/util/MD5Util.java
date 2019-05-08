package com.xupt.zhumeng.speedkill.util;


//MD5工具

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密用户账户密码
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //把用户输入的密码转换成form表单应该提交的密码---MDd5(输入密码+固定salt)
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    //把form表单应该提交的密码转换成数据库里的密码---MD5(表单密码+随机salt)
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    //双重加密
    public static String inputPassToDBPass(String inputPass, String saltDB) {
        //表单密码
        String formPass = inputPassToFormPass(inputPass);
        //数据库密码
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

}
