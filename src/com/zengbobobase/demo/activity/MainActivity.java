package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.zengbobo.android.packageManager.ActivityInfoUtil;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.adapter.MyBaseAdapter;
import com.zengbobobase.demo.model.BaseModel;
import com.zengbobobase.demo.utils.Base64Utils;
import com.zengbobobase.demo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
//	private ImageView img;
	private TextView tv;
	private int count = 0;
	private ListView listView;
	private MyBaseAdapter adapter;
	private List<BaseModel> list = new ArrayList<BaseModel>();
	private boolean flag = true;
	private Thread mThread;

	// private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		img = (ImageView) findViewById(R.id.tv);
		tv = (TextView) findViewById(R.id.tv);
		System.out.println("MainActivity onCreate tv.getTextSize():"+tv.getTextSize()+",dip2px:"+ DisplayUtil.dip2px(this,14));
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String str ="{\"data\":\"{\\\"columnId\\\":\\\"1\\\",\\\"id\\\":\\\"725346\\\"}\",\"id\":\"725346\",\"page\":\"info\",\"type\":\"1\"}";
				String endcodeStr = Base64Utils.encodeToString(str);
				System.out.println("MainActivity onCreate length:"+endcodeStr.length()+",endcodeStr:"+endcodeStr);
//				Uri uri =Uri.parse("xrzgp://www.icaikee.com?"+endcodeStr);
				Uri uri =Uri.parse("xrzgp://www.icaikee.com?eyJkYXRhIjoie1wiY29sdW1uSWRcIjpcIjFcIixcImlkXCI6XCI3MjUzNDZcIn0iLCJpZCI6IjcyNTM0NiIsInBhZ2UiOiJpbmZvIiwidHlwZSI6IjEifQ==");

				intent.setData(uri);
				startActivity(intent);
			}
		});

		// bm = BitmapFactory.decodeResource(getResources(), R.drawable.big);
		// img.setImageBitmap(bm);
		listView = (ListView) findViewById(R.id.list);
		adapter = new MyBaseAdapter(this);
		List<ActivityInfo> listInfo = ActivityInfoUtil.getPackageActivityInfo(
				this, getPackageName());
		for (ActivityInfo info : listInfo) {
			if (info.labelRes == R.string.app_name_title) {
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
		mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				// while(flag){
				count++;

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
				// }
				Looper.loop();
			}
		});
		mThread.start();
		saveHistoryInfo1("aa", "", null);
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		System.out.println("saveHistoryInfo1 list:" + list.size());

		list.remove("3");

		System.out.println("saveHistoryInfo1 remove list:" + list.size());

		String buf = "hell; ";

		System.out.println("saveHistoryInfo1 hell;.split(';').length:"
				+ ("hell;".split(";").length));
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定

//		PushManager
//				.startWork(this,
//						PushConstants.LOGIN_TYPE_API_KEY,
//						Contast.BAIDU_TUISONG_APP_KEY);
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());

	}

	private void saveHistoryInfo1(String... args) {
		System.out.println("saveHistoryInfo1 args.length:" + args.length);

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x1004) {
				// Toast.makeText(MainActivity.this, "what:0x1004",
				// Toast.LENGTH_SHORT).show();
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
		// img.setImageBitmap(null);
		// System.out.println("saveHistoryInfo1 args.length:"+(bm.getRowBytes()*bm.getHeight())/1024);
		// bm.recycle();
		// bm=null;
		// System.gc();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		flag = false;
		PushManager.stopWork(this);
	}

}
