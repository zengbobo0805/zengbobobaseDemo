package com.zengbobobase.demo.download.downloader;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.zengbobobase.demo.download.dao.FileDownLoaderDao;

/**
 * 文件下载器
 */
public class FileDownloader implements DownloadProgressListener {
	private static final String TAG = "FileDownloader";
	/** 上下文 */
	private Context context;
	/** 文件下载服务类 */
	private FileDownLoaderDao fileService;
	/** 是否停止下载 */
	private boolean exit;
	/** 已下载文件长度 */
	private int downloadSize = 0;
	/** 原始文件长度 */
	private int fileSize = 0;
	/** 用于下载的线程数组 */
	private DownloadThread[] threads;
	/** 本地保存文件 */
	private File saveFile;
	/** 缓存各线程下载的长度 */
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
	/** 每条线程下载的长度 */
	private int block;
	/** 下载路径 */
	private String downloadUrl;
	private DownloadProgressListener listener = null;

	/**
	 * 获取线程数
	 */
	public int getThreadSize() {
		return threads.length;
	}

	/**
	 * 退出下载
	 */
	public void exit() {

		print("downloadSize:"+downloadSize+"   fileSize:"+fileSize);
		// 退出的时候保存下载进度，后面继续下载
		for (int i = 0, len = threads.length; i < len; i++) {
			if(threads[i]==null ){
				continue;
			}
			update(threads[i].getThreadId(), threads[i].getDownLength());
		}
		if(downloadSize==fileSize){
			onDownloadUpdateSize(0);
		}
		this.exit = true;
	}

	/**
	 * 是否退出下载
	 */
	public boolean getExit() {
		return this.exit;
	}

	/**
	 * 获取文件大小
	 */
	public int getFileSize() {
		return fileSize;
	}

	/**
	 * 累计已下载大小 该方法在具体某个线程下载的时候会被调用
	 */
	protected synchronized void append(int size, int threadId) {
		downloadSize += size;
		int buf = this.data.get(threadId);
		buf += size;
		this.data.put(threadId, buf);
	}

	/**
	 * 更新指定线程最后下载的位置 该方法在具体某个线程下载的时候会被调用
	 * 
	 * @param threadId
	 *            线程id
	 * @param pos
	 *            最后下载的位置
	 */
	protected synchronized void update(int threadId, int pos) {
		print("threadId:"+threadId+"   pos:"+pos);
		// 缓存各线程下载的长度
		this.data.put(threadId, pos);
		// 更新数据库中的各线程下载的长度
		this.fileService.update(this.downloadUrl, threadId, pos);
	}

