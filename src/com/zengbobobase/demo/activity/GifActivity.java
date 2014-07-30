package com.zengbobobase.demo.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.zengbobobase.demo.MyApplication;
import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.FileUtils;
import com.zengbobobase.demo.view.GifMovieView;

public class GifActivity extends Activity {
	private String path = "http://ww1.sinaimg.cn/bmiddle/56a92a6cjw1djwrliune3g.gif";
//	private String path="http://img1.xcarimg.com/b55/s1450/m_lw3tksgasy8111.JPG";
	GifMovieView gif1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gif_layout);

		gif1 = (GifMovieView) findViewById(R.id.gif1);
		// gif1.setMovieResource(R.drawable.downlaod_my);

		ImageLoader.getInstance().loadImage(path, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				System.out.println("GifActivity onLoadingStarted arg0:" + arg0);
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				System.out.println("GifActivity onLoadingFailed arg0:" + arg0);

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				System.out
						.println("GifActivity onLoadingComplete arg0:" + arg0);

				String fileName = new Md5FileNameGenerator().generate(path);
				String filePath = FileUtils.getPicCachePath() + fileName;

				System.out.println("GifActivity onLoadingComplete filePath:"
						+ filePath);
//				InputStream is = null;
//				try {
//					is = new BufferedInputStream(new FileInputStream(new File(
//							filePath)), 16 * 1024);
//					is.mark(16 * 1024);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				Movie movie = Movie.decodeStream(is);
//				handler.sendMessage(handler.obtainMessage(0x0001, movie));
				readGif(filePath, 0x0001);
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				System.out.println("GifActivity onLoadingCancelled arg0:"
						+ arg0);

			}
		});
	}

	private void readGif(String filePath, int what) {
		byte[] buffer = new byte[1024];
		int len;
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			while ((len = fis.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
			byte[] array = os.toByteArray();
			Movie movie = Movie.decodeByteArray(array, 0, array.length);
			handler.sendMessage(handler.obtainMessage(what, movie));
		} catch (java.io.IOException e) {
		}
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x0001) {
				System.out.println("GifActivity handler handleMessage:");
				gif1.setMovie((Movie) msg.obj);
//				gif1.setBackgroundResource(R.drawable.bg_btn);
//				gif1.setBackgroundDrawable(GifActivity.this.getResources().getDrawable(R.drawable.bg_btn));
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_gif, menu);
		return true;
	}

	public void onGifClick(View v) {
		GifMovieView gif = (GifMovieView) v;
		gif.setPaused(!gif.isPaused());
	}

}
