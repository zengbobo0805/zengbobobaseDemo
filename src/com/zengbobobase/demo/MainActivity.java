package com.zengbobobase.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;

import com.zengbobo.android.packageManager.ActivityInfoUtil;
import com.zengbobobase.demo.adapter.MyBaseAdapter;
import com.zengbobobase.demo.model.BaseModel;

public class MainActivity extends Activity {
	private ListView listView;
	private MyBaseAdapter adapter;
	private List<BaseModel> list = new ArrayList<BaseModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		
		

	}

	
	
}
