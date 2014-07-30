package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.zengbobobase.demo.R;

public class EditTextActivity extends Activity {

	private EditText edittext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edittext);
		edittext = (EditText) findViewById(R.id.edittext);
		edittext.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}
}
