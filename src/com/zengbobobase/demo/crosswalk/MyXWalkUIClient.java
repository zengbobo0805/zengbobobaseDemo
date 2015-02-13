package com.zengbobobase.demo.crosswalk;

import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import com.zengbobobase.demo.S;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ValueCallback;

public class MyXWalkUIClient extends XWalkUIClient {
	private void p(String str){
		S.p("CrossWalk MyXWalkUIClient "+str);
	}
	
	public MyXWalkUIClient(XWalkView arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCreateWindowRequested(XWalkView view,
			InitiateBy initiator, ValueCallback<XWalkView> callback) {
		// TODO Auto-generated method stub
		p("onCreateWindowRequested ");
		return super.onCreateWindowRequested(view, initiator, callback);
	}

	@Override
	public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
		// TODO Auto-generated method stub
		super.onFullscreenToggled(view, enterFullscreen);
		p("onFullscreenToggled ");
	}

	@Override
	public void onIconAvailable(XWalkView view, String url,
			Message startDownload) {
		// TODO Auto-generated method stub
		super.onIconAvailable(view, url, startDownload);
		p("onIconAvailable url:"+url);
	}

	@Override
	public void onJavascriptCloseWindow(XWalkView view) {
		// TODO Auto-generated method stub
		super.onJavascriptCloseWindow(view);
		p("onJavascriptCloseWindow ");
	}

	@Override
	public boolean onJavascriptModalDialog(XWalkView view,
			JavascriptMessageType type, String url, String message,
			String defaultValue, XWalkJavascriptResult result) {
		// TODO Auto-generated method stub
		p("onJavascriptModalDialog ");
		return super.onJavascriptModalDialog(view, type, url, message, defaultValue,
				result);
	}

	@Override
	public void onPageLoadStarted(XWalkView view, String url) {
		// TODO Auto-generated method stub
		super.onPageLoadStarted(view, url);
		p("onPageLoadStarted url:"+url);
	}

	@Override
	public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
		// TODO Auto-generated method stub
		super.onPageLoadStopped(view, url, status);
		p("onPageLoadStopped url:"+url);
	}

	@Override
	public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
		// TODO Auto-generated method stub
		super.onReceivedIcon(view, url, icon);
		p("onReceivedIcon  url:"+url);
	}

	@Override
	public void onReceivedTitle(XWalkView view, String title) {
		// TODO Auto-generated method stub
		super.onReceivedTitle(view, title);
		p("onReceivedTitle  title:"+title);
	}

	@Override
	public void onRequestFocus(XWalkView view) {
		// TODO Auto-generated method stub
		super.onRequestFocus(view);
		p("onRequestFocus ");
	}

	@Override
	public void onScaleChanged(XWalkView view, float oldScale, float newScale) {
		// TODO Auto-generated method stub
		super.onScaleChanged(view, oldScale, newScale);
		p("onScaleChanged oldScale:"+oldScale+",newScale:"+newScale);
	}

	@Override
	public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
		// TODO Auto-generated method stub
		super.onUnhandledKeyEvent(view, event);
		p("onUnhandledKeyEvent event:"+event.getAction());
	}

	@Override
	public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile,
			String acceptType, String capture) {
		// TODO Auto-generated method stub
		super.openFileChooser(view, uploadFile, acceptType, capture);
		p("openFileChooser ");
	}

	@Override
	public boolean shouldOverrideKeyEvent(XWalkView view, KeyEvent event) {
		// TODO Auto-generated method stub
		p("shouldOverrideKeyEvent event:"+event.getAction());
		return super.shouldOverrideKeyEvent(view, event);
	}

}
