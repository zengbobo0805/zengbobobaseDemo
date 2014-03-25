package com.zengbobobase.demo.sinweibo.my;

public interface ResponseHandler {
	public void onSuccess(String content);
	public void onError(String content);
}
