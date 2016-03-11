package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.view.PathEffectView;

public class TestPathEffectActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ListView listView;
    private MyBaseAdapter myBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathEffectView(this));
//        setContentView(R.layout.activity_scrollview_listview_layout);
//        findViewById(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count += 30;
//                myBaseAdapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(listView);
//            }
//        });
//        listView = (ListView) findViewById(R.id.listView);
//        myBaseAdapter = new MyBaseAdapter();
//        listView.setAdapter(myBaseAdapter);
//
//        myBaseAdapter.notifyDataSetChanged();
//        setListViewHeightBasedOnChildren(listView);

    }

    private int count = 30;

    private class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("TestPathEffectActivity getView position:" + position);
            if (convertView == null) {
                convertView = LayoutInflater.from(TestPathEffectActivity.this).inflate(R.layout.listview_scrollview_listview_item, null);
            }
            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_title.setText("position:" + position);
            return convertView;
        }
    }

    /**
     * 动态测量listview-Item的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}