package com.zengbobobase.demo.activity;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.sinweibo.my.Constant;

public class SinaSDKActivity extends Activity implements IWeiboHandler.Response {
	private Button btn1, btn2, btn3;
	private TextView tv1;
	private StringBuffer buf = new StringBuffer();
	private WeiboAuth weiboAuth;
	private SsoHandler mSsoHandler;
	private IWeiboShareAPI mWeiboShareAPI;

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
		weiboAuth = new WeiboAuth(this, Constant.APP_KEY,
				Constant.REDIRECT_URL, Constant.SCOPE);
		mSsoHandler = new SsoHandler(SinaSDKActivity.this, weiboAuth);

		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.APP_KEY);
		mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端
	}

	private OnClickListener btn1Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mSsoHandler.authorize(new AuthListener());
		}
	};

	private OnClickListener btn2Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			weiboAuth.authorize(new AuthListener(), 0);
		}
	};

	private OnClickListener btn3Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendMultiMessage(true);
		}
	};

	class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle bundle) {
			if (bundle != null) {
				Oauth2AccessToken accessToken = new Oauth2AccessToken();
				accessToken.setUid(bundle.getString("uid"));
				accessToken.setToken(bundle.getString("access_token"));
				accessToken.setExpiresIn(bundle.getString("expires_in"));
				accessToken.setRefreshToken(bundle.getString("refresh_token"));// 新
																				// remind_in
				buf.append("*******************onComplete  1***********************\n");
				buf.append("onComplete uid:" + bundle.getString("uid") + "\n");
				buf.append("onComplete access_token:"
						+ bundle.getString("access_token") + "\n");
				buf.append("onComplete expires_in:"
						+ bundle.getString("expires_in") + "\n");
				buf.append("onComplete refresh_token:"
						+ bundle.getString("refresh_token") + "\n");
				buf.append("onComplete code:" + bundle.getString("code") + "\n");
				Set<String> set_key = bundle.keySet();
				Iterator<String> it = set_key.iterator();
				buf.append("*******************onComplete  2***********************\n");
				buf.append("Bundle bundle size:" + set_key.size() + "\n");
				while (it.hasNext()) {
					String key = it.next();
					buf.append("onComplete " + key + ":"
							+ bundle.getString(key) + "\n");
				}
				tv1.setText(buf.toString());

			}

		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			WeiboAuthException ex = (WeiboAuthException) arg0;
			buf.append("*******************onWeiboException***********************\n");
			buf.append("onWeiboException ErrorType:" + ex.getErrorType() + "\n");
			buf.append("onWeiboException ErrorCode:" + ex.getErrorCode() + "\n");
			buf.append("onWeiboException message:" + ex.getMessage() + "\n");
			tv1.setText(buf.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult ");
		if (mSsoHandler != null && data != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			buf.append("*******************onActivityResult  ***********************\n");
			String error = data.getStringExtra("error");
			String error_type = data.getStringExtra("error_type");

			buf.append("onActivityResult error:" + error + "\n");
			buf.append("onActivityResult error_type:" + error_type + "\n");
			Bundle bundle = data.getExtras();
			if (data != null) {
				Set<String> set_key = bundle.keySet();
				Iterator<String> it = set_key.iterator();
				buf.append("onActivityResult bundle size:" + set_key.size()
						+ "\n");
				while (it.hasNext()) {
					String key = it.next();
					buf.append("onActivityResult " + key + ":"
							+ bundle.getString(key) + "\n");
				}
			}

			tv1.setText(buf.toString());
		}
		
	}

	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = "hello xinlang weibo";
		return textObject;
	}

	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		imageObject.title="title";
		imageObject.description="description";
		
		imageObject.imagePath ="http://a.36krcnd.com/photo/e9c23310fcb4883f2ffbd8a3abb49dcd.png";
		return imageObject;
	}

	private void sendMultiMessage(boolean hasText) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// 初始化微博的分享消息
		if (hasText) {
//			weiboMessage.textObject = getTextObj();
			weiboMessage.imageObject=getImageObj();
		}
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(request); // 发送请求消息到微博，唤起微博分享界面
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("onNewIntent ");
		mWeiboShareAPI.handleWeiboResponse(intent, this); // 当前应用唤起微博分享后，返回当前应用
	}

	@Override
	public void onResponse(BaseResponse baseResp) {// 接收微客户端博请求的数据。
		System.out.println("onResponse errCode:" + baseResp.errCode);
		System.out.println("onResponse errMsg:" + baseResp.errMsg);
		System.out.println("onResponse reqPackageName:"
				+ baseResp.reqPackageName);
		System.out.println("onResponse transaction:" + baseResp.transaction);
		System.out.println("onResponse getType:" + baseResp.getType());
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			break;
		}
	}

}
