package com.zengbobobase.demo.download.downloader;

/**
 * 下载进度监听接口
 */
public interface DownloadProgressListener {
	/**
	 *下载的进度 
	 */
	public void onDownloadUpdateSize(int size);
	
	public void onDownloadSuccess(int totalSize);

	public void onDownloadToast(String success);

	public void onDownloadFailure(String fail);
	
	public void onDownloadError(String fail);
	
}
