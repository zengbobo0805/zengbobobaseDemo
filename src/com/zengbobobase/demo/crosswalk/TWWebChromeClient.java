package com.zengbobobase.demo.crosswalk;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

import com.zengbobobase.demo.S;

public class TWWebChromeClient extends WebChromeClient {

	private void p(String str){
		S.p("webview TWWebChromeClient"+str);
	}
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		p(" onProgressChanged newProgress:"+newProgress);
		// TODO Auto-generated method stub
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		p(" onReceivedTitle title:"+title);
		// TODO Auto-generated method stub
		super.onReceivedTitle(view, title);
	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		p(" onReceivedIcon ");
		// TODO Auto-generated method stub
		super.onReceivedIcon(view, icon);
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url,
			boolean precomposed) {
		p(" onReceivedTouchIconUrl url:"+url);
		// TODO Auto-generated method stub
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		// TODO Auto-generated method stub
		p(" onShowCustomView :");
		super.onShowCustomView(view, callback);
	}

	@SuppressLint("NewApi")
	@Override
	@Deprecated
	public void onShowCustomView(View view, int requestedOrientation,
			CustomViewCallback callback) {
		p(" onShowCustomView -@Deprecated--:");
		// TODO Auto-generated method stub
		super.onShowCustomView(view, requestedOrientation, callback);
	}

	@Override
	public void onHideCustomView() {
		p(" onHideCustomView :");
		// TODO Auto-generated method stub
		super.onHideCustomView();
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog,
			boolean isUserGesture, Message resultMsg) {
		p(" onCreateWindow :");
		// TODO Auto-generated method stub
		return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
	}

	@Override
	public void onRequestFocus(WebView view) {
		p(" onRequestFocus :");
		// TODO Auto-generated method stub
		super.onRequestFocus(view);
	}

	@Override
	public void onCloseWindow(WebView window) {
		p(" onCloseWindow :");
		// TODO Auto-generated method stub
		super.onCloseWindow(window);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		p(" onJsAlert url:"+url+",message:"+message);
		// TODO Auto-generated method stub
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			JsResult result) {
		// TODO Auto-generated method stub
		p(" onJsConfirm url:"+url+",message:"+message);
		return super.onJsConfirm(view, url, message, result);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		// TODO Auto-generated method stub
		p(" onJsPrompt url:"+url+",message:"+message+",defaultValue:"+defaultValue);
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		// TODO Auto-generated method stub
		p(" onJsBeforeUnload url:"+url+",message:"+message);
		return super.onJsBeforeUnload(view, url, message, result);
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {
		// TODO Auto-generated method stub
		p(" onGeolocationPermissionsShowPrompt origin:"+origin);
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	@Override
	public void onGeolocationPermissionsHidePrompt() {
		// TODO Auto-generated method stub
		p(" onGeolocationPermissionsHidePrompt :");
		super.onGeolocationPermissionsHidePrompt();
	}

	@Override
	public boolean onJsTimeout() {
		// TODO Auto-generated method stub
		p(" onJsTimeout :");
		return super.onJsTimeout();
	}

	@Override
	@Deprecated
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		// TODO Auto-generated method stub
		p(" onConsoleMessage message:"+message+",lineNumber:"+lineNumber+",sourceID:"+sourceID);
		super.onConsoleMessage(message, lineNumber, sourceID);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		// TODO Auto-generated method stub
		p(" onConsoleMessage :");
		return super.onConsoleMessage(consoleMessage);
	}

	@Override
	public Bitmap getDefaultVideoPoster() {
		// TODO Auto-generated method stub
		p(" getDefaultVideoPoster :");
		return super.getDefaultVideoPoster();
	}

	@Override
	public View getVideoLoadingProgressView() {
		// TODO Auto-generated method stub
		p(" getVideoLoadingProgressView :");
		return super.getVideoLoadingProgressView();
	}

	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		// TODO Auto-generated method stub
		p(" getVisitedHistory :");
		super.getVisitedHistory(callback);
	}

	@Override
	@Deprecated
	public void onExceededDatabaseQuota(String url, String databaseIdentifier,
			long quota, long estimatedDatabaseSize, long totalQuota,
			QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		p(" onExceededDatabaseQuota :");
		super.onExceededDatabaseQuota(url, databaseIdentifier, quota,
				estimatedDatabaseSize, totalQuota, quotaUpdater);
	}

	@Override
	@Deprecated
	public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
			QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		p(" onReachedMaxAppCacheSize :");
		super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
	}

}
