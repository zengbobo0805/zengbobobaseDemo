package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class PhoneDPIActivity extends Activity {
	private DisplayMetrics dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv= new TextView(this);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		StringBuffer buf = new StringBuffer();
		buf.append("dm.density:"+dm.density+"\n");
		buf.append("dm.densityDpi:"+dm.densityDpi+"\n");
		buf.append("dm.heightPixels:"+dm.heightPixels+"\n");
		buf.append("dm.widthPixels:"+dm.widthPixels+"\n");
		buf.append("dm.scaledDensity:"+dm.scaledDensity+"\n");
		buf.append("dm.xdpi:"+dm.xdpi+"\n");
		buf.append("dm.ydpi:"+dm.ydpi+"\n");
		buf.append("getImei:"+getImei(this)+"\n");
		System.out.println("getImei:"+getImei(this));
		tv.setText(buf.toString());
		
		setContentView(tv);
	}
	public static String getImei(Context context){
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		return  telephonyManager.getDeviceId();
	}
}
