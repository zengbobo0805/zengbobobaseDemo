package com.zengbobobase.demo.tencent.my;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils.HttpStatusException;
import com.tencent.utils.HttpUtils.NetworkUnavailableException;
import com.zengbobo.android.utils.JsonUtil;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.sinweibo.my.TencentToken;

public class TencentActivity extends Activity {
	private Tencent mTencent;
	private Button btn1, btn2, btn3;
	private TextView tv1;
	private StringBuffer buf = new StringBuffer();
	private String openId = "", accessToken = "";
	private long expiresIn = 0L;
	private TencentToken tencentToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tencent_layout);
		mTencent = Tencent.createInstance(Constant.TENCENT_APP_ID,
				TencentActivity.this.getApplicationContext());
		tv1 = (TextView) findViewById(R.id.tv1);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn1.setOnClickListener(btn1ClickListener);
		btn2.setOnClickListener(btn2ClickListener);
		btn3.setOnClickListener(btn3ClickListener);
	}

	private OnClickListener btn1ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			login();
		}
	};

	private void login() {
		mTencent.setOpenId(openId);
		mTencent.setAccessToken(accessToken, expiresIn + "");
		mTencent.login(TencentActivity.this, Constant.SCOPE, new IUiListener() {
			@Override
			public void onComplete(Object arg0) {
				buf.append("***************login onComplete *************************\n");
				buf.append("arg0:" + arg0);
				tencentToken = JsonUtil.parseJson(arg0.toString(),
						TencentToken.class);
				openId = tencentToken.getOpenid();
				expiresIn = tencentToken.getExpires_in();
				accessToken = tencentToken.getAccess_token();
				tv1.setText(buf.toString());

			}

			@Override
			public void onError(UiError arg0) {
				buf.append("***************login onError *************************\n");
				buf.append("errorCode:" + arg0.errorCode + "\n");
				buf.append("errorDetail:" + arg0.errorDetail + "\n");
				buf.append("errorMessage:" + arg0.errorMessage + "\n");
				tv1.setText(buf.toString());
			}

			@Override
			public void onCancel() {
				buf.append("***************login onCancel *************************\n");

				tv1.setText(buf.toString());

			}
		});
	}

	private OnClickListener btn2ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			logout();
			UserInfo info = new UserInfo(TencentActivity.this, mTencent.getQQToken());
			info.getUserInfo(new IUiListener() {
				
				@Override
				public void onError(UiError arg0) {
					buf.append("***************getUserInfo onError *************************\n");
					buf.append("errorCode:" + arg0.errorCode + "\n");
					buf.append("errorDetail:" + arg0.errorDetail + "\n");
					buf.append("errorMessage:" + arg0.errorMessage + "\n");
					tv1.setText(buf.toString());
				}
				
				@Override
				public void onComplete(Object arg0) {
					buf.append("***************getUserInfo onComplete *************************\n");
					buf.append("arg0:" + arg0.toString()+ "\n");
					tv1.setText(buf.toString());
					
				}
				
				@Override
				public void onCancel() {
					buf.append("***************getUserInfo onCancel *************************\n");
					tv1.setText(buf.toString());
					
				}
			});
		}
	};

	private void logout() {
		mTencent.logout(this);
	}

	private OnClickListener btn3ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			shareApp();
		}
	};

	private void shareApp() {
		if (mTencent.isSessionValid() && mTencent.getOpenId() != null) {
			Bundle params = new Bundle();
			params.putString(SocialConstants.PARAM_TITLE,
					"AndroidSdk_1_3:UiStory title");
			params.putString(SocialConstants.PARAM_COMMENT,
					"AndroidSdk_1_3: UiStory comment");
			params.putString(SocialConstants.PARAM_IMAGE,
					"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
			params.putString(SocialConstants.PARAM_SUMMARY,
					"AndroidSdk_1_3: UiStory summary");
//			params.putString(SocialConstants.PARAM_SOURCE,
//					"zengbobo");
//			params.putString(SocialConstants.PARAM_PLAY_URL,
//					"http://player.youku.com/player.php/Type/Folder/"
//							+ "Fid/15442464/Ob/1/Pt/0/sid/XMzA0NDM2NTUy/v.swf");

			mTencent.story(TencentActivity.this, params, new IUiListener() {

				@Override
				public void onComplete(Object arg0) {
					buf.append("***************story onComplete *************************\n");
					buf.append("arg0:" + arg0);
				
					tv1.setText(buf.toString());
				}


				@Override
				public void onError(UiError arg0) {
					buf.append("***************story onError *************************\n");
					buf.append("errorCode:" + arg0.errorCode + "\n");
					buf.append("errorDetail:" + arg0.errorDetail + "\n");
					buf.append("errorMessage:" + arg0.errorMessage + "\n");
					tv1.setText(buf.toString());
				}

				@Override
				public void onCancel() {
					buf.append("***************story onCancel *************************\n");

					tv1.setText(buf.toString());

				}
			});
		}
	}
	
	
//	private void onClickUserInfo() {
//        mTencent.requestAsync(SocialConstants.GRAPH_USER_INFO, null,
//                    SocialConstants.HTTP_GET, new BaseApiListener("get_user_info", false), null);
//}

//	private void shareImgToQQ(){
//		final Bundle params = new Bundle();
//		params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
//		params.putString(Tencent.SHARE_TO_QQ_TITLE, "要分享的标题");
//		params.putString(Tencent.SHARE_TO_QQ_SUMMARY,  "要分享的摘要");
//		    params.putString(Tencent.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
//		    params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//		    params.putString(Tencent.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//		    params.putInt(Tencent.SHARE_TO_QQ_EXT_INT,  "其他附加功能");		mTencent.shareToQQ(TencentActivity.this, params, new BaseUiListener());
//
//	}

	IUiListener myIUiListener = new IUiListener() {

		@Override
		public void onComplete(Object arg0) {
			buf.append("*************** onComplete *************************\n");

			tv1.setText(buf.toString());

		}

		@Override
		public void onError(UiError arg0) {
			buf.append("*************** onError *************************\n");
			buf.append("errorCode:" + arg0.errorCode + "\n");
			buf.append("errorDetail:" + arg0.errorDetail + "\n");
			buf.append("errorMessage:" + arg0.errorMessage + "\n");
			tv1.setText(buf.toString());
		}

		@Override
		public void onCancel() {
			buf.append("*************** onCancel *************************\n");

			tv1.setText(buf.toString());

		}
	};

	IRequestListener myIRequestListener = new IRequestListener() {

		@Override
		public void onComplete(JSONObject arg0) {

		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0) {
			// http请求返回码非200时触发此异常
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0) {
			// 当前网络不可用时触发此异常
		}

		@Override
		public void onUnknowException(Exception arg0) {
			// 出现未知错误时会触发此异常
		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0) {

		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0) {

		}

		@Override
		public void onJSONException(JSONException arg0) {

		}

		@Override
		public void onIOException(IOException arg0) {

		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0) {

		}

	};
}
