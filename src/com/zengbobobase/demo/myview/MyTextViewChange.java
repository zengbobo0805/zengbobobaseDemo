package com.zengbobobase.demo.myview;

import com.zengbobo.android.utils.StringUtil;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.DisplayUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 自定义文本显示控件 该自定义控件中的文本可以在9个方位进行控制 左上——中上——右上 左中——中中——右中 左下——中下——右下
 * 
 * @author carrey
 * */
public class MyTextViewChange extends View {
	/** 要显示的文字 */
	private String title;
	/** 文字的颜色 */
	private int titleColor, priceColor, qiColor, danWeiColor;
	/** 文字的大小 */
	private int titleSize, priceSize, qiSize, danWeiSize;
	/** 文本中轴线X坐标 */
	private float[] textCenterX;
	/** 文本baseline线Y坐标 */
	private float[] textBaselineY;
	/** 每行绘制文本数量 */
	private int[] lineTextCount;
	/** 控件的宽度 */
	private int viewWidth;
	/** 控件的高度 */
	private float viewHeight;
	/** 控件画笔 */
	private Paint paint;
	private FontMetrics fm;
	/** 场景 */
	private Context context;
	/** 绘制每行的文本字符个数 */
	private int[] lineDrawTextCount;

	private String price = "1234";
	private String priceDanWeiS = "￥";
	private String priceQiS = "起";
	private String textMore = "...";
	private float priceDanWeiCenterX;
	private float priceQiCenterX;
	private float priceCenterX;
	private float textMoreCenterX;

	public MyTextViewChange(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	
	public MyTextViewChange(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setViewAttributes(context, attrs,0);
		init();
	}
	public MyTextViewChange(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		this.context = context;
		setViewAttributes(context, attrs, defStyle);
		init();

	}
//	http://www.cnblogs.com/angeldevil/p/3479431.html
	private void setViewAttributes(Context context, AttributeSet attrs,
			int defStyle) {
		final TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTextview, defStyle,
				R.style.Widget_GifMoviewView);

		titleSize = (int) array.getDimension(
				R.styleable.CustomTextview_titleSize, -1);
		priceSize = (int) array.getDimension(
				R.styleable.CustomTextview_priceSize, -1);
		qiSize = (int) array
				.getDimension(R.styleable.CustomTextview_qiSize, -1);
		danWeiSize = (int) array.getDimension(
				R.styleable.CustomTextview_danWeiSize, -1);

		title = array.getString(R.styleable.CustomTextview_title);
		price = array.getString(R.styleable.CustomTextview_price);

		titleColor = array.getColor(R.styleable.CustomTextview_titleColor, -1);
		priceColor = array.getColor(R.styleable.CustomTextview_priceColor, -1);
		qiColor = array.getColor(R.styleable.CustomTextview_qiColor, -1);
		danWeiColor = array
				.getColor(R.styleable.CustomTextview_danWeiColor, -1);

		array.recycle();
	}

	private void initSize() {
		if (titleSize == -1) {
			titleSize = DisplayUtil.dip2px(context, 22);
		}
		if (priceSize == -1) {
			priceSize = DisplayUtil.dip2px(context, 22);
		}
		if (qiSize == -1) {
			qiSize = DisplayUtil.dip2px(context, 14);
		}
		if (danWeiSize == -1) {
			danWeiSize = DisplayUtil.dip2px(context, 14);
		}
	}

	private void initColor() {
		setBackgroundColor(Color.WHITE);
		if (titleSize == -1) {
			titleColor = Color.BLACK;
		}
		if (priceColor == -1) {
			priceColor = Color.RED;
		}
		if (qiColor == -1) {
			qiColor = Color.BLACK;
		}
		if (danWeiColor == -1) {
			danWeiColor = Color.RED;
		}
	}

	private void initText() {
		if (StringUtil.equalsNullOrEmpty(title)) {
			title = "上海景域文化传播有限公司--驴妈妈旅游--上海驴途信息科技有限公司（上海普陀区）";
		}
		if (StringUtil.equalsNullOrEmpty(price))  {
			price = "1234";
		}
		if (StringUtil.equalsNullOrEmpty(priceDanWeiS)) {
			priceDanWeiS = "￥";
		}
		if (StringUtil.equalsNullOrEmpty(priceQiS))  {
			priceQiS = "起";
		}
		if (StringUtil.equalsNullOrEmpty(textMore))  {
			textMore = "...";
		}
	}

	private void setPaintDefault() {
		setPaintColorAndSize(titleColor, titleSize);
	}

	private void setPaintColorAndSize(int color, int size) {
		paint.setColor(color);
		paint.setTextSize(size);
	}

