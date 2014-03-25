package com.zengbobobase.demo.sinweibo.my;

import android.content.SharedPreferences;

public class AccessTokenInfo {
	private String access_token;
	private long remind_in;
	private long expires_in;
	private String uid;
	private long create_at;// 1352267591,
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public long getRemind_in() {
		return remind_in;
	}
	public void setRemind_in(long remind_in) {
		this.remind_in = remind_in;
	}
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public long getCreate_at() {
		return create_at;
	}
	public void setCreate_at(long create_at) {
		this.create_at = create_at;
	}
	
	
}
