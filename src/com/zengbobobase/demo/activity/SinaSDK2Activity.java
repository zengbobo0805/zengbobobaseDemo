package com.zengbobobase.demo.activity;
/*
 * 需要合作伙伴；
 * 
 * <activity
android:name="com.zengbobobase.demo.activity.SinaSDK2Activity"
android:configChanges="keyboardHidden|orientation"
android:label="调用新浪微博SDK,从 微博客户端唤起第三方 应用 "
android:screenOrientation="portrait" >
<intent-filter>
    <action android:name="com.sina.weibo.sdk.action.ACTION_SDK_RESP_ACTIVITY" />

    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>
</activity>


*/
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.ProvideMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.sinweibo.my.Constant;

public class SinaSDK2Activity extends Activity implements IWeiboHandler.Request {
	private Button btn1, btn2, btn3;
	private TextView tv1;
	private StringBuffer buf = new StringBuffer();
	private IWeiboShareAPI mShareWeiboAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_layout);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		tv1 = (TextView) findViewById(R.id.tv1);
		btn1.setText("sso");
		btn2.setText("web");
		btn3.setText("分享");
		btn1.setOnClickListener(btn1Listener);
		btn2.setOnClickListener(btn2Listener);
		btn3.setOnClickListener(btn3Listener);

		mShareWeiboAPI = WeiboShareSDK.createWeiboAPI(this, Constant.APP_KEY); // 创建分享实例
		mShareWeiboAPI.handleWeiboRequest(getIntent(), this); // 处理微博客户端发送过来的请求
//		android.os.IInterface;
//		Binder
//		sendBroadcast(intent, receiverPermission)
	}

	private OnClickListener btn1Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	private OnClickListener btn2Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	private OnClickListener btn3Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			responseSingleMessage(true);
		}
	};

	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = "hello xinlang weibo";
		return textObject;
	}

	private void responseSingleMessage(boolean hasText) {
		WeiboMessage weiboMessage = new WeiboMessage();
		// 1. 初始化微博的分享消息
		if (hasText) {
			weiboMessage.mediaObject = getTextObj();
		}
		// 2. 初始化从微博到第三方的消息请求
		ProvideMessageForWeiboResponse response = new ProvideMessageForWeiboResponse();
		response.transaction = mBaseRequest.transaction;
		response.reqPackageName = mBaseRequest.packageName;
		response.message = weiboMessage;
		// 3. 发送响应消息到微博
		mShareWeiboAPI.sendResponse(response);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult ");

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("onNewIntent ");
		mShareWeiboAPI.handleWeiboRequest(intent, this); // 处理微博客户端发送过来的请求
	}

	BaseRequest mBaseRequest;

	@Override
	public void onRequest(BaseRequest baseRequest) {
		mBaseRequest = baseRequest;
	}

}
