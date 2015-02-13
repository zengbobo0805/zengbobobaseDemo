package com.zengbobobase.demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TWWebViewClient extends WebViewClient {

	private void p(String str){
		S.p("webview TWWebViewClient "+str);
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		p("shouldOverrideUrlLoading url:"+url);
		// TODO Auto-generated method stub
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		p("onPageStarted url:"+url);
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		p("onPageFinished url:"+url);
		super.onPageFinished(view, url);
		view.loadUrl("javascript:addChild()");
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		// TODO Auto-generated method stub
		p("onLoadResource url:"+url);
		super.onLoadResource(view, url);
	}

	@SuppressLint("NewApi")
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		// TODO Auto-generated method stub
		p("shouldInterceptRequest url:"+url);
		return super.shouldInterceptRequest(view, url);
	}

	@Override
	@Deprecated
	public void onTooManyRedirects(WebView view, Message cancelMsg,
			Message continueMsg) {
		// TODO Auto-generated method stub
		p("onTooManyRedirects :");
		super.onTooManyRedirects(view, cancelMsg, continueMsg);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		p("onReceivedError errorCode:"+errorCode+",description:"+description+",failingUrl:"+failingUrl);
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public void onFormResubmission(WebView view, Message dontResend,
			Message resend) {
		p("onFormResubmission :");
		// TODO Auto-generated method stub
		super.onFormResubmission(view, dontResend, resend);
	}

	@Override
	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload) {
		// TODO Auto-generated method stub
		p("doUpdateVisitedHistory url:"+url+",isReload:"+isReload);
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		// TODO Auto-generated method stub
		p("onReceivedSslError");
		super.onReceivedSslError(view, handler, error);
	}

	@Override
	public void onReceivedHttpAuthRequest(WebView view,
			HttpAuthHandler handler, String host, String realm) {
		// TODO Auto-generated method stub
		p("onReceivedHttpAuthRequest host:"+host+",realm:"+realm);
		super.onReceivedHttpAuthRequest(view, handler, host, realm);
	}

	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		p("shouldOverrideKeyEvent :");
		return super.shouldOverrideKeyEvent(view, event);
	}

	@Override
	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		p("onUnhandledKeyEvent :");
		super.onUnhandledKeyEvent(view, event);
	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		// TODO Auto-generated method stub
		p("onScaleChanged oldScale:"+oldScale+",newScale:"+newScale);
		super.onScaleChanged(view, oldScale, newScale);
	}

	@SuppressLint("NewApi")
	@Override
	public void onReceivedLoginRequest(WebView view, String realm,
			String account, String args) {
		// TODO Auto-generated method stub
		p("onReceivedLoginRequest realm:"+realm+",account:"+account+",args:"+args);
		super.onReceivedLoginRequest(view, realm, account, args);
	}

}
