package com.zengbobobase.demo.sinweibo.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.zengbobobase.demo.sinweibo.Constant;

public class SinaBusinessFactory {
	// 请求用户授权Token
	public static void authorize(ResponseHandler responseHandler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", Constant.APP_KEY);
		map.put("display", "mobile");
		map.put("redirect_uri", Constant.REDIRECT_URL);
		SinaHttpUtil.getInstance().get(SinaApi.ANTHORIZE_URL, map,
				responseHandler);
	}

	// 获取授权过的Access Token
	public static void access_token(String code, ResponseHandler responseHandler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", Constant.APP_KEY);
		map.put("client_secret", Constant.APP_SECRET);
		map.put("grant_type", "authorization_code");
		map.put("redirect_uri", Constant.REDIRECT_URL);
		map.put("code", code);
		SinaHttpUtil.getInstance().post(SinaApi.ACCESS_TOKEN_URL, map,
				responseHandler);
	}

	// 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
	public static void get_token_info(String access_token,
			ResponseHandler responseHandler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", access_token);
		SinaHttpUtil.getInstance().post(SinaApi.GET_TOKEN_INFO_URL, map,
				responseHandler);
	}
	
//	发布一条新微博
	public static void update(String access_token,String status,
			ResponseHandler responseHandler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", Constant.APP_KEY);
		map.put("access_token", access_token);
		map.put("status", URLEncoder.encode(status) );
		map.put("visible", "0");
		map.put("long", "121.509806");
		map.put("lat", "25.069856");
//		SinaHttpUtil.getInstance().post(SinaApi.UPDATE, map,
//				responseHandler);

		SinaHttpUtil.toUploadFile(null, "pic", SinaApi.UPDATE, map, responseHandler);
	}
//	上传图片并发布一条新微博
	public static void upload(String access_token,String status,String path,
			ResponseHandler responseHandler) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", Constant.APP_KEY);
		map.put("access_token", access_token);
		map.put("status", URLEncoder.encode(status) );
//		map.put("pic", path);
		map.put("visible", "0");
		map.put("long", "120.911306");
		map.put("lat", "23.855649");
//		SinaHttpUtil.getInstance().post(SinaApi.UPLOAD, map,
//				responseHandler);
		SinaHttpUtil.toUploadFile(new File(path), "pic", SinaApi.UPLOAD, map, responseHandler);
	}
//	上传图片并发布一条新微博
	public static void upload_url_text(String access_token,String status,String url,
			ResponseHandler responseHandler) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", Constant.APP_KEY);
		map.put("access_token", access_token);
		map.put("status", URLEncoder.encode(status) );
		map.put("url", url);
//		map.put("pic_id", "http://ww4.sinaimg.cn/bmiddle/8086e234jw1ees3t462kvj20dz0ku7b3.jpg");
		map.put("visible", "0");
		map.put("long", "120.911306");
		map.put("lat", "23.855649");
//		SinaHttpUtil.getInstance().post(SinaApi.UPLOAD, map,
//				responseHandler);
		SinaHttpUtil.toUploadFile(null, "pic", SinaApi.UPLOAD_URL_TEXT, map, responseHandler);
	}
}
