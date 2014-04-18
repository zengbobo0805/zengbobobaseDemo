package com.zengbobobase.demo;

import android.app.Application;
import android.widget.TextView;

public class MyApplication extends Application {
	private static MyApplication instance;
	public static MyApplication getInstance(){
		return instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		new TextView(this);
	}
	
	
}
