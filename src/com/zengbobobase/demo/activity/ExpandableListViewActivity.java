package com.zengbobobase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.zengbobobase.demo.R;

public class ExpandableListViewActivity extends Activity {
	ExpandableListView expandableListView;
	MyExpandableListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_listview_layout);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		adapter = new MyExpandableListViewAdapter(this);
	
		expandableListView.setAdapter(adapter);
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						System.out
								.println("ExpandableListViewActivity onCreate onGroupExpand groupPosition:"
										+ groupPosition);
					}
				});

		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				System.out
						.println("ExpandableListViewActivity onCreate onChildClick groupPosition:"
								+ groupPosition
								+ ",childPosition:"
								+ childPosition + ",id:" + id);
				System.out
				.println("ExpandableListViewActivity onCreate onChildClick v:"+v.getId());
				return true;
			}
		});
	
		for (int i = 0; i < 100000; i++) {
			System.out
			.println("ExpandableListViewActivity onCreate  i:"+i);
			adapter.getGroup().add(i + "-----");
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < 1; j++) {
				list.add(i + "----" + j);
			}
			adapter.getChild().add(list);

			expandableListView.expandGroup(i);
		}
//		for (int i = 0; i < adapter.getGroupCount(); i++) {
//			expandableListView.expandGroup(i);
//
//		}
		adapter.notifyDataSetChanged();

	}

	private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
		private Context context;
		private ArrayList<String> group = new ArrayList<String>();
		private ArrayList<List<String>> childArray = new ArrayList<List<String>>();

		public MyExpandableListViewAdapter(Context context) {
			super();
			this.context = context;
		}

		public ArrayList<String> getGroup() {
			return group;
		}

		public ArrayList<List<String>> getChild() {
			return childArray;
		}

		@Override
		public int getGroupCount() {
			return group.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childArray.get(groupPosition).size();
		}

		@Override
		public String getGroup(int groupPosition) {
			return group.get(groupPosition);
		}

		@Override
		public String getChild(int groupPosition, int childPosition) {
			return childArray.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			System.out
					.println("MyExpandableListViewAdapter getGroupView groupPosition:"
							+ groupPosition);
			System.out
					.println("MyExpandableListViewAdapter getGroupView convertView:"
							+ convertView);
			TextView tv;
			if (convertView == null) {
				tv = new TextView(context);
				tv.setBackgroundColor(context.getResources().getColor(
						R.color.blue));
				convertView = tv;
				convertView.setTag(tv);
			} else {
				tv = (TextView) convertView.getTag();
			}
			tv.setText(group.get(groupPosition));
			
			if(groupPosition==0){
				tv.setVisibility(View.GONE);
			}else{
				tv.setVisibility(View.VISIBLE);
			}
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			System.out
					.println("MyExpandableListViewAdapter getChildView groupPosition:"
							+ groupPosition + ",childPosition:" + childPosition);
			System.out
					.println("MyExpandableListViewAdapter getChildView convertView:"
							+ convertView);

			if (groupPosition==0 ) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.v5_special_price_list_header, null);
				convertView.findViewById(R.id.img_fanhui).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						System.out.println("MyExpandableListViewAdapter getChildView OnClickListener ");
					}
				});
			}else if(groupPosition!=0 ){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.v5_special_price_list_item, null);
			}
		
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
	// getPackedPositionGroup (long packedPosition) ：返回所选择的组
	//
	// 　　　　getPackedPositionForChild (int groupPosition, int childPosition)
	// ：返回所选择的子项
	//
	// 　　　　getPackedPositionType (long packedPosition) ：返回所选择项的类型（Child,Group）
	//
	// 　　　　isGroupExpanded (int groupPosition) :判断此组是否展开
}
