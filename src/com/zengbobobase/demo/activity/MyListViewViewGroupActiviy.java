package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zengbobobase.demo.view.MyScrollItemView;

public class MyListViewViewGroupActiviy extends Activity {

	private  MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    ListView listView = new ListView(this){

			@Override
			public boolean onTouchEvent(MotionEvent ev) {
				System.out.println("MainActivity ListView onTouchEvent ev.getAction:"+ev.getAction());
				return super.onTouchEvent(ev);
			}

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev) {
				System.out.println("MainActivity ListView onInterceptTouchEvent ev.getAction:"+ev.getAction());
				return super.onInterceptTouchEvent(ev);
			}

			@Override
			public boolean dispatchTouchEvent(MotionEvent ev) {
				System.out.println("MainActivity ListView dispatchTouchEvent ev.getAction:"+ev.getAction());
				boolean flag = super.dispatchTouchEvent(ev);
				System.out.println("MainActivity ListView dispatchTouchEvent flag:"+flag);
				onTouchEvent(ev);
				return flag;
			}
	    	
	    	
	    	};
        setContentView(listView);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        listView.requestFocus();
        myAdapter.notifyDataSetChanged();
//        listView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				return false;
//			}
//		});
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("MainActivity OnItemClickListener onItemClick:");
			}
		});
        listView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				System.out.println("MainActivity OnLongClickListener onLongClick:");
				return false;
			}
		});
    }

    private class MyAdapter extends BaseAdapter{
    	private Context mContext;
    	
		public MyAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 20;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView !=null){
//				return	convertView;
//			}
			LinearLayout layout = new LinearLayout(mContext){

				@Override
				public boolean dispatchTouchEvent(MotionEvent event) {
					System.out.println("MyScrollItemView MyAdapter LinearLayout dispatchTouchEvent event:"+event.getAction());
					return super.dispatchTouchEvent(event);
				}

				@Override
				public boolean onInterceptTouchEvent(MotionEvent event) {
					System.out.println("MyScrollItemView MyAdapter LinearLayout onInterceptTouchEvent event:"+event.getAction());
					return super.onInterceptTouchEvent(event);
				}

				@Override
				public boolean onTouchEvent(MotionEvent event) {	
					System.out.println("MyScrollItemView  MyAdapter LinearLayout onTouchEvent event:"+event.getAction());
					return super.onTouchEvent(event);
				}
				
			};
			layout.addView(new MyScrollItemView(mContext));
			
			return layout;
		}
    	
    }
}
