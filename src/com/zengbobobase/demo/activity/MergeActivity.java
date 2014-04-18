package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zengbobobase.demo.R;

public class MergeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout=new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		View view=LayoutInflater.from(this).inflate(R.layout.merge_layout, layout, true);
		
		
//		setContentView(layout);

		setContentView(R.layout.merge_layout);
//		setContentView(R.layout.framelayout_inclube_layout);
//		setContentView(R.layout.relativelayout_inclube_layout);
//		setContentView(R.layout.linearlayout_inclube_layout);
	}

}
