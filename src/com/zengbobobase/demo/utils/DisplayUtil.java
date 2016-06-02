package com.zengbobobase.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtil {
	private static int scrrentWidth = 0;
	private static int scrrentHeight = 0;

	public static int getScrentWidth(Context context) {
		if (scrrentWidth <= 0) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			scrrentWidth = dm.widthPixels;
		}
		return scrrentWidth;
	}
	public static int getScrentHeight(Context context) {
		if (scrrentHeight <= 0) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			scrrentHeight = dm.heightPixels;
		}
		return scrrentHeight;
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static  int dip2px(Context context, int dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	public static  float density(Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return scale;
	}

}
