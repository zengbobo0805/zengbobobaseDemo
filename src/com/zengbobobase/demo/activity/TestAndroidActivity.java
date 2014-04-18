package com.zengbobobase.demo.activity;

import com.zengbobobase.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestAndroidActivity extends Activity {
	private TextView tv1,tv2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_android_layout);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		System.out.println("TestAndroidActivity tv1.getText():"+tv1.getText());
		System.out.println("TestAndroidActivity tv2.getText():"+tv2.getText());
		System.out.println("TestAndroidActivity tv1.getText()!=null:"+(tv1.getText()!=null));
		System.out.println("TestAndroidActivity tv2.getText()!=null:"+(tv2.getText()!=null));
		System.out.println("TestAndroidActivity tv1.getText().equals(''):"+(tv1.getText().equals("")));
		System.out.println("TestAndroidActivity tv2.getText().equals(''):"+(tv2.getText().equals("")));
	}
	
}
