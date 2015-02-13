package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.TWWebChromeClient;
import com.zengbobobase.demo.TWWebViewClient;

public class WebViewJsRouDongActivity extends Activity  {

	private WebView webView;
	public List list;
	private Button btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);// 开启javascript设置
		// webView.addJavascriptInterface(new TestJs(), "javatojs");//
		// 把RIAExample的一个实例添加到js的全局对象window中，
		// //这样就可以使用window.javatojs来调用它的方法

		webView.setWebViewClient(new TWWebViewClient());
		
		webView.setWebChromeClient(new TWWebChromeClient());
		
		webView.loadUrl("file:///android_asset/js_roudong_demo.html");// 加载网页
		// webView.loadUrl("file:///android_asset/tw_cheditor_api/api.html");//
		// 加载网页

	}

	private String jsStr = "javascript:(function JsAddJavascriptInterface_(){if (typeof(window.jsInterface)!='undefined') {console.log('window.jsInterface_js_interface_name is exist!!');}else {console.log('window.jsInterface_js_interface_name is no exist!!');window.jsInterface = { onButtonClick:function(arg0) {return prompt('MyApp:'+JSON.stringify({obj:'jsInterface',func:'onButtonClick',args:[arg0]}));},onImageClick:function(arg0,arg1,arg2) {prompt('MyApp:'+JSON.stringify({obj:'jsInterface',func:'onImageClick',args:[arg0,arg1,arg2]}));},};}})";


}