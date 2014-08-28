package com.zengbobobase.demo.myview;

import com.zengbobobase.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * 
 * @author http://http://blog.csdn.net/qinjuning
 */
//���п����л�����Activity
public class MultiScreenActivity extends Activity implements OnClickListener {

	private Button mScrollLeftButton;
    private Button mScrollRightButton;
	private Button mStopScrollButton;
    private Button mLogDataButton;
	private MultiViewGroup mMulTiViewGroup;
	
	public static int mScreenWidth;
	public static int mScrrenHeight;
	
	private static final String TAG = MultiScreenActivity.class.getName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels ;
		mScrrenHeight = metric.heightPixels;

		setContentView(R.layout.multiview);
 
 		mMulTiViewGroup = (MultiViewGroup)findViewById(R.id.mymultiViewGroup);
		
		mScrollLeftButton = (Button) findViewById(R.id.scroll_left);
		mStopScrollButton = (Button) findViewById(R.id.stop_scroll);
        mLogDataButton = (Button) findViewById(R.id.log_data);
        mScrollRightButton = (Button) findViewById(R.id.scroll_right);

		mScrollLeftButton.setOnClickListener(this);
		mStopScrollButton.setOnClickListener(this);
        mLogDataButton.setOnClickListener(this);
        mScrollRightButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.scroll_left:
			mMulTiViewGroup.moveToLeftSide() ;
			break;
        case R.id.scroll_right:
                mMulTiViewGroup.moveToRightSide(); ;
                break;
		case R.id.stop_scroll:
			mMulTiViewGroup.stopMove() ;
			break;
        case R.id.log_data:
            logData();
            break;
		}
	}

    private void logData() {
      //  mMulTiViewGroup.scrollTo(200, 0);
       //  mMulTiViewGroup.offsetLeftAndRight(200);
        Log.d(TAG, "scroll in logData:" + mMulTiViewGroup.getScrollX());
    }

}
