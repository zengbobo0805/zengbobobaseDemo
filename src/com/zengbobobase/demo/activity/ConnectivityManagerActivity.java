package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.connectivityManager.ConnectivityManagerUtil;

public class ConnectivityManagerActivity extends Activity {
	private TextView tv;
	private StringBuffer buf = new StringBuffer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.os.Debug.startMethodTracing("Entertainment");
		setContentView(R.layout.activity_scrollview_tv);
		tv= (TextView) findViewById(R.id.tv);
		NetworkInfo[]  infoList = ConnectivityManagerUtil.getAllNetworkInfo(this);
		buf.append("getAllNetworkInfo:\n");
		int i=0;
		for(NetworkInfo info:infoList){
			i++;
			buf.append(" ---------------"+i+"-----------------\n");
			buf.append("     getTypeName: "+info.getTypeName()+"\n");
			buf.append("     getType: "+info.getType()+"\n");
			buf.append("     getClass: "+info.getClass()+"\n");
			buf.append("     getDetailedState: "+info.getDetailedState()+"\n");
			buf.append("     getState: "+info.getState()+"\n");
			buf.append("     getExtraInfo: "+info.getExtraInfo()+"\n");
			buf.append("     getReason: "+info.getReason()+"\n");
			buf.append("     getSubtype: "+info.getSubtype()+"\n");
			buf.append("     getSubtypeName: "+info.getSubtypeName()+"\n\n");
		}
		TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE); 
		buf.append(" ---------------TelephonyManager-----------------\n");
		buf.append("     getDeviceId: "+tel.getDeviceId()+"\n");
		buf.append("     getLine1Number: "+tel.getLine1Number()+"\n");
		buf.append("     getNetworkCountryIso: "+tel.getNetworkCountryIso()+"\n");
		buf.append("     getNetworkOperator: "+tel.getNetworkOperator()+"\n");
		buf.append("     getNetworkOperatorName: "+tel.getNetworkOperatorName()+"\n");
		buf.append("     getNetworkType: "+tel.getNetworkType()+"\n");
		buf.append("     getPhoneType: "+tel.getPhoneType()+"\n");
		buf.append("     getSimCountryIso: "+tel.getSimCountryIso()+"\n");
		buf.append("     getSimOperatorName: "+tel.getSimOperatorName()+"\n\n");
		buf.append("     getSimOperator: "+tel.getSimOperator()+"\n\n");
		buf.append("     getSimSerialNumber: "+tel.getSimSerialNumber()+"\n\n");
		buf.append("     getSimState: "+tel.getSimState()+"\n\n");
		buf.append("     getSubscriberId: "+tel.getSubscriberId()+"\n\n");
		buf.append("     getVoiceMailAlphaTag: "+tel.getVoiceMailAlphaTag()+"\n\n");
		buf.append("     getVoiceMailNumber: "+tel.getVoiceMailNumber()+"\n\n");
		
		
		tv.setText(buf.toString());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Debug.stopMethodTracing();
	}
	
}
