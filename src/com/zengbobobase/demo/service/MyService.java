package com.zengbobobase.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("MyService  onBind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("MyService  onUnbind");
		return super.onUnbind(intent);
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
//		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("MyService  onDestroy"); 
		Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class);  //销毁时重新启动Service
        this.startService(localIntent);
	}

}
