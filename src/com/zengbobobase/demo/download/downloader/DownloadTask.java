package com.zengbobobase.demo.download.downloader;

import java.io.File;

import com.zengbobobase.demo.utils.Contast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
 * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
 * 借用Handler来传送UI控件的值到主线程去，在主线程更新UI控件的值
 */
public class DownloadTask implements Runnable {
	/** 下载路径 */
	private String path;
	/** 保存路径 */
	private File saveDir;
	/** 文件下载器 */
	private FileDownloader loader;
	private Handler handler;
	private Context mContext;

	/**
	 * DownloadTask的构造函数
	 * 
	 * @param path
	 *            下载路径
	 * @param saveDir
	 *            保存路径
	 */
	public DownloadTask(Context mContext, String path, File saveDir,
			Handler handler) {
		this.path = path;
		this.saveDir = saveDir;
		this.handler = handler;
		this.mContext = mContext;
	}

	/**
	 * 线程主方法
	 */
	public void run() {
		try {
			/**
			 * 构建文件下载器 将下载路径，文件保存目录，下载线程数指定好
			 */
			loader = new FileDownloader(mContext, path, saveDir, 5);
			sendMessage(Contast.TOTAL_SIZE, loader.getFileSize());// 设置进度条的最大刻度（即文件的总长度）
			/**
			 * DownloadProgressListener是一个接口，onDownloadSize()为未实现的方法。
			 * onDownloadSize()方法会在download方法内部被动态赋值
			 * 监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
			 */
			loader.download(new DownloadProgressListener() {

				@Override
				public void onDownloadUpdateSize(int size) {
					// 借用Handler来传送UI控件的值到主线程去，在主线程更新UI控件的值
					// 对应UIHander 获得的msg.what
					// 将获取的值发送给handler，用于动态更新进度
					sendMessage(Contast.UPDATE_SIZE, size);

				}

				@Override
				public void onDownloadSuccess(int totalSize) {
					sendMessage(Contast.SUCCESS_SIZE, totalSize);

				}

				@Override
				public void onDownloadFailure(String fail) {
					sendMessage(Contast.FAILURE_SIZE, fail);

				}

				@Override
				public void onDownloadError(String error) {
					sendMessage(Contast.ERROR_SIZE, error);
				}

				@Override
				public void onDownloadToast(String success) {
					sendMessage(Contast.TOAST_SIZE, success);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			// 对应UIHander 获得的msg.what
			sendMessage(Contast.ERROR_SIZE, -1);
		}
	}

	private void sendMessage(int what, Object obj) {
		if (handler == null) {
			return;
		}

		Message msg = handler.obtainMessage(what, obj);
		handler.sendMessage(msg);
	}

	/**
	 * 退出下载
	 */
	public void exit() {
		if (loader != null)
			loader.exit();
	}
}