	/** * 变量初始化 */
	private void init() {
		initSize();
		initColor();
		initText();

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);
		setPaintDefault();
		fm = paint.getFontMetrics();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, (int) viewHeight);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		viewWidth = getWidth();
		setTextLocation();
		super.onLayout(changed, left, top, right, (int) (top + viewHeight));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 绘制控件内容
		// setTextLocation();

		System.out.println("MyTextViewChange onDraw");
		if (textCenterX == null || textCenterX.length <= 0) {
			return;
		}
		setPaintDefault();
		if (textCenterX.length == 1) {
			canvas.drawText(title, textCenterX[0], textBaselineY[0], paint);
		} else if (textCenterX.length == 2) {
			canvas.drawText(title.substring(0, lineDrawTextCount[0]),
					textCenterX[0], textBaselineY[0], paint);
			canvas.drawText(
					title.substring(lineDrawTextCount[0], lineDrawTextCount[0]
							+ lineDrawTextCount[1]), textCenterX[1],
					textBaselineY[1], paint);

			if (title.length() > lineDrawTextCount[0] + lineDrawTextCount[1]) {
				canvas.drawText(textMore, textCenterX[1] * 2 + textMoreCenterX,
						textBaselineY[1], paint);
			}
		}
		setPaintColorAndSize(qiColor, qiSize);
		canvas.drawText(priceQiS, viewWidth - priceQiCenterX, fm.bottom
				- fm.top - fm.ascent, paint);

		setPaintColorAndSize(priceColor, priceSize);
		canvas.drawText(price, viewWidth - priceQiCenterX * 2 - priceCenterX,
				fm.bottom - fm.top - fm.ascent, paint);

		setPaintColorAndSize(danWeiColor, danWeiSize);
		canvas.drawText(priceDanWeiS, viewWidth - priceQiCenterX * 2
				- priceCenterX * 2 - priceDanWeiCenterX, fm.bottom - fm.top
				- fm.ascent, paint);

		super.onDraw(canvas);
	}

	/** * 定位文本绘制的位置 */
	private void setTextLocation() {
		setPaintColorAndSize(danWeiColor, danWeiSize);
		priceDanWeiCenterX = paint.measureText(priceDanWeiS) / 2;

		setPaintColorAndSize(qiColor, qiSize);
		priceQiCenterX = paint.measureText(priceQiS) / 2;

		setPaintColorAndSize(priceColor, priceSize);
		priceCenterX = paint.measureText(price) / 2;

		// 文本的宽度
		int textLength = title.length();
		setPaintDefault();
		float textWidth = paint.measureText(title);
		textMoreCenterX = paint.measureText(textMore) / 2;

		float priceWidth = (priceDanWeiCenterX + priceQiCenterX + priceCenterX) * 2;

		if (viewWidth >= textWidth) {
			textCenterX = new float[1];
			textBaselineY = new float[1];
			lineDrawTextCount = new int[1];
			textCenterX[0] = textWidth / 2;
			textBaselineY[0] = -fm.ascent;
			lineDrawTextCount[0] = textLength;
		} else {
			textCenterX = new float[2];
			textBaselineY = new float[2];
			lineDrawTextCount = new int[2];
			int drawTextCount = (int) ((viewWidth / textWidth) * textLength);
			drawTextCount = measureTextLine(0, drawTextCount, viewWidth, 0);
			drawTextCount = lastMesuresCount(0, drawTextCount, viewWidth);
			textCenterX[0] = paint.measureText(title
					.substring(0, drawTextCount)) / 2;
			textBaselineY[0] = -fm.ascent;
			lineDrawTextCount[0] = drawTextCount;

			if (textLength >= lineDrawTextCount[0] * 2) {
				drawTextCount = measureTextLine(lineDrawTextCount[0],
						lineDrawTextCount[0], viewWidth - priceWidth
								- textMoreCenterX * 2, 0);
			} else {
				drawTextCount = measureTextLine(lineDrawTextCount[0],
						textLength - lineDrawTextCount[0], viewWidth
								- priceWidth - textMoreCenterX * 2, 0);
			}
			drawTextCount = lastMesuresCount(lineDrawTextCount[0],
					drawTextCount, viewWidth);
			textCenterX[1] = paint
					.measureText(title.substring(lineDrawTextCount[0],
							lineDrawTextCount[0] + drawTextCount)) / 2;
			textBaselineY[1] = fm.bottom - fm.top - fm.ascent;
			lineDrawTextCount[1] = drawTextCount;
		}
		viewHeight = (fm.bottom - fm.top) * 2;

	}

	private int lastMesuresCount(int startIndex, int count, float width) {
		setPaintDefault();
		float mesWidth = paint.measureText(title.substring(startIndex,
				startIndex + count));
		if (mesWidth > width) {
			count--;
		}
		return count;
	}

	private int measureTextLine(int startIndex, int lineCount, float width,
			int flag) {
		setPaintDefault();
		float textWidthLine = paint.measureText(title.substring(startIndex,
				startIndex + lineCount));
		if (textWidthLine > width && flag != 1) {
			if (lineCount <= 0) {
				return 0;
			}
			lineCount--;
			lineCount = measureTextLine(startIndex, lineCount, width, -1);
		} else if (textWidthLine < width && flag != -1) {
			if (title.length() <= startIndex + lineCount) {
				return title.length() - startIndex;
			}
			lineCount++;
			lineCount = measureTextLine(startIndex, lineCount, width, 1);
		}
		return lineCount;
	}

	/** * 设置文本内容 * @param text */
	public void setTitle(String text) {
		this.title = text;
		invalidate();
	}


	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
		invalidate();
	}

	public void setPriceColor(int priceColor) {
		this.priceColor = priceColor;
		invalidate();
	}

	public void setQiColor(int qiColor) {
		this.qiColor = qiColor;
		invalidate();
	}

	public void setDanWeiColor(int danWeiColor) {
		this.danWeiColor = danWeiColor;
		invalidate();
	}

	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
		invalidate();
	}

	public void setPriceSize(int priceSize) {
		this.priceSize = priceSize;
		invalidate();
	}

	public void setQiSize(int qiSize) {
		this.qiSize = qiSize;
		invalidate();
	}

	public void setDanWeiSize(int danWeiSize) {
		this.danWeiSize = danWeiSize;
		invalidate();
	}

	public void setPrice(String price) {
		this.price = price;
		invalidate();
	}

	public void setPriceDanWeiS(String priceDanWeiS) {
		this.priceDanWeiS = priceDanWeiS;
		invalidate();
	}

	public void setPriceQiS(String priceQiS) {
		this.priceQiS = priceQiS;
		invalidate();
	}

	public void setTextMore(String textMore) {
		this.textMore = textMore;
		invalidate();
	}

}
