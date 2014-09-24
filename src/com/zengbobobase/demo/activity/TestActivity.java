package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zengbobobase.demo.R;

public class TestActivity extends Activity implements OnClickListener {
	private List<View> list = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);
		for (int i = 0; i < 10; i++) {
			View view = initGoodsItemView();
			list.add(view);
			layout.addView(view);
		}
	}

	// TODO
	private View initGoodsItemView() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.test_item_layout, null);
		TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
		ImageView img_number_minus = (ImageView) view
				.findViewById(R.id.img_number_minus);
		ImageView img_number_plus = (ImageView) view
				.findViewById(R.id.img_number_plus);
		img_number_minus.setTag(view);
		img_number_plus.setTag(view);
		tv_number.setText("10");
		img_number_minus.setOnClickListener(this);
		img_number_plus.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_number_minus:
			View tagMinusView = (View) v.getTag();
			TextView tv_number = (TextView) tagMinusView
					.findViewById(R.id.tv_number);
			int number = Integer.parseInt(tv_number.getText().toString());
			number--;
			tv_number.setText(number + "");
			for (View view_item : list) {
				TextView tv=(TextView) view_item.findViewById(R.id.tv_number);
				System.out.println("TestActivity img_number_minus:"
						+ tv.getText().toString());
			}
			break;
		case R.id.img_number_plus:
			View tagPlusView = (View) v.getTag();
			TextView tv_number_plus = (TextView) tagPlusView
					.findViewById(R.id.tv_number);
			String str = tv_number_plus.getText()
					.toString();
			int number_plus = Integer.parseInt(tv_number_plus.getText()
					.toString());
			number_plus++;
			tv_number_plus.setText(number_plus + "");
			for (View view_item : list) {
				TextView tv=(TextView) view_item.findViewById(R.id.tv_number);
				System.out.println("TestActivity img_number_minus:"
						+ tv.getText().toString());
			}
			break;

		}
	}
}
