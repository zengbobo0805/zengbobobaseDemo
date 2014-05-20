package com.zengbobobase.demo.activity;


import com.zengbobobase.demo.service.MyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnclickActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VISIBLE);
		TextView tv1 = new TextView(this);
		tv1.setText("on click me to MyService ...");
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OnclickActivity.this,MyService.class);
				OnclickActivity.this.startService(intent);
			}
		});
		layout.addView(tv1);
		setContentView(layout);
	}
	
}
