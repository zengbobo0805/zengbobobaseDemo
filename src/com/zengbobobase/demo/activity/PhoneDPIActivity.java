package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
		buf.append("手机型号: " + android.os.Build.MODEL + ",\nSDK版本:"
				+ android.os.Build.VERSION.SDK + ",\n系统版本:"
				+ android.os.Build.VERSION.RELEASE);

		buf.append("Build.BOARD:"+Build.BOARD+"\n");

		try {
			buf.append("android.os.Build.getRadioVersion():"+ Build.getRadioVersion()+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		buf.append("dm.BOOTLOADER:"+ Build.BOOTLOADER+"\n");
		buf.append("dm.BRAND:"+ Build.BRAND+"\n");
		buf.append("dm.DEVICE:"+ Build.DEVICE+"\n");
		buf.append("dm.DISPLAY:"+ Build.DISPLAY+"\n");
		buf.append("dm.FINGERPRINT:"+ Build.FINGERPRINT + "\n");
		buf.append("dm.HARDWARE:"+ Build.HARDWARE+"\n");
		buf.append("dm.BOARD:"+ Build.BOARD+"\n");
		buf.append("dm.ID:"+ Build.ID+"\n");
		buf.append("dm.MANUFACTURER:"+ Build.MANUFACTURER+"\n");
		buf.append("dm.MODEL:"+ Build.MODEL+"\n");
		buf.append("dm.PRODUCT:"+ Build.PRODUCT+"\n");
		buf.append("dm.SERIAL:"+ Build.SERIAL+"\n");
		buf.append("dm.TAGS:"+ Build.TAGS+"\n");
		buf.append("dm.TYPE:"+ Build.TYPE+"\n");
		buf.append("dm.UNKNOWN:"+ Build.UNKNOWN+"\n");
		buf.append("dm.USER:"+ Build.USER+"\n");
		buf.append("dm.CPU_ABI:"+ Build.CPU_ABI+"\n");
		buf.append("dm.CPU_ABI2:"+ Build.CPU_ABI2+"\n");
		buf.append("dm.RADIO:"+ Build.RADIO+"\n");
//		buf.append("dm.SUPPORTED_32_BIT_ABIS:"+ Build.SUPPORTED_32_BIT_ABIS+"\n");
//		buf.append("dm.SUPPORTED_64_BIT_ABIS:"+ Build.SUPPORTED_64_BIT_ABIS+"\n");
//		buf.append("dm.SUPPORTED_ABIS:"+ Build.SUPPORTED_ABIS+"\n");
		buf.append("dm.TIME:"+ Build.TIME+"\n");
		buf.append("dm.CODENAME:"+ Build.VERSION.CODENAME+"\n");
		buf.append("dm.SDK_INT:"+ Build.VERSION.SDK_INT+"\n");
		buf.append("dm.SDK:" + Build.VERSION.SDK + "\n");
		buf.append("dm.INCREMENTAL:" + Build.VERSION.INCREMENTAL + "\n");
		buf.append("dm.RELEASE:" + Build.VERSION.RELEASE + "\n");



		System.out.println("getImei:" + getImei(this));
		tv.setText(buf.toString());
		ScrollView scrollView =new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		scrollView.addView(layout);
		layout.addView(tv);
		setContentView(scrollView);
	}
	public static String getImei(Context context){
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		return  telephonyManager.getDeviceId();
	}
}
