package com.zengbobobase.demo.sinweibo.my;

import com.zengbobo.android.utils.JsonUtil;
import com.zengbobo.android.utils.StringUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AccessTokenUtil {
	private static String ACCESSTOKENNAME = "accessTokenFile";

	public static void save(Context mContext, AccessTokenInfo info) {
		SharedPreferences sp = mContext.getSharedPreferences(ACCESSTOKENNAME,
				Context.MODE_APPEND);
		Editor ed = sp.edit();
		String content = JsonUtil.toJson(info);
		ed.putString("accessTokenFile", content);
		ed.commit();
	}

	public static AccessTokenInfo read(Context mContext) {
		SharedPreferences sp = mContext.getSharedPreferences(ACCESSTOKENNAME,
				Context.MODE_APPEND);
		String content = sp.getString("accessTokenFile", "");
		if (StringUtil.equalsNullOrEmpty(content)) {
			return null;
		}
		return JsonUtil.parseJson(content, AccessTokenInfo.class);
	}
	public static boolean isInvalidAccessToken(Context mContext) {
		AccessTokenInfo info =read(mContext);
		if (info == null
				|| StringUtil.equalsNullOrEmpty(info.getAccess_token())
				|| info.getCreate_at() <= 0
				|| (info.getCreate_at() > 0 && System.currentTimeMillis()/1000
						- info.getCreate_at() > info.getExpires_in())) {
			return true;
		}

		return false;
	}
	
	public static boolean isInvalidAccessToken(AccessTokenInfo info) {
		System.out.println((info == null)+"  "+(StringUtil.equalsNullOrEmpty(info.getAccess_token())));
		System.out.println((info.getCreate_at() <= 0)+"  "+(info.getCreate_at() > 0 && System.currentTimeMillis()/1000
				- info.getCreate_at() > info.getExpires_in()));
		if (info == null
				|| StringUtil.equalsNullOrEmpty(info.getAccess_token())
				|| info.getCreate_at() <= 0
				|| (info.getCreate_at() > 0 && System.currentTimeMillis()/1000
						- info.getCreate_at() > info.getExpires_in())) {
			return true;
		}

		return false;
	}
}
