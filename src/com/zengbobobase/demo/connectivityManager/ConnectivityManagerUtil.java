package com.zengbobobase.demo.connectivityManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityManagerUtil {
	// 判断是否有网络连接
	public static boolean isAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo[] infoList = connectivityManager.getAllNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	// 判断WIFI网络是否可用
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 判断MOBILE网络是否可用
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 获取当前网络连接的类型信息
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	//
	// 大家都知道cmwap连接需要设置代理地址和端口，那么，android程序中如何设置代理呢？这是个问题。
	// //当我们使用的是中国移动的手机网络时，下面方法可以直接获取得到10.0.0.172，80端口
	// String
	// host=android.net.Proxy.getDefaultHost();//通过andorid.net.Proxy可以获取默认的代理地址
	// int port
	// =android.net.Proxy.getDefaultPort();//通过andorid.net.Proxy可以获取默认的代理端口
	// SocketAddress sa=new InetSocketAddress(host,port);
	// //定义代理，此处的Proxy是源自java.net
	// Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,sa);
	// URL getUrl = new URL(“www.baidu.com”);
	// HttpURLConnection con = (HttpURLConnection)
	// getUrl.openConnection(proxy);//设置代理
	// HttpClient设置代理

	public static InputStream getHttpURLConnectionInputStream(Context context,
			String requestUrl, Map<String, String> param) {
		URL url;
		HttpURLConnection conn = null;
		InputStream input = null;
		try {
			url = new URL(requestUrl);
			// 当请求的网络为wap的时候，就需要添加中国移动代理
			if (getAPNType(context) == NetWorkUtil.CMWAP.value()) {
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172", 80));
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setConnectTimeout(10000); // 请求超时
			conn.setRequestMethod("POST"); // 请求方式
			conn.setReadTimeout(1000); // 读取超时
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStream os = conn.getOutputStream();
			StringBuilder sb = new StringBuilder();
			Iterator<String> it = param.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = param.get(key);
				sb.append(key).append("=").append(value).append("&");
			}
			String p = sb.toString().substring(0, sb.length() - 1);
			System.out.println("请求的参数" + p);
			os.write(p.getBytes("utf-8"));
			os.close();
			if (conn != null) {
				input = conn.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	// DefaultHttpClient httpClient=new DefaultHttpClient();
//	 String host=android.net.Proxy.getDefaultHost();//此处Proxy源自android.net
	// int port = Proxy.getPort(context);//同上
	// HttpHost httpHost = new HttpHost(host, port);
	// //设置代理
	// httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,httpHost);
	// HttpGet httpGet=new
	// HttpPost("<a href="http://www.baidu.com">www.baidu.com</a>");
	// HttpResponse response=httpClient.execute(httpGet);
	public static InputStream getHttpClientInputStream(Context context,
			String requestUrl, Map<String, String> param) throws Exception {
		HttpClient client = new DefaultHttpClient();
		// 当请求的网络为wap的时候，就需要添加中国移动代理
		if (getAPNType(context) == NetWorkUtil.CMWAP.value()) {
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		HttpPost hp = new HttpPost(requestUrl);
		hp.setHeader("Charset", "UTF-8");
		hp.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		Iterator<String> it = param.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			list.add(new BasicNameValuePair(key, param.get(key)));
		}
		hp.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
		HttpResponse response = null;
		response = client.execute(hp);
		return response.getEntity().getContent();
	}

	public static enum NetWorkUtil {
		CMNET(0), CMWAP(1), WIFI(2);

		private int value = -1;
		private NetWorkUtil(int value) { // 必须是private的，否则编译错误
			this.value = value;
		}

		public static NetWorkUtil valueOf(int value) { // 手写的从int到enum的转换函数
			switch (value) {
			case 0:
				return CMNET;
			case 1:
				return CMWAP;
			case 2:
				return WIFI;
			default:
				return null;
			}
		}

		public int value() {
			return this.value;
		}
	}

	/**
	 * 
	 * @author sky
	 * 
	 *         Email vipa1888@163.com
	 * 
	 *         QQ:840950105
	 * 
	 *         获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * 
	 * @return
	 */

	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}

		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Log.e("networkInfo.getExtraInfo()",
					"networkInfo.getExtraInfo() is "
							+ networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = NetWorkUtil.CMNET.value();
			} else {
				netType = NetWorkUtil.CMWAP.value();
				
			}
			
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NetWorkUtil.WIFI.value();
			
		}

		return netType;

	}

	public static NetworkInfo[] getAllNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infoList = connectivityManager.getAllNetworkInfo();
		return infoList;
	}
}
