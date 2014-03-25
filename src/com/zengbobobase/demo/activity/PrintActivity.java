package com.zengbobobase.demo.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import android.app.Activity;
import android.os.Bundle;

public class PrintActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		printURL();

	}

	private void printURL() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL("http://www.lvmama.com");
					System.out.println("url.getAuthority():"
							+ url.getAuthority());
					System.out.println("url.getDefaultPort():"
							+ url.getDefaultPort());
					System.out.println("url.getFile():" + url.getFile());
					System.out.println("url.getHost():" + url.getHost());
					System.out.println("url.getPath():" + url.getPath());
					System.out.println("url.getPort():" + url.getPort());
					System.out.println("url.getProtocol():" + url.getProtocol());
					System.out.println("url.getQuery():" + url.getQuery());
					System.out.println("url.getRef():" + url.getRef());
					System.out.println("url.getUserInfo():" + url.getUserInfo());
					System.out.println("url.getContent():" + url.getContent());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	// ClassLoader contextClassLoader =
	// Thread.currentThread().getContextClassLoader();
	// if (packageList != null && contextClassLoader != null) {
	// for (String packageName : packageList.split("\\|")) {
	// String className = packageName + "." + protocol + ".Handler";
	// try {
	// Class<?> c = contextClassLoader.loadClass(className);
	// streamHandler = (URLStreamHandler) c.newInstance();
	// if (streamHandler != null) {
	// streamHandlers.put(protocol, streamHandler);
	// }
	// return;
	// } catch (IllegalAccessException ignored) {
	// } catch (InstantiationException ignored) {
	// } catch (ClassNotFoundException ignored) {
	// }
	// }

}
