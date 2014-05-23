package com.zengbobobase.demo.utils;

import java.io.File;

import com.zengbobobase.demo.MyApplication;

import android.os.Environment;

public class FileUtils {
	
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		} else {
			return null;
		}
	}
	
	public static String getPicCachePath() {
	
		String dir = null;
		String sdcard = getSDPath();
		if (sdcard != null) {
			dir = sdcard;
		} else {
			File cacheDir = MyApplication.getInstance().getCacheDir();
			dir = cacheDir.getAbsolutePath();
		}
		if (dir != null) {
			if (dir.endsWith(File.separator)) {
				dir = dir + "zengbobobasedemo/imagecachedir" + File.separator;
			} else {
				dir = dir + File.separator + "zengbobobasedemo/imagecachedir"
						+ File.separator;
			}
		}
		return dir;
	}
}
