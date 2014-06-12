package com.zengbobobase.demo.activity;

import com.zengbobobase.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class ReLoadWebVIEWActivity extends Activity {
	private Button btn;
	private WebView webView;
//	private boolean flag =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ReLoadWebVIEWActivity onCreate");
		setContentView(R.layout.activity_reload_layout);
		btn = (Button) findViewById(R.id.btn);
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("http://www.baidu.com");
		
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				System.out.println("ReLoadWebVIEWActivity shouldOverrideUrlLoading url:"+url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				System.out.println("ReLoadWebVIEWActivity onPageStarted url:"+url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				System.out.println("ReLoadWebVIEWActivity onPageFinished url:"+url);
			}
			
		});
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReLoadWebVIEWActivity.this, ResultActivity.class);
				startActivityForResult(intent, 0x1000);
//				webView.reload();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("ReLoadWebVIEWActivity onResume");
//		if(flag){
//			webView.reload();
//		}
//		flag = true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("ReLoadWebVIEWActivity onActivityResult");
//		if(flag){
			webView.reload();
//		}
//		flag = true;
	}

}
