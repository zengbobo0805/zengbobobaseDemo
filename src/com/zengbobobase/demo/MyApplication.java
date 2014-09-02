package com.zengbobobase.demo;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zengbobobase.demo.utils.FileUtils;

public class MyApplication extends Application {
	private static MyApplication instance;
	public ImageLoaderConfiguration config ;
	public static MyApplication getInstance(){
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		new TextView(this);
		init(getApplicationContext());
//		FrontiaApplication.initFrontiaApplication(getApplicationContext());
	}
	public void init(Context context) {
		 config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1024 * 1024 * 5)
				// 1.5 Mb
				.defaultDisplayImageOptions(
						new DisplayImageOptions.Builder().cacheOnDisc()
								.cacheInMemory().build())
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				.denyCacheImageMultipleSizesInMemory()
				
				.discCache(
						new TotalSizeLimitedDiscCache(new File(FileUtils
								.getPicCachePath()),
								new Md5FileNameGenerator(), 50 * 1024 * 1024))
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


}
