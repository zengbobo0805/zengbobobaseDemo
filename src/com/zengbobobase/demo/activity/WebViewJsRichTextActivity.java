package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.zengbobobase.demo.R;

public class WebViewJsRichTextActivity extends Activity implements OnClickListener {

	private WebView webView;
	public List list;
	private Button btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_webview);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);// 开启javascript设置
		webView.addJavascriptInterface(new TestJs(), "javatojs");// 把RIAExample的一个实例添加到js的全局对象window中，
		// //这样就可以使用window.javatojs来调用它的方法

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("WebViewJsNewActivity onPageFinished url:"
						+ url);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						webView.loadUrl("javascript:getList(10)");
					}
				});

			}

		});
		webView.loadUrl("file:///android_asset/js_roudong_demo.html");// 加载网页
//		webView.loadUrl("file:///android_asset/tw_cheditor_api/api.html");// 加载网页
		
		

	}

	void initData() {
		list = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			list.add("我是从数据库中读取的哈哈");
		}
	}

	/**
	 * 该方法将在js脚本中，通过window.javatojs.....()进行调用
	 * 
	 * @return
	 */
	Handler mHandler = new Handler();

	public class TestJs {
		@JavascriptInterface
		public Object getObject(int index) {
			System.out.println("WebViewJsNewActivity Object(int index) :"
					+ index);
			return list.get(index);
		}
		@JavascriptInterface
		public int getSize() {
			System.out.println("WebViewJsNewActivity getSize():" + list.size());
			return list.size();
		}
		@JavascriptInterface
		public void Callfunction(String buf) {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
					System.out.println("WebViewJsNewActivity Callfunction buf:"+buf);
					webView.loadUrl("javascript:getList(10)");
//				}
//			});
		}
		@JavascriptInterface
		public void Callfunction() {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
					System.out.println("WebViewJsNewActivity Callfunction");
					webView.loadUrl("javascript:window.getList(10)");
//				}
//			});
		}
		
		@JavascriptInterface
		public String onButtonClick(String text) {
			System.out.println("WebViewJsNewActivity onButtonClick");
			return text;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		webView.loadUrl("javascript:setContent(true)");
	}
}