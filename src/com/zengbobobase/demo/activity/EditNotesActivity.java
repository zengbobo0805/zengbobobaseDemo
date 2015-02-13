package com.zengbobobase.demo.activity;

import com.zengbobobase.demo.view.DewdropView;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class EditNotesActivity extends Activity {

	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		DewdropView view = new DewdropView(this);
		setContentView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		view.postInvalidate();
	}

	
}
