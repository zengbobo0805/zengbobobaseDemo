package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.view.MyGridView;

public class ListViewItemGridActivity extends Activity {
	LayoutInflater inflater;
	ListView mlListView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView mlListView = new ListView(this);
		setContentView(mlListView);
		inflater= LayoutInflater.from(this);
	}

	private class MyListViewAdpater extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold viewHold;
			if(convertView ==null){
				viewHold= new ViewHold();
				convertView = inflater.inflate(R.layout.listview_item_grid_layout, null);
				viewHold.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
				viewHold.myGridView=(MyGridView) convertView.findViewById(R.id.myGridView);
				convertView.setTag(viewHold);
			}else{
				viewHold = (ViewHold) convertView.getTag();
			}
			
			return null;
		}
		
		class ViewHold{
			TextView tv_title;
			MyGridView myGridView;
		}
	}
	
	
}
