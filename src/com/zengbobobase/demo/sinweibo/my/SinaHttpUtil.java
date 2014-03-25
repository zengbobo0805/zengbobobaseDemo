package com.zengbobobase.demo.sinweibo.my;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.zengbobo.android.utils.StringUtil;
import com.zengbobobase.demo.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class SinaHttpUtil {
	private static SinaHttpUtil singInstance = new SinaHttpUtil();

	private SinaHttpUtil() {
	}

	public static SinaHttpUtil getInstance() {
		return singInstance;

	}

	// public static final String BOUNDARY = "7cd4a6d158c";
	// public static final String MP_BOUNDARY = "--" + BOUNDARY;
	// public static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
	// public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
																			// 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	public void post(String path, Map<String, String> map,
			ResponseHandler responseHandler) {
		Set<Entry<String, String>> entryMap = map.entrySet();
		Iterator<Entry<String, String>> it = entryMap.iterator();
		StringBuffer buf = new StringBuffer();
		String pic = "";
		while (it.hasNext()) {
			Entry<String, String> it_entry = it.next();
			String key = it_entry.getKey();
			String values = it_entry.getValue();
			if (key.equals("pic")) {
				pic = values;
				continue;
			}
			buf.append("&");
			buf.append(key);
			buf.append("=");
			buf.append(values);
		}
		System.out.println("post path:" + path + buf.toString());
		ByteArrayOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");

			byte[] mydata = buf.toString().getBytes("UTF-8");
			// 设置请求报文头，设定请求数据类型
			OutputStream outputStream_params = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (!StringUtil.equalsNullOrEmpty(pic)) {
				// 设置请求数据长度bos= new ByteArrayOutputStream();
				httpURLConnection.setRequestProperty("Content-Type",
						CONTENT_TYPE + ";boundary=" + BOUNDARY);
				bos.write(mydata);
				bos.write("&pic=".getBytes("UTF-8"));
				Bitmap bm = BitmapFactory.decodeFile(pic);
				bm.compress(CompressFormat.PNG, 75, bos);

			} else {
				httpURLConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				httpURLConnection.setRequestProperty("Content-Length",
						String.valueOf(mydata.length));
				bos.write(mydata);
			}
			outputStream_params = httpURLConnection.getOutputStream();
			outputStream_params.write(bos.toByteArray());

			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == 200) {

				inputStream = httpURLConnection.getInputStream();
				int len = -1;
				outputStream = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				while ((len = inputStream.read(data)) != -1) {
					// 向本地文件中写入图片流
					outputStream.write(data, 0, len);
				}

				responseHandler
						.onSuccess(new String(outputStream.toByteArray()));
			} else {
				responseHandler.onError("请求错误responseCode:" + responseCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			responseHandler.onError(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			responseHandler.onError(e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void get(String path, Map<String, String> map,
			ResponseHandler responseHandler) {
		// http://www.lvmama.com/?code=cd4aaeeb505491dbbcea1da4a23698c9
		Set<Entry<String, String>> entryMap = map.entrySet();
		Iterator<Entry<String, String>> it = entryMap.iterator();
		StringBuffer buf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> it_entry = it.next();
			String key = it_entry.getKey();
			String values = it_entry.getValue();
			buf.append("&");
			buf.append(key);
			buf.append("=");
			buf.append(values);
		}

		System.out.println("post path:" + path + buf.toString());
		ByteArrayOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(path + buf.toString());
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(false);
			httpURLConnection.setRequestMethod("GET");

			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == 200) {

				outputStream = new ByteArrayOutputStream();
				inputStream = httpURLConnection.getInputStream();
				int len = -1;
				byte[] data = new byte[1024];
				while ((len = inputStream.read(data)) != -1) {
					// 向本地文件中写入图片流
					outputStream.write(data, 0, len);
				}
				responseHandler
						.onSuccess(new String(outputStream.toByteArray()));
			} else {
				responseHandler.onError("请求错误!responseCode:" + responseCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			responseHandler.onError(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			responseHandler.onError(e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void toUploadFile(File file, String fileKey,
			String RequestURL, Map<String, String> param,
			ResponseHandler responseHandler) {

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setReadTimeout(30000);
			 conn.setConnectTimeout(30000);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
//			conn.setRequestProperty("Charset", "UTF-8"); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// TODO ZENGBOBO CHANGE
			if (file != null && file.exists()) {
				conn.setRequestProperty("Content-Type", CONTENT_TYPE
						+ ";boundary=" + BOUNDARY);
			} else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
			}

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";

			// TODO ZENGBOBO CHANGE
			if (file != null && file.exists()) {

				/***
				 * 以下是用于上传参数
				 */
				if (param != null && param.size() > 0) {
					Iterator<String> it = param.keySet().iterator();
					while (it.hasNext()) {
						sb = null;
						sb = new StringBuffer();
						String key = it.next();
						String value = param.get(key);
						sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
						sb.append("Content-Disposition: form-data; name=\"")
								.append(key).append("\"").append(LINE_END)
								.append(LINE_END);
						sb.append(value).append(LINE_END);
						params = sb.toString();
						dos.write(params.getBytes());
					}
				}
				sb = null;
				params = null;
				sb = new StringBuffer();
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition:form-data; name=\"" + fileKey
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的
																	// ，用于服务器端辨别文件的类型的
				sb.append(LINE_END);
				params = sb.toString();
				sb = null;

				dos.write(params.getBytes());
				/** 上传文件 */
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();

				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
			} else {

				/***
				 * 以下是用于上传参数
				 */
				if (param != null && param.size() > 0) {
					Iterator<String> it = param.keySet().iterator();
					while (it.hasNext()) {
						sb = null;
						sb = new StringBuffer();
						String key = it.next();
						String value = param.get(key);
						sb.append("&");
						sb.append(key);
						sb.append("=");
						sb.append(value);
						params = sb.toString();
						dos.write(params.getBytes("UTF-8"));
					}
				}
			}
			dos.flush();
			//
			// dos.write(tempOutputStream.toByteArray());
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			if (res == 200) {
				InputStream input = conn.getInputStream();
//				StringBuffer sb1 = new StringBuffer();
//				int ss;
//				while ((ss = input.read()) != -1) {
//					sb1.append((char) ss);
//				}
				int len = -1;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				while ((len = input.read(data)) != -1) {
					// 向本地文件中写入图片流
					outputStream.write(data, 0, len);
				}
				responseHandler.onSuccess(new String(outputStream.toByteArray()));
				return;
			} else {
				responseHandler.onError("错误getResponseCode：" + res);
				return;
			}
		} catch (MalformedURLException e) {
			responseHandler.onError("错误：" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			responseHandler.onError("错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
}
