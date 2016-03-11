package com.zengbobobase.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zengbobo.android.utils.StringUtil;

@SuppressLint("JavascriptInterface")
public class WebViewJsActivity extends FragmentActivity {
	private WebView webView;
	private String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		path = "file:///android_asset/webview_js.html";
		path="http://minfo.gildata.com/mobileF10View/f10.html?s=002711&p=HSJY_1008&u=54f955e8-9f0a-4468-a8c3-cbd1c69a5b75&t=red&v=1.0&n=1450749986759";
//		path="http://mp.weixin.qq.com/s?__biz=MzA5ODE4NjU2Mw==&mid=233438399&idx=1&sn=fdd1fcc2b2a6c0aebf5fbf40975901c6#rd";

//		path="xrzgp://www.icaikee.com?data={\"id\":\"1\"}";
		webView = new WebView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setContentView(webView, params);
//		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setUseWideViewPort(true);
//		webView.getSettings().setSupportZoom(true);
//		webView.getSettings().setPluginState(PluginState.ON);
//		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
//
//		webView.addJavascriptInterface(new MyJavascriptInterface(), "mytestjs");

		
		
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBlockNetworkLoads(false);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setDatabaseEnabled(true); 
		webView.getSettings().setDomStorageEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				Log.i("zengbobo", "WebViewJsActivity shouldOverrideUrlLoading url:" + url);
				if(StringUtil.equalsNullOrEmpty(url)||url.toLowerCase().startsWith("android")){
					return true;
				}
				if(url.startsWith("xrzgp")){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri =Uri.parse(url);
					intent.setData(uri);
					startActivity(intent);
				}else{
					view.loadUrl(url);
				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				System.out
						.println("WebViewJsActivity onPageStarted url:" + url);
				if(StringUtil.equalsNullOrEmpty(url)||url.toLowerCase().startsWith("android")){
					return ;
				}
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				System.out.println("WebViewJsActivity onPageFinished url:"
					+ url);
				System.out.println("WebViewJsActivity onPageFinished path:"
						+ path);
				System.out
						.println("WebViewJsActivity onPageFinished url.equals(path):"
								+ (url.equals(path)));
				if(StringUtil.equalsNullOrEmpty(url)||url.toLowerCase().startsWith("android")){
					return ;
				}
				super.onPageFinished(view, url);

//				if (!StringUtil.equalsNullOrEmpty(path) && url.equals(path)) {
//					// 要注入的js
//					System.out
//							.println("WebViewJsActivity onPageFinished ......:");
//					String js = "document.forms[0].onsubmit=function(event){var pwd = document.forms[0].getElementById(\"ptlogin-password\").value;window.mytestjs.getPassword(pwd);return false;}";
//
////					view.loadUrl("javascript:" + js);
//				}
			}

		});
		webView.loadUrl(path);
	}

//	public class MyJavascriptInterface {
//
//		@JavascriptInterface
//		public void getPassword() {
//			System.out.println("WebViewJsActivity getPassword ");
//			webView.loadUrl("javascript:getList()");
//		}
//
//		@JavascriptInterface
//		public void getPassword(String password) {
//			System.out.println("WebViewJsActivity getPassword(String  password):"+ password);
//		}
//	}

}
