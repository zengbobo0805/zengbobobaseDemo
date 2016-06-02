package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by bobo on 2016/5/23.
 */
public class CallLogProviderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
                tv.setText("ON CLICK ME!");
        tv.setTextColor(android.R.color.black);
        tv.setBackgroundColor(android.R.color.white);
        tv.setGravity(Gravity.CENTER);
        setContentView(tv,new FrameLayout.LayoutParams(200,200));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent newIntent = new Intent(Intent.A);
//                newIntent.putExtra(Intent.MTK_EXTRA_UNREAD_NUMBER, newCallsCount);
//                newIntent.putExtra(Intent.MTK_EXTRA_UNREAD_COMPONENT
//                        , new ComponentName("",""));
//                sendBroadcast(newIntent); //发送对应的广播
//                android.provider.Settings.System.putInt(getContentResolver(), Constants.CONTACTS_UNREAD_KEY, Integer
//                        .valueOf(newCallsCount));
            }
        });

    }
}
