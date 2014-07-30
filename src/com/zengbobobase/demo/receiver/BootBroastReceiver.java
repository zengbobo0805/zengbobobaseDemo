package com.zengbobobase.demo.receiver;

import com.zengbobobase.demo.activity.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class BootBroastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("BootBroastReceiver onReceive");
		Intent intent_new = new Intent(context, MainActivity.class);
		intent_new.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent_new);
	}

}
