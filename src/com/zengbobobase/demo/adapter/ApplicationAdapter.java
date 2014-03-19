package com.zengbobobase.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.model.BaseModel;

public class ApplicationAdapter extends AbBaseAdapter {
	
	
	public ApplicationAdapter(Context mContext) {
		super(mContext);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold;
		if(convertView ==null){
			viewHold = new ViewHold();
			convertView = inflater.inflate(R.layout.adapter_application_layout, null);
			viewHold.packageName = (TextView) convertView.findViewById(R.id.packageName);
			viewHold.name = (TextView) convertView.findViewById(R.id.name);
			viewHold.icon = (ImageView) convertView.findViewById(R.id.icon);
			viewHold.total = (TextView) convertView.findViewById(R.id.total);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		final BaseModel model = list.get(position);
		viewHold.packageName.setText(model.getPackageName());
		viewHold.name.setText(model.getName()+"     版本:"+model.getVersionName());
		viewHold.icon.setImageDrawable(model.getIcon());
		viewHold.total.setText("程序大小："+(float)model.getTotal()/(1024*1024)+"   安装时间:"+model.getTime());
		
		return convertView;
	}
	
}
