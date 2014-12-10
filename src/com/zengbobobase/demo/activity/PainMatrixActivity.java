package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.zengbobobase.demo.R;

public class PainMatrixActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyView myView = new MyView(PainMatrixActivity.this);
		setContentView(myView);
	}

	class MyView extends View {
		private Bitmap bm;// 位图实例
		private Matrix matrix = new Matrix();// Matrix实例
		private float angle = 0.0f;// 旋转角度
		private int w, h;// 位图宽和高
		private float scale = 1.0f;// 缩放比例
		private boolean isScale = false;// 判断缩放还是旋转

		// 构造方法
		public MyView(Context context) {
			super(context);
			// 获得位图
			bm = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.bg_content);
			w = bm.getWidth();// 获得位图宽
			h = bm.getHeight();// 获得位图高
			// 使当前视图获得焦点
			this.setFocusable(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 重置Matrix
			matrix.reset();
			if (!isScale) {
				// 旋转Matrix
				matrix.setRotate(angle);
			} else {
				// 缩放Matrix
				matrix.setScale(scale, scale);
			}
			// 根据原始位图和Matrix创建新视图
			Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
			// 绘制新视图
			canvas.drawBitmap(bm2, matrix, null);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// 向左旋转
			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {// 键盘←
				isScale = false;
				angle++;
				postInvalidate();
			}
			// 向右旋转
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {// 键盘→
				isScale = false;
				angle--;
				postInvalidate();
			}
			// 放大
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {// 键盘↑
				isScale = true;
				if (scale < 2.0)
					scale += 0.1;
				postInvalidate();
			}
			// 缩小
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {// 键盘↓
				isScale = true;
				if (scale > 0.5)
					scale -= 0.1;
				postInvalidate();
			}
			return super.onKeyDown(keyCode, event);
		}
	}

}
