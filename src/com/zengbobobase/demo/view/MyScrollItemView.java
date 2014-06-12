package com.zengbobobase.demo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class MyScrollItemView extends ViewGroup {
	private Context mContext;
	private Scroller mScroller;
	private LinearLayout linearLayout;
	private View leftView;
	private View rightView;
	private int width;
	private int height;
	private boolean isShowRightView = false;
	private boolean isShowLeftView = false;
	private int rightViewDisX = 0;
	private int leftViewDisX = 0;
	private VelocityTracker vTracker = null;

	public MyScrollItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MyScrollItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public MyScrollItemView(Context context) {
		super(context);
		init(context);

	}

	private void init(Context context) {
		this.mContext = context;
		width = context.getResources().getDisplayMetrics().widthPixels;
		height = context.getResources().getDisplayMetrics().heightPixels;
		rightViewDisX = width / 6;
		leftViewDisX = width / 6;

		System.out.println("MyScrollItemView init width:" + width + ",height:"
				+ height + ",rightViewDisX:" + rightViewDisX + ",leftViewDisX:"
				+ leftViewDisX);

		this.mScroller = new Scroller(mContext);
		linearLayout = new LinearLayout(mContext) {

			@Override
			public boolean dispatchTouchEvent(MotionEvent event) {
				System.out
						.println("MyScrollItemView  LinearLayout dispatchTouchEvent event:"
								+ event.getAction());

				return super.dispatchTouchEvent(event);
			}

			@Override
			public boolean onInterceptTouchEvent(MotionEvent event) {
				System.out
						.println("MyScrollItemView LinearLayout onInterceptTouchEvent event:"
								+ event.getAction());
				return false;
			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {
				System.out
						.println("MyScrollItemView  LinearLayout onTouchEvent event:"
								+ event.getAction());
				return onMyTouchEvent(event);
			}

		};
		linearLayout.setTag(1);
		linearLayout.setBackgroundColor(Color.RED);
		TextView tv = new TextView(mContext);
		tv.setText("my onclick linearlayout!");
		linearLayout.addView(tv);
		leftView = new View(mContext);
		leftView.setTag(2);
		leftView.setBackgroundColor(Color.BLACK);
		rightView = new View(mContext);
		rightView.setTag(3);
		rightView.setBackgroundColor(Color.BLUE);
		addView(linearLayout);
		addView(leftView);
		addView(rightView);
		this.setTag(4);
		leftView.setOnClickListener(myClickListener);
		linearLayout.setOnClickListener(myClickListener);
		rightView.setOnClickListener(myClickListener);

	}

	private OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			System.out.println("MyScrollItemView myClickListener onClick tag:"
					+ tag);

		}
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		System.out.println("MyScrollItemView dispatchTouchEvent event:"
				+ event.getAction());
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		System.out.println("MyScrollItemView onInterceptTouchEvent event:"
				+ event.getAction());

		return false;
	}

	private float mDownX;
	private float mDownY;
	private long mDownTime;

	public boolean onMyTouchEvent(MotionEvent event) {
		System.out.println("MyScrollItemView onTouchEvent event:"
				+ event.getAction());
		float x = event.getX();
		float y = event.getY();
		long currentTime = System.currentTimeMillis();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = x;
			mDownY = y;
			mDownTime = currentTime;
			if (vTracker == null) {
				vTracker = VelocityTracker.obtain();
			} else {
				vTracker.clear();
			}
			vTracker.addMovement(event);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			float disX = mDownX - x;
			float disY = mDownY - y;
			vTracker.addMovement(event);
			vTracker.computeCurrentVelocity(1000);

			if ((disX > 0 && disX > width / 4)
					|| vTracker.getXVelocity() < -width / 4) {
				if (isShowLeftView) {
					hideLeftView();
				} else {
					showRightView();
				}
			} else if ((disX < 0 && disX < -width / 4)
					|| vTracker.getXVelocity() > width / 4) {
				if (isShowRightView) {
					hideRightView();
				} else {
					showLeftView();
				}
			} else if (currentTime - mDownTime <= 2000) {
				myClickListener.onClick(MyScrollItemView.this);
			}
			vTracker.recycle();
		default:
			break;
		}
		return true;

	}

	private void showLeftView() {
		System.out.println("MyScrollItemView showRightView getScrollX():"
				+ getScrollX() + ",isShowRightView:" + isShowRightView);
		if (isShowLeftView) {
			return;
		}
		isShowLeftView = true;
		mScroller.startScroll(getScrollX(), 0, rightViewDisX, 0, 500);
		invalidate();
	}

	private void hideLeftView() {
		System.out.println("MyScrollItemView hideRightView getScrollX():"
				+ getScrollX() + ",isShowRightView:" + isShowRightView);
		if (!isShowLeftView) {
			return;
		}
		isShowLeftView = false;
		mScroller.startScroll(0, 0, 0, 0, 500);
		invalidate();
	}

	private void showRightView() {
		System.out.println("MyScrollItemView showRightView getScrollX():"
				+ getScrollX() + ",isShowRightView:" + isShowRightView);
		if (isShowRightView) {
			return;
		}
		isShowRightView = true;
		mScroller.startScroll(0, 0, -rightViewDisX, 0, 500);
		invalidate();
	}

	private void hideRightView() {
		System.out.println("MyScrollItemView hideRightView getScrollX():"
				+ getScrollX() + ",isShowRightView:" + isShowRightView);
		if (!isShowRightView) {
			return;
		}
		isShowRightView = false;
		mScroller.startScroll(0, 0, 0, 0, 500);
		invalidate();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		System.out
				.println("MyScrollItemView computeScroll mScroller.computeScrollOffset():"
						+ mScroller.computeScrollOffset());
		System.out
				.println("MyScrollItemView computeScroll mScroller.getCurrX():"
						+ mScroller.getCurrX());
		System.out
				.println("MyScrollItemView computeScroll  mScroller.getCurrY():"
						+ mScroller.getCurrY());

		if (mScroller.computeScrollOffset()) {
			scrollTo(-mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		b = heightMeasure;
		System.out.println("MyScrollItemView onLayout l:" + l + ",t:" + t
				+ ",r:" + r + ",b:" + b);
		leftView.layout(-rightViewDisX + l, t, l, b);
		linearLayout.layout(l, t, l + width, b);
		rightView.layout(l + width, t, l + width + rightViewDisX, b);
	}

	int heightMeasure = 200;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// System.out.println("MyScrollItemView onLayout onMeasure");
		// // 设置该ViewGroup的大小
		// int width = MeasureSpec.getSize(widthMeasureSpec);
		// int height = MeasureSpec.getSize(heightMeasureSpec);
		// setMeasuredDimension(width, height);
		//
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			// 设置每个子视图的大小 ， 即全屏
			child.measure(widthMeasureSpec, heightMeasureSpec);
			if (i == 0) {
				heightMeasure = child.getMeasuredHeight();

			}
		}
		setMeasuredDimension(width + 2 * rightViewDisX, heightMeasure);

	}

}
