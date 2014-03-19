package com.zengbobobase.demo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zengbobobase.demo.model.BaseModel;

public class AbBaseAdapter extends BaseAdapter {
	public List<BaseModel> list = new ArrayList<BaseModel>();
	public Context mContext;
	public LayoutInflater inflater;
	
	
	
	public AbBaseAdapter(Context mContext) {
		super();
		this.mContext= mContext;
		 inflater = LayoutInflater.from(mContext);
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BaseModel getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return convertView;
	}

	class ViewHold{
		TextView packageName;
		TextView name;
		ImageView icon;
		TextView total;
	}

}
