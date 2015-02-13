package com.zengbobobase.demo.activity;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;

import com.zengbobobase.demo.R;

public class Experiment_ac_Demo extends Activity implements Callback {

	// 小数点格式化
	DecimalFormat dFormat = new DecimalFormat("00.00");
	SurfaceView surface;
	SurfaceHolder mHolder = null;
	Timer mTimer;
	TimerTask mTask;
	TextView mTextView;
	float surWidth, surHeigth;
	float light_x, light_y;
	float Li_point_x, Li_point_y;
	Handler mHandler = null;
	Canvas surCanvas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_demo_layout);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		surWidth = metrics.widthPixels;
		surHeigth = metrics.heightPixels;
		init_point();
		initView();
	}

	private void init_point() {
		Li_point_x = 43;
		light_x = 43;
		Li_point_y = surHeigth / 4;
		light_y = surHeigth / 4;
	}

	private void initView() {
		surface = (SurfaceView) findViewById(R.id.ex_demo_sur);
		mTextView = (TextView) findViewById(R.id.ex_demo_text);
		mHolder = surface.getHolder();
		// 设置表面视图放置在最顶部(即：没有一个窗口的内容本SurfaceView可见它的表面)
		surface.setZOrderOnTop(true);
		// 游戏框架的表面层设置格式为像素格式透明
		surface.getHolder().setFormat(PixelFormat.TRANSPARENT);
		mHolder.addCallback(this);
	}

	boolean judge = false;
	int time = 0;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		mTimer = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (time % 2 == 0) {
					judge = true;
				} else {
					judge = false;
				}
				Message message = new Message();
				message.what = 1;
				if (mHandler != null) {

					if (time % 2 == 0) {
						Li_point_y = surHeigth / 4 + 20;
						Li_point_x = Li_point_x + 3;
					} else if (time % 3 == 0) {
						Li_point_y = surHeigth / 4 - 10;
						Li_point_x = Li_point_x + 3;
					} else if (time % 5 == 0) {
						Li_point_y = surHeigth / 4 + 10;
						Li_point_x = Li_point_x + 3;
					} else {
						Li_point_y = surHeigth / 4 - 20;
						Li_point_x = Li_point_x + 3;

					}
					if (mHandler != null) {
						mHandler.sendMessage(message);
					}

				}
				time++;
			}
		};

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 1) {
					if (Li_point_x > surWidth) {
						Canvas canvas = mHolder.lockCanvas();
						if (canvas != null) {
							// 清除画布
							canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
							mHolder.unlockCanvasAndPost(canvas);
							init_point();
							DrawBack(mHolder);
							Canvas canvas2 = mHolder.lockCanvas(new Rect(43,
									60, (int) surWidth,
									(int) surHeigth / 2 - 20));
							canvas2.drawColor(Color.TRANSPARENT, Mode.CLEAR);
							mHolder.unlockCanvasAndPost(canvas2);
							time = 0;
						}
					}
					/* clear(surCanvas); */
					if (mHolder != null) {
						DrawWave(mHolder);
					}
				}
			}
		};
		mTimer.schedule(mTask, 100, 100);
		super.onStart();
	}

	private void DrawBack(SurfaceHolder holder) {

		surCanvas = holder.lockCanvas();
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setAntiAlias(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.BLACK);
		surCanvas.drawLine(40, surHeigth / 2, surWidth, surHeigth / 2, paint);
		surCanvas.drawLine(40, 40, 40, surHeigth / 2, paint);
		surCanvas.drawLine(35, 60, 40, 40, paint);
		surCanvas.drawLine(45, 60, 40, 40, paint);
		surCanvas.drawLine(surWidth - 20, surHeigth / 2 - 5, surWidth,
				surHeigth / 2, paint);
		surCanvas.drawLine(surWidth - 20, surHeigth / 2 + 5, surWidth,
				surHeigth / 2, paint);
		surCanvas.drawText("0", 25, surHeigth / 2, paint);
		/* surCanvas.drawText("10",25, surHeigth/4, paint); */
		surCanvas.drawText("value", 25, 40, paint);
		surCanvas.drawText("time", surWidth - 25, surHeigth / 2 + 15, paint);
		holder.unlockCanvasAndPost(surCanvas);
		holder.lockCanvas(new Rect(0, 0, 0, 0));
		holder.unlockCanvasAndPost(surCanvas);
	}

	private void DrawWave(SurfaceHolder holder) {
		final float value = (float) Math.hypot((Li_point_x - light_x),
				(Li_point_y - light_y));
		surCanvas = holder.lockCanvas(new Rect((int) light_x,
				(int) (light_y - value), (int) (Li_point_x + value),
				(int) (Li_point_y + value)));
		if (surCanvas != null) {
			Paint onePaint = new Paint();
			onePaint.setAntiAlias(true);
			onePaint.setDither(true);
			onePaint.setStrokeJoin(Paint.Join.ROUND);
			onePaint.setColor(Color.BLUE);
			surCanvas.drawLine(light_x, light_y, Li_point_x, Li_point_y,
					onePaint);
			holder.unlockCanvasAndPost(surCanvas);
			holder.lockCanvas(new Rect(0, 0, 0, 0));
			holder.unlockCanvasAndPost(surCanvas);
			light_x = Li_point_x;
			light_y = Li_point_y;
			mTextView.setText("Ac：  " + dFormat.format(Li_point_y));
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		DrawBack(mHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	/** 清除画笔之前绘制的痕迹 **/
	public void clear(Canvas aCanvas) {
		Paint paint = new Paint();
		// 设置图形重叠时的处理方式（例如：合并，取交集或并集）
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		aCanvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mTimer.cancel();
		super.onDestroy();

	}
}