	/**
	 * 构建文件下载器
	 * 
	 * @param downloadUrl
	 *            下载路径
	 * @param fileSaveDir
	 *            文件保存目录
	 * @param threadNum
	 *            下载线程数
	 */
	public FileDownloader(Context context, String downloadUrl,
			File fileSaveDir, int threadNum) {
		
		try {
			this.context = context;
			this.downloadUrl = downloadUrl;
			fileService = new FileDownLoaderDao(this.context);
			// 根据指定的下载路径，生成URL
			URL url = new URL(this.downloadUrl);
			if (!fileSaveDir.exists())
				fileSaveDir.mkdirs();// 如果保存路径不存在，则新建一个目录
			// 根据指定的线程数来新建线程数组
			this.threads = new DownloadThread[threadNum];
			// 打开HttpURLConnection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置 HttpURLConnection的断开时间
			conn.setConnectTimeout(5 * 1000);
			// 设置 HttpURLConnection的请求方式
			conn.setRequestMethod("GET");
			// 设置 HttpURLConnection的接收的文件类型
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
							+ "application/x-shockwave-flash, application/xaml+xml, "
							+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "
							+ "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			// 设置 HttpURLConnection的接收语音
			conn.setRequestProperty("Accept-Language", "zh-CN");
			// 指定请求uri的源资源地址
			conn.setRequestProperty("Referer", downloadUrl);
			// 设置 HttpURLConnection的字符编码
			conn.setRequestProperty("Charset", "UTF-8");
			// 检查浏览页面的访问者在用什么操作系统（包括版本号）浏览器（包括版本号）和用户个人偏好
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2;"
							+ " Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; "
							+ ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152;"
							+ " .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			// 打印Http协议头
			printResponseHeader(conn);
			print("conn.getContent()：" + conn.getContent());
			print("conn.getContentType()：" + conn.getContentType());
			print("conn.getResponseMessage()：" + conn.getResponseMessage());
			print("conn.getDate()：" + new Date(conn.getDate()));
			
			// 如果返回的状态码为200表示正常
			// System.out.println("conn.getResponseCode()："+conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				this.fileSize = conn.getContentLength();// 根据响应获取文件大小
				print("已经文件总长度：" + this.fileSize);
				if (this.fileSize <= 0)
					throw new RuntimeException("Unkown file size ");

				String filename = getFileName(conn);// 获取文件名称
				this.saveFile = new File(fileSaveDir, filename);// 构建保存文件
				Map<Integer, Integer> logdata = fileService
						.getData(downloadUrl);// 获取下载记录
				if (logdata.size() > 0) {// 如果存在下载记录
					for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
						data.put(entry.getKey(), entry.getValue());// 把各条线程已经下载的数据长度放入data中
				}

				if (this.data.size() == this.threads.length) {// 下面计算所有线程已经下载的数据总长度
					for (int i = 0; i < this.threads.length; i++) {
						this.downloadSize += this.data.get(i + 1);
					}
				}
				print("保存数据条数：" + this.data.size());
				print("打算开启线程数：" + this.threads.length);
				print("已经下载的长度：" + this.downloadSize);
				// 计算每条线程下载的数据长度
				this.block = (this.fileSize % this.threads.length) == 0 ? this.fileSize
						/ this.threads.length
						: this.fileSize / this.threads.length + 1;

				print("每个线程下载文件大小：" + this.block);
			} else {
				throw new RuntimeException("server no response ");
			}
		} catch (Exception e) {
			print(e.toString());
			throw new RuntimeException("don't connection this url");
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param conn
	 *            Http连接
	 */
	private String getFileName(HttpURLConnection conn) {
		String filename = this.downloadUrl.substring(this.downloadUrl
				.lastIndexOf('/') + 1);// 截取下载路径中的文件名
		// 如果获取不到文件名称
		if (filename == null || "".equals(filename.trim())) {
			// 通过截取Http协议头分析下载的文件名
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);
				if (mine == null)
					break;
				/**
				 * Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME
				 * 用户代理如何显示附加的文件。
				 * Content-Disposition就是当用户想把请求所得的内容存为一个文件的时候提供一个默认的文件名
				 * 协议头中的Content-Disposition格式如下：
				 * Content-Disposition","attachment;filename=FileName.txt");
				 */
				if ("content-disposition".equals(conn.getHeaderFieldKey(i)
						.toLowerCase())) {
					// 通过正则表达式匹配出文件名
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(
							mine.toLowerCase());
					// 如果匹配到了文件名
					if (m.find())
						return m.group(1);// 返回匹配到的文件名
				}
			}
			// 如果还是匹配不到文件名，则默认取一个随机数文件名
			filename = UUID.randomUUID() + ".tmp";
		}
		return filename;
	}

	/**
	 * 开始下载文件
	 * 
	 * @param listener
	 *            监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
	 * @return 已下载文件大小
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception {
		this.listener = listener;
		try {
			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
			if (this.fileSize > 0)
				randOut.setLength(this.fileSize);
			randOut.close();
			URL url = new URL(this.downloadUrl);
			// 如果原先未曾下载或者原先的下载线程数与现在的线程数不一致
			// TODO zengbobo 因为每个线程下载大小=文件总大小/线程数；
			if (this.data.size() != this.threads.length) {
				this.data.clear();// 清除原来的线程数组
				for (int i = 0; i < this.threads.length; i++) {
					this.data.put(i + 1, 0);// 初始化每条线程已经下载的数据长度为0
				}
				this.downloadSize = 0;
			}

			boolean notFinish = false;// 下载未完成
			// 循环遍历线程数组
			for (int i = 0; i < this.threads.length; i++) {
				int downLength = this.data.get(i + 1); // 获取当前线程下载的文件长度
				// 判断线程是否已经完成下载,否则继续下载
				if (downLength < this.block
						&& this.downloadSize < this.fileSize) {
					// 启动线程开始下载
					this.threads[i] = new DownloadThread(this, url,
							this.saveFile, this.block, this.data.get(i + 1),
							i + 1);
					 this.threads[i].setPriority(7);
					 this.threads[i].start();
					 onDownloadToast("开启线程:"+i);
					notFinish = true;
				} else {
					this.threads[i] = null;
				}
			}
			// 如果存在下载记录，从数据库中删除它们
			fileService.delete(this.downloadUrl);
			// 重新保存下载的进度到数据库
			fileService.save(this.downloadUrl, this.data);
			while (notFinish) {// 循环判断所有线程是否完成下载
				Thread.sleep(900);
				notFinish = false;
				for (int i = 0; i < this.threads.length; i++) {
					if (this.threads[i] != null && !this.threads[i].isFinish()) {// 如果发现线程未完成下载
						notFinish = true;// 设置标志为下载没有完成
						// 如果下载失败,再重新下载
						if (this.threads[i].getDownLength() == -1) {
							this.threads[i] = new DownloadThread(this, url,
									this.saveFile, this.block,
									this.data.get(i + 1), i + 1);
							this.threads[i].setPriority(7);
							this.threads[i].start();
							 onDownloadToast("重启线程:"+i);
						}
					}
				}
				onDownloadUpdateSize(this.downloadSize);// 通知目前已经下载完成的数据长度
			}
			// 如果下载完成

			print("如果下载完成downloadSize:"+downloadSize+"  fileSize:"+fileSize);
			if (downloadSize == this.fileSize) {
				onDownloadSuccess(this.fileSize);
				fileService.delete(this.downloadUrl);// 下载完成删除记录
			}
		} catch (Exception e) {
			 onDownloadError("下载出错："+e.toString());
			print(e.toString());
			throw new Exception("file download error");
		}
		return this.downloadSize;
	}

	/**
	 * 获取Http响应头字段
	 * 
	 * @param http
	 * @return
	 */
	public static Map<String, String> getHttpResponseHeader(
			HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}

	/**
	 * 打印Http头字段
	 * 
	 * @param http
	 */
	public static void printResponseHeader(HttpURLConnection http) {
		Map<String, String> header = getHttpResponseHeader(http);
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() + ":" : "";
			print(key + entry.getValue());
		}
		
	}

	/**
	 * 打印信息
	 * 
	 * @param msg
	 *            信息
	 */
	private static void print(String msg) {
		Log.i(TAG, msg);
	}

	@Override
	public void onDownloadUpdateSize(int size) {
		if (listener != null)
			listener.onDownloadUpdateSize(size);// 通知目前已经下载完成的数据长度
	}

	@Override
	public void onDownloadSuccess(int totalSize) {
		if (listener != null)
			listener.onDownloadSuccess(totalSize);// 通知目前已经下载完成的数据长度
	}

	@Override
	public void onDownloadFailure(String fail) {
		fileService.delete(this.downloadUrl);
		fileService.save(this.downloadUrl, data);
		if (listener != null)
			listener.onDownloadFailure(fail);// 通知目前已经下载完成的数据长度
	}

	@Override
	public void onDownloadError(String fail) {
		fileService.delete(this.downloadUrl);
		fileService.save(this.downloadUrl, data);
		if (listener != null)
			listener.onDownloadError(fail);// 通知目前已经下载完成的数据长度
	}

	@Override
	public void onDownloadToast(String success) {
		if (listener != null)
			listener.onDownloadToast(success);// 通知目前已经下载完成的数据长度
	}
	
	
	private void disableConnectionReuseIfNecessary() {
	    // 这是一个2.2版本之前的bug
	    if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
}
