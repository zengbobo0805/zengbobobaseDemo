package com.zengbobobase.demo.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetUtils {
	private static String URL_PATH = "http://192.168.1.106:8080/green.jpg";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 调用方法获取图片并保存
		saveImageToDisk();
	}

	/**
	 * 通过URL_PATH的地址访问图片并保存到本地
	 */
	public static void saveImageToDisk() {
		InputStream inputStream = getInputStream();
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			// 把图片文件保存在本地F盘下
			fileOutputStream = new FileOutputStream("F:\\test.png");
			while ((len = inputStream.read(data)) != -1) {
				// 向本地文件中写入图片流
				fileOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 最后关闭流
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过URL获取图片
	 * 
	 * @return URL地址图片的输入流。
	 */
	public static InputStream getInputStream() {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;

		try {
			// 根据URL地址实例化一个URL对象，用于创建HttpURLConnection对象。
			URL url = new URL(URL_PATH);

			if (url != null) {
				// openConnection获得当前URL的连接
				httpURLConnection = (HttpURLConnection) url.openConnection();
				// 设置3秒的响应超时
				httpURLConnection.setConnectTimeout(3000);
				// 设置允许输入
				httpURLConnection.setDoInput(true);
				// 设置为GET方式请求数据
				httpURLConnection.setRequestMethod("GET");
				// 获取连接响应码，200为成功，如果为其他，均表示有问题
				int responseCode = httpURLConnection.getResponseCode();
				if (responseCode == 200) {
					// getInputStream获取服务端返回的数据流。
					inputStream = httpURLConnection.getInputStream();
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

}