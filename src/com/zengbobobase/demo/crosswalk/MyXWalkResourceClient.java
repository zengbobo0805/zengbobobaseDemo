package com.zengbobobase.demo.crosswalk;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import com.zengbobobase.demo.S;

import android.net.http.SslError;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;

public class MyXWalkResourceClient extends XWalkResourceClient {
	private void p(String str){
		S.p("CrossWalk MyXWalkResourceClient "+str);
	}
	public MyXWalkResourceClient(XWalkView arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLoadFinished(XWalkView view, String url) {
		// TODO Auto-generated method stub
		super.onLoadFinished(view, url);
		p("onLoadFinished url:"+url);
	}

	@Override
	public void onLoadStarted(XWalkView view, String url) {
		// TODO Auto-generated method stub
		super.onLoadStarted(view, url);
		p("onLoadStarted url:"+url);
	}

	@Override
	public void onProgressChanged(XWalkView view, int progressInPercent) {
		// TODO Auto-generated method stub
		super.onProgressChanged(view, progressInPercent);
		p("onProgressChanged progressInPercent:"+progressInPercent);
	}

	@Override
	public void onReceivedLoadError(XWalkView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		super.onReceivedLoadError(view, errorCode, description, failingUrl);
		p("onReceivedLoadError errorCode:"+errorCode+",description:"+description+",failingUrl:"+failingUrl);
	}

	@Override
	public void onReceivedSslError(XWalkView view,
			ValueCallback<Boolean> callback, SslError error) {
		// TODO Auto-generated method stub
		super.onReceivedSslError(view, callback, error);
		p("onReceivedSslError ");
	}

	@Override
	public WebResourceResponse shouldInterceptLoadRequest(XWalkView view,
			String url) {
		// TODO Auto-generated method stub
		p("shouldInterceptLoadRequest  url:"+url);
		return super.shouldInterceptLoadRequest(view, url);
	}

	@Override
	public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
		// TODO Auto-generated method stub
		p("shouldOverrideUrlLoading  url:"+url);
		return super.shouldOverrideUrlLoading(view, url);
	}

}
