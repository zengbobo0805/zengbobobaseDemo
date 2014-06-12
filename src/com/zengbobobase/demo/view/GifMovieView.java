package com.zengbobobase.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.zengbobobase.demo.R;

/**
 * This is a View class that wraps Android {@link Movie} object and displays it.
 * You can set GIF as a Movie object or as a resource id from XML or by calling
 * {@link #setMovie(Movie)} or {@link #setMovieResource(int)}.
 * <p>
 * You can pause and resume GIF animation by calling {@link #setPaused(boolean)}.
 * <p>
 * The animation is drawn in the center inside of the measured view bounds.
 * 
 * @author Sergey Bakhtiarov
 */

public class GifMovieView extends View {

	private static final int DEFAULT_MOVIEW_DURATION = 1000;

	private int mMovieResourceId;
	private Movie mMovie;

	private long mMovieStart;
	private int mCurrentAnimationTime = 0;
	
	/**
	 * Position for drawing animation frames in the center of the view.
	 */
	private float mLeft;
	private float mTop;

	/**
	 * Scaling factor to fit the animation within view bounds.
	 */
	private float mScale;

	/**
	 * Scaled movie frames width and height.
	 */
	private int mMeasuredMovieWidth;
	private int mMeasuredMovieHeight;

	private volatile boolean mPaused = false;
	private boolean mVisible = true;

	public GifMovieView(Context context) {
		this(context, null);
	}

	public GifMovieView(Context context, AttributeSet attrs) {
		this(context, attrs, R.styleable.CustomTheme_gifMoviewViewStyle);
	}

	public GifMovieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setViewAttributes(context, attrs, defStyle);
	}

	@SuppressLint("NewApi")
	private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

		/**
		 * Starting from HONEYCOMB have to turn off HW acceleration to draw
		 * Movie on Canvas.
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifMoviewView, defStyle,
				R.style.Widget_GifMoviewView);

		mMovieResourceId = array.getResourceId(R.styleable.GifMoviewView_gif, -1);
		mPaused = array.getBoolean(R.styleable.GifMoviewView_paused, false);

		array.recycle();

		if (mMovieResourceId != -1) {
			mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
		}
	}

	@Override
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		super.setBackgroundColor(color);
	}

	@Override
	public void setBackgroundResource(int resid) {
		// TODO Auto-generated method stub
		super.setBackgroundResource(resid);
	}

	@Override
	public void setBackground(Drawable background) {
		// TODO Auto-generated method stub
		super.setBackground(background);
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background) {
		// TODO Auto-generated method stub
		super.setBackgroundDrawable(background);
	}

	public void setMovieResource(int movieResId) {
		this.mMovieResourceId = movieResId;
		mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
		requestLayout();
	}

	public void setMovie(Movie movie) {
		this.mMovie = movie;
		requestLayout();
	}

	public Movie getMovie() {
		return mMovie;
	}

	public void setMovieTime(int time) {
		mCurrentAnimationTime = time;
		invalidate();
	}

	public void setPaused(boolean paused) {
		this.mPaused = paused;

		/**
		 * Calculate new movie start time, so that it resumes from the same
		 * frame.
		 */
		if (!paused) {
			mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
		}

		invalidate();
	}

	public boolean isPaused() {
		return this.mPaused;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("GifMoviewView onMeasure mMovie:"+mMovie);
		if (mMovie != null) {
			int movieWidth = mMovie.width();
			int movieHeight = mMovie.height();

			System.out.println("GifMoviewView onMeasure movieWidth:"+movieWidth+",movieHeight:"+movieHeight);
			float scaleH = 1f;
			int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
			System.out.println("GifMoviewView onMeasure measureModeWidth:"+measureModeWidth);
			if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
				int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
				System.out.println("GifMoviewView onMeasure maximumWidth:"+maximumWidth);
					scaleH = (float)maximumWidth  / (float)movieWidth;
			}


			mScale=scaleH;
			mMeasuredMovieWidth = (int) (movieWidth * mScale);
			mMeasuredMovieHeight = (int) (movieHeight * mScale);
			System.out.println("GifMoviewView onMeasure scaleH:"+scaleH+",mMeasuredMovieWidth:"+mMeasuredMovieWidth+",mMeasuredMovieHeight:"+mMeasuredMovieHeight);
			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

		} else {
			setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		System.out.println("GifMoviewView onLayout l:"+l+",t:"+t+",r:"+r+",b:"+b);
		/*
		 * Calculate left / top for drawing in center
		 */
		mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
		mTop = (getHeight() - mMeasuredMovieHeight) / 2f;
		
		mVisible = getVisibility() == View.VISIBLE;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		System.out.println("GifMoviewView onDraw mMovie:"+mMovie);
		System.out.println("GifMoviewView onDraw mPaused:"+mPaused);
		if (mMovie != null) {
			if (!mPaused) {
				updateAnimationTime();
				drawMovieFrame(canvas);
				invalidateView();
			} else {
				drawMovieFrame(canvas);
			}
		}
	}
	
	/**
	 * Invalidates view only if it is visible.
	 * <br>
	 * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
	 * 
	 */
	@SuppressLint("NewApi")
	private void invalidateView() {
		if(mVisible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
		}
	}

	/**
	 * Calculate current animation time
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();

		if (mMovieStart == 0) {
			mMovieStart = now;
		}

		int dur = mMovie.duration();

		if (dur == 0) {
			dur = DEFAULT_MOVIEW_DURATION;
		}

		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
	}

	/**
	 * Draw current GIF frame
	 */
	private void drawMovieFrame(Canvas canvas) {

		mMovie.setTime(mCurrentAnimationTime);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
		canvas.restore();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		mVisible = screenState == SCREEN_STATE_ON;
		invalidateView();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}
}
