package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;

import com.zengbobo.android.packageManager.ActivityInfoUtil;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.adapter.MyBaseAdapter;
import com.zengbobobase.demo.model.BaseModel;

public class MainActivity extends Activity {
	private ImageView img;
	private int count =0;
	private ListView listView;
	private MyBaseAdapter adapter;
	private List<BaseModel> list = new ArrayList<BaseModel>();
	private boolean flag=true;
	private Thread mThread;
//	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		img= (ImageView) findViewById(R.id.tv);
//		 bm = BitmapFactory.decodeResource(getResources(), R.drawable.big);
//		img.setImageBitmap(bm);
		listView = (ListView) findViewById(R.id.list);
		adapter = new MyBaseAdapter(this);
		List<ActivityInfo> listInfo = ActivityInfoUtil.getPackageActivityInfo(this, getPackageName());
		for(ActivityInfo info:listInfo){
			if(info.labelRes==R.string.app_name_title){
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
		mThread.start();
		saveHistoryInfo1("aa","",null);
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		 System.out.println("saveHistoryInfo1 list:"+list.size());
		 
		 list.remove("3");
		 
		 System.out.println("saveHistoryInfo1 remove list:"+list.size());
		 
		 String buf ="hell; ";

		 System.out.println("saveHistoryInfo1 hell;.split(';').length:"+("hell;".split(";").length));
		
	}
	
	private void saveHistoryInfo1(String... args) {
		 System.out.println("saveHistoryInfo1 args.length:"+args.length);
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
		
	}

	@Override
	protected void onPause() {
		super.onPause();
//		img.setImageBitmap(null);
//		 System.out.println("saveHistoryInfo1 args.length:"+(bm.getRowBytes()*bm.getHeight())/1024);
//		bm.recycle();
//		bm=null;
//		System.gc();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		flag = false;
	}

	

	
	
}
