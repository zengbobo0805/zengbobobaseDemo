package com.zengbobobase.demo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.ListView;

import com.gift.android.R;
import com.zengbobo.android.packageManager.PackageInfoUtil;
import com.zengbobobase.demo.adapter.ApplicationAdapter;
import com.zengbobobase.demo.model.BaseModel;

public class ApplicationTotalActivity extends Activity {

	private ListView listView;
	private ApplicationAdapter adapter;
	private List<BaseModel> list = new ArrayList<BaseModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		adapter = new ApplicationAdapter(this);
		// List<ApplicationInfo> listInfo =
		// ApplicationInfoUtil.getInstalledApplications(this);
		List<PackageInfo> listInfo = PackageInfoUtil.loadCustomPackages(this);
		for (PackageInfo info : listInfo) {
			BaseModel model = new BaseModel();
			model.setPackageName(info.packageName);
			model.setName(info.applicationInfo.loadLabel(getPackageManager())
					.toString());
			model.setVersionName(info.versionName);
			model.setIcon(info.applicationInfo.loadIcon(getPackageManager()));
			model.setTotal(Integer.valueOf((int) new File(
					info.applicationInfo.publicSourceDir).length()));
			model.setTime(new Date(new File(info.applicationInfo.sourceDir)
					.lastModified()).toGMTString()
					+ "\n"
					+ info.applicationInfo.sourceDir
					+ "\n"
					+ info.applicationInfo.publicSourceDir);
			adapter.list.add(model);
		}
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

}
