package com.zengbobobase.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.DisplayUtil;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2016/3/22 11:55
 * 修改人：bobo
 * 修改时间：2016/3/22 11:55
 * 修改备注：
 */
public class ViewPageIndicator extends View {

    private Paint textPaint;
    private Paint scrollPaint;
    private Paint linePaint;

    private int delta = 15;

    private float mTextHeight;
    private float[] mTextWidth;
    private OnClickViewPageIndicatorListener onClickViewPageIndicatorListener;
    private PorterDuffXfermode xformode;
    private PorterDuffXfermode scrollXformode;
    private String[] mText = {"银行转证券", "证券转银行", "资金流水"};
    private int textColor;
    private int scrollPaintColor;
    private int linePaintColor;

    public void setmText(String[] mText) {
        this.mText = mText;
    }

    public void setOnClickViewPageIndicatorListener(OnClickViewPageIndicatorListener onClickViewPageIndicatorListener) {
        this.onClickViewPageIndicatorListener = onClickViewPageIndicatorListener;
    }

    /**
     * 背景结束位置X
     */
    private int scrollEndX;

    /**
     * 背景开始位置X
     */
    private int scrollStartX;

    /**
     * 背景框停留位置
     */
    private int position = 0;


    public ViewPageIndicator(Context ctx) {
        this(ctx, null);
    }


