package com.zengbobobase.demo.activity;

import com.zengbobo.android.utils.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebViewClient;

@SuppressLint("JavascriptInterface")
public class WebViewJsActivity extends Activity {
	private WebView webView;
	private String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		path = "file:///android_asset/webview_js.html";
		webView = new WebView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setContentView(webView, params);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);

		webView.addJavascriptInterface(new MyJavascriptInterface(), "mytestjs");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out
						.println("WebViewJsActivity onPageStarted url:" + url);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("WebViewJsActivity onPageFinished url:"
						+ url);
				System.out.println("WebViewJsActivity onPageFinished path:"
						+ path);
				System.out
						.println("WebViewJsActivity onPageFinished url.equals(path):"
								+ (url.equals(path)));
				if (!StringUtil.equalsNullOrEmpty(path) && url.equals(path)) {
					// 要注入的js
					System.out
							.println("WebViewJsActivity onPageFinished ......:");
					String js = "document.forms[0].onsubmit=function(event){var pwd = document.forms[0].getElementById(\"ptlogin-password\").value;window.mytestjs.getPassword(pwd);return false;}";

//					view.loadUrl("javascript:" + js);
				}
			}

		});
		webView.loadUrl(path);
	}

	public class MyJavascriptInterface {

		@JavascriptInterface
		public void getPassword() {
			System.out.println("WebViewJsActivity getPassword ");
			webView.loadUrl("javascript:getList()");
		}

		@JavascriptInterface
		public void getPassword(String password) {
			System.out.println("WebViewJsActivity getPassword(String  password):"+ password);
		}
	}

}
