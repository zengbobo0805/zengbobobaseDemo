package com.zengbobobase.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;
//mScroller.getCurrX() //获取mScroller当前水平滚动的位置
//mScroller.getCurrY() //获取mScroller当前竖直滚动的位置
//mScroller.getFinalX() //获取mScroller最终停止的水平位置
//mScroller.getFinalY() //获取mScroller最终停止的竖直位置
public class MyScroller extends LinearLayout {
	private Context mContext;
	private Scroller mScroller;
	

	public MyScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public MyScroller(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	@SuppressLint("NewApi")
	public MyScroller(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		mScroller = new Scroller(mContext);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			DownY= event.getY();
			nextMoveY= DownY;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			int dmy=(int) ((DownY - nextMoveY) / 3);
			Log.i("MyScroll", "zengbobo MyScroll ACTION_MOVE dmy:" + dmy+",countY:"+countY);
			Log.i("MyScroll", "zengbobo MyScroll ACTION_UP mScroller getCurrY:" + mScroller.getCurrY()+",getFinalY:"+mScroller.getFinalY()+",getStartY:"+mScroller.getStartY());
			mScroller.startScroll(mScroller.getStartX(), mScroller.getStartY(), 0, 0);
			postInvalidate();
			nextMoveY = 0;
			DownY=0;
			countY=0;
			break;
		case MotionEvent.ACTION_MOVE:

			float currentY = event.getY();
			int dy = (int) ((currentY - nextMoveY) / 3);
			Log.i("MyScroll", "zengbobo MyScroll ACTION_MOVE dy:" + dy);
			Log.i("MyScroll", "zengbobo MyScroll ACTION_MOVE mScroller getCurrY:" + mScroller.getCurrY()+",getFinalY:"+mScroller.getFinalY()+",getStartY:"+mScroller.getStartY());
			if (nextMoveY != 0) {
				countY+=dy;
				scrollBy(0, -dy);
//				mScroller.startScroll(0, 0, 0, dy);
				postInvalidate();
			}

			nextMoveY = currentY;
			break;
		}

		return true;
	}

	private float nextMoveY = 0;
	private float DownY = 0;
	private int countY = 0;

	@Override
	public void computeScroll() {
		// 如果mScroller没有调用startScroll，这里将会返回false。
		Log.i("MyScroll",
				"zengbobo MyScroll computeScroll mScroller.computeScrollOffset():"
						+ mScroller.computeScrollOffset());
		if (mScroller.computeScrollOffset()) {
			// 因为调用computeScroll函数的是MyLinearLayout实例，
			// 所以调用scrollTo移动的将是该实例的孩子，也就是MyButton实例	
			Log.i("MyScroll", "zengbobo MyScroll computeScroll mScroller getCurrY:" + mScroller.getCurrY()+",getFinalY:"+mScroller.getFinalY()+",getStartY:"+mScroller.getStartY());
			
			scrollTo(mScroller.getStartX(), mScroller.getStartY());
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);// 设置Canvas颜色
		Paint paint = new Paint();// 实例化Paint
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);// 设置颜色
		paint.setStyle(Paint.Style.STROKE);// 设置样式
		paint.setStrokeWidth(3);// 设置笔画粗细
		canvas.drawCircle(40, 40, 30, paint);// 画圆
		canvas.drawRect(10, 90, 70, 150, paint);// 画矩形
		canvas.drawRect(10, 170, 70, 200, paint);// 画矩形

		
	}

}
