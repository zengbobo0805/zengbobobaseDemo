package com.zengbobobase.demo.download.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.util.Log;

public class DownloadThread extends Thread {
	private static final String TAG = "DownloadThread";
	/** 本地保存文件 */
	private File saveFile;
	/** 下载路径 */
	private URL downUrl;
	/** 该线程要下载的长度 */
	private int block;
	/** 线程ID */
	private int threadId = -1;
	/** 该线程已经下载的长度 */
	private int downLength;
	/** 是否下载完成 */
	private boolean finish = false;
	/** 文件下载器 */
	private FileDownloader downloader;

	/***
	 * 构造方法
	 */
	public DownloadThread(FileDownloader downloader, URL downUrl,
			File saveFile, int block, int downLength, int threadId) {
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downLength = downLength;
	}

	/**
	 * 线程主方法
	 */
	@Override
	public void run() {
		if (downLength < block) {// 未下载完成
			try {
				HttpURLConnection http = (HttpURLConnection) downUrl
						.openConnection();
				http.setConnectTimeout(5 * 1000);
				http.setRequestMethod("GET");
				http.setRequestProperty(
						"Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash,"
								+ " application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, "
								+ "application/x-ms-application, application/vnd.ms-excel,"
								+ " application/vnd.ms-powerpoint, application/msword, */*");
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer", downUrl.toString());
				http.setRequestProperty("Charset", "UTF-8");
				// 该线程开始下载位置
				int startPos = block * (threadId - 1) + downLength;
				// 该线程下载结束位置
				int endPos = block * threadId - 1;
				// 设置获取实体数据的范围
				http.setRequestProperty("Range", "bytes=" + startPos + "-"
						+ endPos);
				http.setRequestProperty(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0;"
								+ " .NET CLR 1.1.4322; .NET CLR 2.0.50727; "
								+ ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				http.setRequestProperty("Connection", "Keep-Alive");
				/****/
				System.out.println("DownloadThread http.getResponseCode()："
						+ http.getResponseCode());
				if (http.getResponseCode() == 206) {
					/***
					 * //获取输入流 InputStream inStream = http.getInputStream();
					 * byte[] buffer = new byte[1024]; int offset = 0;
					 * print("Thread " + this.threadId +
					 * " start download from position " + startPos);
					 * 
					 * // rwd: 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到基础存储设备。
					 * //对于Android移动设备一定要注意同步，否则当移动设备断电的话会丢失数据 RandomAccessFile
					 * threadfile = new RandomAccessFile( this.saveFile, "rwd");
					 * //直接移动到文件开始位置下载的 threadfile.seek(startPos); while
					 * (!downloader.getExit() && (offset = inStream.read(buffer,
					 * 0, 1024)) != -1) { threadfile.write(buffer, 0,
					 * offset);//开始写入数据到文件 downLength += offset; //该线程以及下载的长度增加
					 * downloader.update(this.threadId,
					 * downLength);//修改数据库中该线程已经下载的数据长度
					 * downloader.append(offset);//文件下载器已经下载的总长度增加 }
					 * threadfile.close();
					 * 
					 * print("Thread " + this.threadId + " download finish");
					 * this.finish = true;
					 **/
					
					// 获取输入流
					InputStream inStream = http.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(inStream);
					byte[] buffer = new byte[1024 * 8];
					int offset = 0;
					RandomAccessFile threadfile = new RandomAccessFile(
							this.saveFile, "rwd");
					// 获取RandomAccessFile的FileChannel
					FileChannel outFileChannel = threadfile.getChannel();
					// 直接移动到文件开始位置下载的
					outFileChannel.position(startPos);
					// 分配缓冲区的大小
					while (!downloader.getExit()
							&& (offset = bis.read(buffer)) != -1) {
						
						outFileChannel
								.write(ByteBuffer.wrap(buffer, 0, offset));// 开始写入数据到文件
						downLength += offset; // 该线程以及下载的长度增加
						// 更新数据库的位置需要换其他地方,会占用很多时间
						// downloader.update(this.threadId, downLength);
						// 修改数据库中该线程已经下载的数据长度
						downloader.append(offset, threadId);// 文件下载器已经下载的总长度增加
						print("Thread " + this.threadId + " write:" + offset);
					}
					outFileChannel.force(true);
					outFileChannel.close();
					threadfile.close();
					inStream.close();
					print("Thread " + this.threadId + " download finish");
					this.finish = true;
					downloader.onDownloadToast("Thread " + this.threadId
							+ " download finish");
				}
			} catch (Exception e) {
				// 发生异常时，保存下载进度，后面继续下载
				downloader.update(this.threadId, downLength);// 修改数据库中该线程已经下载的数据长度
				this.downLength = -1;
				print("Thread " + this.threadId + ":" + e);
				downloader.onDownloadError("Thread " + this.threadId + ":" + e);
			}
		}
	}

	private static void print(String msg) {
		Log.i(TAG, msg);
	}

	/**
	 * 下载是否完成
	 * 
	 * @return
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * 已经下载的内容大小
	 * 
	 * @return 如果返回值为-1,代表下载失败
	 */
	public int getDownLength() {
		return downLength;
	}

	public int getThreadId() {
		return threadId;
	}
}
