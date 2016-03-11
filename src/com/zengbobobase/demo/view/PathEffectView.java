package com.zengbobobase.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathDashPathEffect.Style;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

public class PathEffectView extends View{

	private Paint mPaint;
	private Paint starPaint;
	private Path mPath;
	private Path star;

	private float phase = 0;
	
	public PathEffectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PathEffectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PathEffectView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for(int i=0;i<9;i++){
			mPath.moveTo(0, 0+(i*50));
			mPath.lineTo(50, 30+(i*50));
			mPath.lineTo(100,0+(i*50));
			mPath.lineTo(150, 30+(i*50));
			mPath.lineTo(200, 0+(i*50));
			PathEffect pe = null;
			switch (i) {
			case 0:
//				这个类的作用就是将Path的各个连接线段之间的夹角用一种更平滑的方式连接，类似于圆弧与切线的效果。
//				一般的，通过CornerPathEffect(float radius)指定一个具体的圆弧半径来实例化一个CornerPathEffect。
				pe = new CornerPathEffect(30);
				break;
			case 1:
//				这个类的作用就是将Path的线段虚线化。
//				构造函数为DashPathEffect(float[] intervals, float offset)，其中intervals为虚线的ON和OFF数组，
//				该数组的length必须大于等于2，phase为绘制时的偏移量。
				pe = new DashPathEffect(new float[]{10f,3f}, phase);
				break;
			case 2:
//				这个类的作用是打散Path的线段，使得在原来路径的基础上发生打散效果。
//				一般的，通过构造DiscretePathEffect(float segmentLength,float deviation)来构造一个实例，
//				其中，segmentLength指定最大的段长，deviation指定偏离量。
				pe = new DiscretePathEffect(5f, 5f);
				break;
			case 3:
//				这个类的作用是使用Path图形来填充当前的路径，其构造函数为
//				PathDashPathEffect (Path shape, float advance, float phase,PathDashPathEffect.Stylestyle)。
//				shape则是指填充图形，advance指每个图形间的间距，phase为绘制时的偏移量，style为该类自由的枚举值，
//				有三种情况：Style.ROTATE、Style.MORPH和
//				Style.TRANSLATE。其中ROTATE的情况下，线段连接处的图形转换以旋转到与下一段移动方向相一致的角度进行转转，
//				MORPH时图形会以发生拉伸或压缩等变形的情况与下一段相连接，
//				TRANSLATE时，图形会以位置平移的方式与下一段相连接。
				pe = new PathDashPathEffect(star, 20, phase, Style.ROTATE);
				break;
			case 4:
				pe = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				break;
			case 5:
				pe = new PathDashPathEffect(star, 20, phase, Style.TRANSLATE);
				break;
			case 6:
//				组合效果，这个类需要两个PathEffect参数来构造一个实例，
//              ComposePathEffect (PathEffect outerpe,PathEffect innerpe)，
//				表现时，会首先将innerpe表现出来，然后再在innerpe的基础上去增加outerpe的效果。
				PathEffect cpeOuter = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				PathEffect cpeInner = new DiscretePathEffect(5f, 10f);
				pe = new ComposePathEffect(cpeOuter, cpeInner);
				break;
			case 7:
//				叠加效果，这个类也需要两个PathEffect作为参数SumPathEffect(PathEffect first,PathEffect second)，
//				但与ComposePathEffect不同的是，在表现时，会分别对两个参数的效果各自独立进行表现，
//				然后将两个效果简单的重叠在一起显示出来。
				PathEffect cpe1 = new PathDashPathEffect(star, 20, phase, Style.MORPH);
				PathEffect cpe2 = new DiscretePathEffect(5f, 10f);
				pe = new SumPathEffect(cpe1, cpe2);
				break;
			default:
				break;
			}
			mPaint.setPathEffect(pe);
			canvas.drawPath(mPath, mPaint);
			mPath.reset();
		}
//		phase+=2;
//		invalidate();
	}
	
	private void init(){
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPaint.setColor(Color.RED);
		mPath = new Path();	
		starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		starPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		starPaint.setColor(Color.YELLOW);
		star(10);
	}
	
	private void star(float length){
		star = new Path();
		float dis1 = (float)((length/2)/Math.tan((54f/180)*Math.PI));
		float dis2 = (float)(length*Math.sin((72f/180)*Math.PI));
		float dis3 = (float)(length*Math.cos((72f/180)*Math.PI));
		star.moveTo(length/2, 0);
		star.lineTo(length/2-dis3, dis2);
		star.lineTo(length, dis1);
		star.lineTo(0, dis1);
		star.lineTo(length/2+dis3, dis2);
		star.lineTo(length/2, 0);
	}
	
}
