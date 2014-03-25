package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.zengbobo.android.utils.JsonUtil;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.sinweibo.Constant;
import com.zengbobobase.demo.sinweibo.my.AccessTokenInfo;
import com.zengbobobase.demo.sinweibo.my.AccessTokenUtil;
import com.zengbobobase.demo.sinweibo.my.ResponseHandler;
import com.zengbobobase.demo.sinweibo.my.SinaBusinessFactory;

public class SinaActivity extends Activity {
	private WebView webView;
	private Button btn1, btn2;
	private TextView tv1;
	private StringBuffer buf = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_layout);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		tv1 = (TextView) findViewById(R.id.tv1);
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		initWebView();
		btn1.setOnClickListener(btn1Listener);
		btn2.setOnClickListener(btn2Listener);

	}

	private OnClickListener btn1Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					AccessTokenInfo info = AccessTokenUtil
							.read(SinaActivity.this);
					ResponseHandler responseHandler = new ResponseHandler() {

						@Override
						public void onSuccess(String content) {
							buf.append("********get_token_info  "
									+ System.currentTimeMillis() / 1000
									+ "   **********\n");
							buf.append("get_token_info onSuccess:" + content
									+ "\n");
							AccessTokenInfo info = JsonUtil.parseJson(content,
									AccessTokenInfo.class);
							AccessTokenInfo info_1 = AccessTokenUtil
									.read(SinaActivity.this);
							info_1.setCreate_at(info.getCreate_at());
							AccessTokenUtil.save(SinaActivity.this, info_1);
							Message msg = handler.obtainMessage(0x0001,
									buf.toString());
							msg.sendToTarget();
						}

						@Override
						public void onError(String content) {
							buf.append("********get_token_info  "
									+ System.currentTimeMillis() / 1000
									+ "   **********\n");
							buf.append("get_token_info onError:" + content
									+ "\n");
							Message msg = handler.obtainMessage(0x0001,
									buf.toString());
							msg.sendToTarget();
						}
					};
					if (AccessTokenUtil.isInvalidAccessToken(info)) {
						responseHandler.onError("access_token无效！");
						return;
					}
					SinaBusinessFactory.get_token_info(info.getAccess_token(),
							responseHandler);

				}
			}).start();
		}
	};
	private OnClickListener btn2Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			upload();
			// update();
			upload_url_text();
		}
	};
	private void upload_url_text() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						AccessTokenInfo info = AccessTokenUtil
								.read(SinaActivity.this);
						ResponseHandler responseHandler = new ResponseHandler() {

							@Override
							public void onSuccess(String content) {
								buf.append("********upload  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("upload onSuccess:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}

							@Override
							public void onError(String content) {
								buf.append("********upload  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("upload onError:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}
						};
						if (AccessTokenUtil.isInvalidAccessToken(info)) {
							responseHandler.onError("access_token无效！");
							return;
						}
						Bitmap bm = BitmapFactory.decodeResource(
								SinaActivity.this.getResources(),
								R.drawable.ic_launcher);
						SinaBusinessFactory.upload_url_text(info.getAccess_token(),
								"我在马航MH370这！", "http://a.36krcnd.com/photo/e9c23310fcb4883f2ffbd8a3abb49dcd.png", responseHandler);

					}
				}).start();

			}
		}).start();
	}

	private void upload() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						AccessTokenInfo info = AccessTokenUtil
								.read(SinaActivity.this);
						ResponseHandler responseHandler = new ResponseHandler() {

							@Override
							public void onSuccess(String content) {
								buf.append("********upload  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("upload onSuccess:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}

							@Override
							public void onError(String content) {
								buf.append("********upload  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("upload onError:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}
						};
						if (AccessTokenUtil.isInvalidAccessToken(info)) {
							responseHandler.onError("access_token无效！");
							return;
						}
						Bitmap bm = BitmapFactory.decodeResource(
								SinaActivity.this.getResources(),
								R.drawable.ic_launcher);
						SinaBusinessFactory.upload(info.getAccess_token(),
								"我在马航MH370这！", Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/ping.png", responseHandler);

					}
				}).start();

			}
		}).start();
	}

	private void update() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						AccessTokenInfo info = AccessTokenUtil
								.read(SinaActivity.this);
						ResponseHandler responseHandler = new ResponseHandler() {

							@Override
							public void onSuccess(String content) {
								buf.append("********update  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("update onSuccess:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}

							@Override
							public void onError(String content) {
								buf.append("********update  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append("update onError:" + content + "\n");
								Message msg = handler.obtainMessage(0x0001,
										buf.toString());
								msg.sendToTarget();
							}
						};
						if (AccessTokenUtil.isInvalidAccessToken(info)) {
							responseHandler.onError("access_token无效！");
							return;
						}
						SinaBusinessFactory.update(info.getAccess_token(),
								"我在马航MH370这！", responseHandler);

					}
				}).start();

			}
		}).start();
	}

	private void initWebView() {
		if (AccessTokenUtil.isInvalidAccessToken(SinaActivity.this)) {
			webView.loadUrl("https://api.weibo.com/oauth2/authorize?client_id="
					+ Constant.APP_KEY + "&display=mobile&redirect_uri="
					+ Constant.REDIRECT_URL);
			webView.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view,
						final String url) {

					System.out.println("SinaActivity url:" + url);
					if (url.contains("http://www.lvmama.com/?code=")) {
						final String code = url.substring(url.indexOf("code=") + 5);
						new Thread(new Runnable() {

							@Override
							public void run() {
								buf.append("********authorize  "
										+ System.currentTimeMillis() / 1000
										+ "   **********\n");
								buf.append(" authorize url:" + url + "\n");
								SinaBusinessFactory.access_token(code,
										new ResponseHandler() {

											@Override
											public void onSuccess(String content) {
												buf.append("********access_token  "
														+ System.currentTimeMillis()
														/ 1000
														+ "   **********\n");
												buf.append("access_token onSuccess:"
														+ content + "\n");
												AccessTokenInfo info = JsonUtil
														.parseJson(
																content,
																AccessTokenInfo.class);
												info.setCreate_at(System
														.currentTimeMillis() / 1000);
												AccessTokenUtil
														.save(SinaActivity.this,
																info);
												Message msg = handler
														.obtainMessage(0x0001,
																buf.toString());
												msg.sendToTarget();
											}

											@Override
											public void onError(String content) {
												buf.append("********access_token  "
														+ System.currentTimeMillis()
														/ 1000
														+ "   **********\n");
												buf.append("access_token onError:"
														+ content + "\n");
												Message msg = handler
														.obtainMessage(0x0001,
																buf.toString());
												msg.sendToTarget();
											}
										});
							}

						}).start();

					} else {
						view.loadUrl(url);
					}
					return true;
				}

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
				}

			});
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x0001) {
				tv1.setText((String) msg.obj);
			}
		}

	};
}
