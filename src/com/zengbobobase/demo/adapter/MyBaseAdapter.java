package com.zengbobobase.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gift.android.R;
import com.zengbobobase.demo.model.BaseModel;

public class MyBaseAdapter extends AbBaseAdapter {
	
	
	public MyBaseAdapter(Context mContext) {
		super(mContext);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold;
		if(convertView ==null){
			viewHold = new ViewHold();
			convertView = inflater.inflate(R.layout.adapter_layout, null);
			viewHold.packageName = (TextView) convertView.findViewById(R.id.packageName);
			viewHold.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		final BaseModel model = list.get(position);
		viewHold.packageName.setText(model.getClassName());
		viewHold.name.setText(model.getName());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(mContext, Class.forName(model.getClassName()));
					mContext.startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		return convertView;
	}
	
}
