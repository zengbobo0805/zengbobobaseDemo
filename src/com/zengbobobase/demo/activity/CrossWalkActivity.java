package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.crosswalk.MyXWalkResourceClient;
import com.zengbobobase.demo.crosswalk.MyXWalkUIClient;
import com.zengbobobase.demo.crosswalk.TWXWalkView;

public class CrossWalkActivity extends Activity {

	/************************** 1 START *******************************/
	// TODO SAMPLE 1
	// private XWalkView mXWalkView;
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_crosswalk_layout);
	// mXWalkView = (XWalkView) findViewById(R.id.xWalkView);
	// // turn on debugging
	// XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
	// // this loads a file from the assets/ directory
	// mXWalkView.load("file:///android_asset/crosswalk/index.html", null);
	// }
	/************************** 1 END *******************************/

	/************************** 2 START *******************************/
	// TODO SAMPLE 2
	TWXWalkView mXwalkView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crosswalk_layout);
		mXwalkView = (TWXWalkView) findViewById(R.id.xWalkView);
		mXwalkView.setResourceClient(new MyXWalkResourceClient(mXwalkView));
		mXwalkView.setUIClient(new MyXWalkUIClient(mXwalkView));
//		mXwalkView.load("http://www.crosswalk-project.org", null);
		mXwalkView.load("file:///android_asset/crosswalk/index.html", null);
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (mXwalkView != null) {
//			mXwalkView.pauseTimers();
//			mXwalkView.onHide();
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (mXwalkView != null) {
//			mXwalkView.resumeTimers();
//			mXwalkView.onShow();
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (mXwalkView != null) {
//			mXwalkView.onDestroy();
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (mXwalkView != null) {
//			mXwalkView.onActivityResult(requestCode, resultCode, data);
//		}
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		if (mXwalkView != null) {
//			mXwalkView.onNewIntent(intent);
//		}
//	}
}
