package com.zengbobobase.demo.utils;

import android.util.Base64;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/9/8 10:36
 * 修改人：bobo
 * 修改时间：2015/9/8 10:36
 * 修改备注：
 */
public class Base64Utils {

    // 这里 encodeToString 则直接将返回String类型的加密数据
    public static String encodeToString(String str){
        if(StringUtil.isNullOrEmpty(str)){
            return "";
        }

        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }


    // 对base64加密后的数据进行解密
    public static String decodeToStrig(String strBase64){
        if(StringUtil.isNullOrEmpty(strBase64)){
            return "";
        }
        return  new String(Base64.decode(strBase64.getBytes(), Base64.NO_WRAP));
    }

}
