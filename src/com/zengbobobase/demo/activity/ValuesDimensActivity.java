package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.DisplayUtil;

/**
 * Created by bobo on 2016/6/2.
 */
public class ValuesDimensActivity extends Activity {

    private TextView tv1,tv2,tv3,tv4,tv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values_dimens_layout);
        tv1= (TextView) findViewById(R.id.tv1);
        tv1.setText("normal dip size:"+tv1.getTextSize()+""+ DisplayUtil.density(this));

        tv2= (TextView) findViewById(R.id.tv2);
        tv2.setText("no dip size:"+tv2.getTextSize());

        tv3= (TextView) findViewById(R.id.tv3);
        tv3.setText("h dip size:"+tv3.getTextSize());

        tv4= (TextView) findViewById(R.id.tv4);
        tv4.setText("xh dip size:"+tv4.getTextSize());

        tv5= (TextView) findViewById(R.id.tv5);
        tv5.setText("xxh dip size:"+tv5.getTextSize());

    }
}
