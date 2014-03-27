package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.android.R;
import com.zengbobo.android.packageManager.PackageInfoUtil;
import com.zengbobo.android.packageManager.PackageStatsCallBack;

public class FirstActivity extends Activity {

	private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		tv = (TextView) findViewById(R.id.tv);

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PackageInfoUtil.queryPacakgeSize(getApplicationContext(),
						getPackageName(), new PackageStatsCallBack() {

							@Override
							public void onCallBack(PackageStats stats) {
								Message msg = new Message();
								msg.what = 0x0001;
								msg.obj=stats;
								handler.sendMessage(msg);
								
							}
						});
			}
		});
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0x0001){
				PackageStats stats = (PackageStats) msg.obj;
				if (stats == null) {
					return;
				}
					StringBuffer buf = new StringBuffer();
					buf.append("缓存大小:" + (float) stats.cacheSize
							/ (1024 * 1024) + "\n");
					buf.append(" 数据大小:" + (float) stats.dataSize
							/ (1024 * 1024) + "\n");
					buf.append("程序大小:" + (float) stats.codeSize / (1024 * 1024)
							+ "\n");
					
					// buf.append("程序总大小:"+stats.getTotalsize()+"\n");
					buf.append("");
					tv.setText(buf.toString());
			
			}else if(msg.what == 0x1004){
				Toast.makeText(FirstActivity.this, "what:0x1004", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
}
