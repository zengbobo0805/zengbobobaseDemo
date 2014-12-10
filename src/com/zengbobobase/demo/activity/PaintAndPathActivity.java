package com.zengbobobase.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

public class PaintAndPathActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}

	// 自定义视图类
	@SuppressLint("DrawAllocation")
	private class MyView extends View {
		public MyView(Context context) {
			super(context);
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

			RectF re = new RectF(10, 220, 70, 250);// 声明矩形
			canvas.drawOval(re, paint);// 画椭圆

			Path path = new Path();// 实例化路径
			path.moveTo(70, 200);// 移动到指定点
			path.lineTo(10, 300);// 画线
			path.lineTo(70, 300);// 画线
			path.close();// 关闭路径
			paint.setAlpha(50);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawPath(path, paint);// 画路径

			Path path_1 = new Path();// 实例化路径
			path_1.moveTo(10, 330);// 移动到指定点
			path_1.lineTo(70, 330);// 画线
			path_1.lineTo(40, 270);// 画线
			path_1.close();// 关闭路径
			canvas.drawPath(path_1, paint);// 画路径

			Path path1 = new Path();// 实例化路径
			path1.moveTo(10, 410);// 移动到指定点
			path1.lineTo(70, 410);// 画线
			path1.lineTo(55, 350);// 画线
			path1.lineTo(25, 350);// 画线
			path1.close();// 关闭路径
			canvas.drawPath(path1, paint);// 画路径
			// ---------------------------1-------------------------------------------
			paint.setStyle(Paint.Style.FILL);// 设置样式
			paint.setColor(Color.BLUE);// 设置颜色
			canvas.drawCircle(120, 40, 30, paint);// 画圆
			canvas.drawRect(90, 90, 150, 150, paint);// 画矩形
			canvas.drawRect(90, 170, 150, 200, paint);// 画矩形

			RectF re2 = new RectF(90, 220, 150, 250);// 实例化矩形
			canvas.drawOval(re2, paint);// 画椭圆
			Path path2 = new Path();// 实例化路径
			path2.moveTo(90, 330);// 移动到指定点
			path2.lineTo(150, 330);// 画线
			path2.lineTo(120, 270);
			path2.close();
			canvas.drawPath(path2, paint);
			Path path3 = new Path();
			path3.moveTo(90, 410);
			path3.lineTo(150, 410);
			path3.lineTo(135, 350);
			path3.lineTo(105, 350);
			path3.close();
			canvas.drawPath(path3, paint);
			// ------------------------------2----------------------------------
			// 线性渲染
			Shader mShader = new LinearGradient(0, 0, 100, 100, new int[] {
					Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW }, null,
					Shader.TileMode.REPEAT);
			paint.setShader(mShader);// 为Paint设置线性渲染
			canvas.drawCircle(200, 40, 30, paint);
			canvas.drawRect(170, 90, 230, 150, paint);
			canvas.drawRect(170, 170, 230, 200, paint);
			
			RectF re3 = new RectF(170, 220, 230, 250);
			canvas.drawOval(re3, paint);
			
			Path path4 = new Path();
			path4.moveTo(170, 330);
			path4.lineTo(230, 330);
			path4.lineTo(200, 270);
			path4.close();
			canvas.drawPath(path4, paint);
			Path path5 = new Path();
			path5.moveTo(170, 410);
			path5.lineTo(230, 410);
			path5.lineTo(215, 350);
			path5.lineTo(185, 350);
			path5.close();
			canvas.drawPath(path5, paint);
			paint.setTextSize(24);// 设置文本大小
			// 写文字
			canvas.drawText("str_text1", 240, 50, paint);
			canvas.drawText("str_text2", 240, 120, paint);
			canvas.drawText("str_text3", 240, 190, paint);
			canvas.drawText("str_text4", 240, 250, paint);
			canvas.drawText("str_text5", 240, 320, paint);
			canvas.drawText("str_text6", 240, 390, paint);
		}
	}
}
