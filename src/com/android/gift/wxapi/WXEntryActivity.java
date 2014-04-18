package com.android.gift.wxapi;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.os.Bundle;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	

	// 微信发送的请求将回调到onReq方法;
	@Override
	public void onReq(BaseReq arg0) {

	}

	// 发送到微信请求的响应结果将回调到onResp方法;
	@Override
	public void onResp(BaseResp arg0) {
//		
	}

}
