package com.zengbobobase.demo.activity;

import com.zengbobo.android.utils.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;

@SuppressLint("JavascriptInterface")
public class WebViewJsLvmmActivity extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setContentView(webView, params);
		webView.getSettings().setJavaScriptEnabled(true);

//		webView.addJavascriptInterface(new MyJavascriptInterface(), "mytestjs");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			LvmmLogin://[{"login_third":"false","onsuccess":"success","onfail":"fail"}]
				System.out.println("WebViewJsLvmmActivity shouldOverrideUrlLoading url:" + url);
				if(url.contains("LvmmLogin://")){
//					Intent intent = new Intent(WebViewJsLvmmActivity.this, ResultActivity.class);
//					startActivityForResult(intent, 0);
					Intent intent = new Intent();
					intent.setData(Uri.parse(url));
					startActivityForResult(intent, 0);
				}else{
					view.loadUrl(url);
//					Intent intent_1 = new Intent();
					
				}

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out
						.println("WebViewJsLvmmActivity onPageStarted url:" + url);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("WebViewJsLvmmActivity onPageFinished url:"
						+ url);
			
			}

		});
//		webView.loadUrl("http://m.lvmama.com/static/demo/loginClient.html");
		webView.loadUrl("file:///android_asset/webview_js_lvmm.html");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("WebViewJsLvmmActivity resultCode:"+resultCode);
		if(resultCode==1){
			webView.loadUrl("javascript:success('http://m.lvmama.com/ticket/piao-100797/')");
		}else if(resultCode==2){
			webView.loadUrl("javascript:fail('http://m.lvmama.com')");	
		}
	}

}