    public ViewPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewAndDatas();

    }

    public void initViewAndDatas() {
        textColor = getResources().getColor(R.color.app_textColor_red);
        scrollPaintColor = getResources().getColor(R.color.app_textColor_red);
        linePaintColor = getResources().getColor(R.color.app_textColor_red);
        setBackgroundResource(R.drawable.shape_border_red_bg_white);
        initTextPaint();
        initScrollPaint();
        initLinePaint();
    }

    private void initTextPaint() {
        xformode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setXfermode(null);
        setTextColor(textColor);
        setTextSize(DisplayUtil.dip2px(getContext(), 16));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setXfermode(null);
        textPaint.setTextAlign(Paint.Align.LEFT);

        //文字精确高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        mTextHeight = -fontMetrics.ascent;
        mTextWidth = new float[textLength()];
        for (int i = 0; i < 3; i++) {
            mTextWidth[i] = textPaint.measureText(mText[i]);

        }

    }

    private void initScrollPaint() {
        scrollXformode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        scrollPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrollPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scrollPaint.setStrokeWidth(scrollStrokeWidth());
        setScrollPaintColor(scrollPaintColor);

    }

    private void initLinePaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLinePaintColor(linePaintColor);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(scrollStrokeWidth());
    }


    public void setTextSize(int size) {
        textPaint.setTextSize(size);
    }

    public void setTextColor(int color) {
        textColor = color;
        textPaint.setColor(color);
    }

    public void setScrollPaintColor(int color) {
        scrollPaintColor = color;
        scrollPaint.setColor(color);
    }

    public void setLinePaintColor(int color) {
        linePaintColor = color;
        linePaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth;
        int mHeight;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;

        } else {
            // 由图片决定的宽
            int desireByText = 0;
            for (int i = 0; i < textLength(); i++) {
                desireByText += mTextWidth[i] + 50;
            }
            if (specMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(desireByText, specSize);
            } else {
                mWidth = desireByText;
            }
        }
        /***
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int desireText = (int) (mTextHeight * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desireText, specSize);
            } else {
                mHeight = desireText;
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    private int postIndex;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("ViewPageIndicator onDraw postIndex:" + postIndex);
        int contentWidth = getWidth();
        int contentHeight = getHeight();
        /**
         * 绘制滚动背景
         */
        Bitmap bgBitmap = Bitmap.createBitmap(getWidth() - scrollStrokeWidth() * 2
                , getHeight() - scrollStrokeWidth() * 2, Bitmap.Config.ARGB_8888);
        Canvas bgCanvas = new Canvas(bgBitmap);
        RectF rectF = new RectF(postIndex + scrollStartX
                , 0
                , postIndex + scrollStartX + textWidth()
                , contentHeight);
        scrollPaint.setStrokeWidth(scrollStrokeWidth());
        scrollPaint.setXfermode(null);
        scrollPaint.setColor(getResources().getColor(R.color.white));
        bgCanvas.drawRoundRect(new RectF(0, 0, contentWidth - scrollStrokeWidth() * 2
                , contentHeight - scrollStrokeWidth() * 2)
                , scrollRadio(), scrollRadio(), scrollPaint);

        scrollPaint.setXfermode(scrollXformode);
        scrollPaint.setColor(scrollPaintColor);
        bgCanvas.drawRect(rectF, scrollPaint);
        canvas.drawBitmap(bgBitmap, scrollStrokeWidth(), scrollStrokeWidth(), null);

        /**
         * 绘制文字效果；
         */
        Bitmap srcBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas srcCanvas = new Canvas(srcBitmap);

        textPaint.setXfermode(null);
        textPaint.setColor(textColor);
        for (int i = 0; i < mText.length; i++) {
            float offextX = (contentWidth / 3 - mTextWidth[i]) / 2 + (contentWidth * i) / 3;
            srcCanvas.drawText(mText[i], offextX, contentHeight / 2 + mTextHeight / 2
                    , textPaint);

        }

        textPaint.setXfermode(xformode);
        textPaint.setColor(Color.WHITE);
        srcCanvas.drawRect(rectF, textPaint);
        canvas.drawBitmap(srcBitmap, 0, 0, null);

        /**
         * 绘制分割线
         */
        for (int i = 1; i < textLength(); i++) {
            canvas.drawLine(textWidth() * i, 0, textWidth() * i, getHeight(), linePaint);
        }

        if (Math.abs(postIndex) < Math.abs(scrollEndX - scrollStartX)) {
//            if(scrollEndX>scrollStartX){
//                postIndex+=10;
//            }else{
//                postIndex-=10;
//            }
            postIndex +=speek();
            postInvalidateDelayed(5);
        } else {
            scrollStartX = scrollEndX;
            postIndex = 0;
        }
    }


    private int speek(){
        return (scrollEndX-scrollStartX)*5 /duration;
    }

    private int scrollStrokeWidth() {
        return DisplayUtil.dip2px(getContext(), 1);
    }

    private int scrollRadio() {
        return DisplayUtil.dip2px(getContext(), 3);
    }

    /**
     * mText文本长度
     *
     * @return
     */
    private int textLength() {
        return mText.length;
    }

    /**
     * mText每个文本平均宽度
     *
     * @return
     */
    public int textWidth() {
        return getWidth() / textLength();
    }


    /**
     * 设置背景介绍位置
     *
     * @param scrollEndX
     */
    public  void setScrollEndX(int scrollEndX ,int position) {
        this.scrollStartX += postIndex;
        postIndex = 0;
        this.position = position;
        this.scrollEndX = scrollEndX;
        invalidate();
//        scrollAnim(0, scrollEndX - scrollStartX, duration);
    }

    private int duration = 60;


    public int getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(int postIndex) {
        System.out.println("ViewPageIndicator setPostIndex postIndex:" + postIndex);
        this.postIndex = postIndex;
        invalidate();
    }

    public void scrollAnim(int fromX, int toX, long duration) {
//        ObjectAnimator animator = ObjectAnimator.ofInt(this, "postIndex", fromX, toX);
//        animator.setDuration(duration);
////        animator.setInterpolator(new DecelerateInterpolator());
//        animator.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30);
//                    setPostIndex();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });




    }

    private float downX, downY;
    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("ViewPageIndicator onTouchEvent event.getAction():" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (Math.abs(downX - event.getX()) <= getScaledTouchSlop()
                    && Math.abs(downY - event.getY()) <= getScaledTouchSlop()) {
                for (int i = 0; i < textLength(); i++) {
                    if (i * textWidth() <= event.getX()
                            && event.getX() <= textWidth() * (i + 1)) {
                        setScrollEndX(i * textWidth(),i);
                        if (onClickViewPageIndicatorListener != null) {
                            if (System.currentTimeMillis() - downTime <= getLongPressTimeout()) {
                                onClickViewPageIndicatorListener.onClickViewPageIndicator(position);
                            } else {
                                onClickViewPageIndicatorListener.onLongPressViewPageIndicator(position);
                            }
                        }
                    }
                }
            }


        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY();
            downTime = System.currentTimeMillis();

        }
        return true;
    }

    public int getLongPressTimeout() {
        return ViewConfiguration.getLongPressTimeout();
    }

    public int getScaledTouchSlop() {
        return ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public interface OnClickViewPageIndicatorListener {
        public void onClickViewPageIndicator(int position);

        public void onLongPressViewPageIndicator(int position);

    }

}
