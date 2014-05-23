package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.zengbobobase.demo.R;

public class DalvikActivity extends Activity {
	private TextView tv;
	private StringBuffer buf = new StringBuffer();
	private ActivityManager mActivityManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrollview_tv);
		tv = (TextView) findViewById(R.id.tv);
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int memory =  mActivityManager.getMemoryClass();
		buf.append("获得Dalvik虚拟机的Java Object Heap的最大值:"+memory+"M\n");
		tv.setText(buf.toString());
	}

}
