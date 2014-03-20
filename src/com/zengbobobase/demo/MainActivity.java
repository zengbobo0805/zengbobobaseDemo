package com.zengbobobase.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zengbobo.android.packageManager.ActivityInfoUtil;
import com.zengbobobase.demo.adapter.MyBaseAdapter;
import com.zengbobobase.demo.model.BaseModel;

public class MainActivity extends Activity {
	private TextView tv;
	private int count =0;
	private ListView listView;
	private MyBaseAdapter adapter;
	private List<BaseModel> list = new ArrayList<BaseModel>();
	private boolean flag=true;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv= (TextView) findViewById(R.id.tv);
		listView = (ListView) findViewById(R.id.list);
		adapter = new MyBaseAdapter(this);
		List<ActivityInfo> listInfo = ActivityInfoUtil.getPackageActivityInfo(this, getPackageName());
		for(ActivityInfo info:listInfo){
			if(info.labelRes==R.string.app_name){
				continue;
			}
			BaseModel model = new BaseModel();
			model.setClassName(info.name);
			model.setName(info.loadLabel(getPackageManager()).toString());
			adapter.list.add(model);
		}
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		flag = true;
		mThread=	new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
//				while(flag){
					count ++;
					tv.setText(count+"");
					try {
						Thread.sleep(4000);
						Message msg = handler.obtainMessage();
						msg.what = 0x1004;
						msg.sendToTarget();
						System.out.println("Thread............0");
						System.out.println("Thread............1");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//				}
				Looper.loop();
			}
		});
		
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0x1004){
//				Toast.makeText(MainActivity.this, "what:0x1004", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	@Override
	protected void onResume() {
		super.onResume();
		mThread.start();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		flag = false;
	}

	

	
	
}
