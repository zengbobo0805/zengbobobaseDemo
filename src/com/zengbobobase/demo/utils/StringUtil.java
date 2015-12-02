package com.zengbobobase.demo.utils;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/9/8 10:46
 * 修改人：bobo
 * 修改时间：2015/9/8 10:46
 * 修改备注：
 */
public class StringUtil {
    /**
     * 判断是否为null或空�?
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.toLowerCase().equals("null");
    }
}
