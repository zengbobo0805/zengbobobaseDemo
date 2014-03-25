package com.zengbobobase.demo.sinweibo.my;

public class SinaApi {
	public static String HOST_URL = "https://api.weibo.com/";
	public static String HOST_OAUTH2_URL = HOST_URL+"oauth2/";

	public static String ANTHORIZE_URL = "https://open.weibo.cn/oauth2/authorize";// 请求用户授权Token
	public static String ACCESS_TOKEN_URL = HOST_OAUTH2_URL + "access_token?";// 获取授权过的Access
																				// Token
	public static String GET_TOKEN_INFO_URL = HOST_OAUTH2_URL
			+ "get_token_info";// 授权信息查询接口
	public static String REVOKEOAUTH2_URL = HOST_OAUTH2_URL + "revokeoauth2";// 授权回收接口
	public static String GET_OAUTH2_TOKEN_URL = HOST_OAUTH2_URL
			+ "get_oauth2_token";// OAuth1.0的Access Token更换至OAuth2.0的Access
									// Token
	public static String UPDATE = HOST_URL+"2/statuses/update.json";//发布一条新微博
	public static String UPLOAD = "https://upload.api.weibo.com/2/statuses/upload.json";//上传图片并发布一条新微博
	public static String UPLOAD_URL_TEXT = "https://api.weibo.com/2/statuses/upload_url_text.json";//指定一个图片URL地址抓取后上传并同时发布一条新微博


	
}
