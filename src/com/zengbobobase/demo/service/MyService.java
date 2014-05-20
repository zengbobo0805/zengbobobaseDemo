package com.zengbobobase.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("MyService  onCreate");
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		System.out.println("MyService  onStart");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("MyService  onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("MyService  onDestroy");
	}

}
