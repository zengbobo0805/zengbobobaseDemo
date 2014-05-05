package com.zengbobobase.demo.activity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.MD5;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class MD5Activity extends Activity {
	private ImageView img;
	private final static int ALIPAY_APP_STATE = 1;
	private int state =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_md5_layout);
		initState(ALIPAY_APP_STATE);
		System.out.println("testcontextandclass boolean:"+(state == ALIPAY_APP_STATE));
		img = (ImageView) findViewById(R.id.img);
		startOtherApplicationClass();
		String apkPath = getApkPath();

		String apkMD5 = getApkSignatureMD5(apkPath);

		System.out.println("testcontextandclass apkMD5:" + apkMD5);

	}

	private void initState(int num){
		state = num;
	}
	
	public String getApkPath() {
		String path = getApplicationContext().getApplicationInfo().publicSourceDir;
		System.out.println("com.example.testcontextandclass path:" + path);
		return path;
	}

	// 得到任意apk公钥信息的md5字符串
	public String getApkSignatureMD5(String apkPath) {
		try {
			Class clazz = Class.forName("android.content.pm.PackageParser");
			Method parsePackageMethod = clazz.getMethod("parsePackage",
					File.class, String.class, DisplayMetrics.class, int.class);

			Object packageParser = clazz.getConstructor(String.class)
					.newInstance("");
			Object packag = parsePackageMethod.invoke(packageParser, new File(
					apkPath), null, getResources().getDisplayMetrics(), 0x0004);

			Method collectCertificatesMethod = clazz.getMethod(
					"collectCertificates",
					Class.forName("android.content.pm.PackageParser$Package"),
					int.class);
			collectCertificatesMethod.invoke(packageParser, packag,
					PackageManager.GET_SIGNATURES);
			Signature mSignatures[] = (Signature[]) packag.getClass()
					.getField("mSignatures").get(packag);

			Signature apkSignature = mSignatures.length > 0 ? mSignatures[0]
					: null;
			System.out
					.println("testcontextandclass getApkSignatureMD5 apkSignature:"
							+ apkSignature.toCharsString());
			if (apkSignature != null) {
				// 说明：没有提供md5的具体实现
				return MD5.md5(apkSignature.toCharsString());
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} catch (NoSuchMethodException e) {
			e.printStackTrace();

		} catch (InstantiationException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (InvocationTargetException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		}
		return null;
	}

	public void startOtherApplicationClass() {
		Context c;
		try {
			c = createPackageContext("com.gift.android",
					Context.CONTEXT_INCLUDE_CODE
							| Context.CONTEXT_IGNORE_SECURITY);
			// 载入这个类
			Class clazz = c.getClassLoader().loadClass(
					"com.gift.android.model.BaseModel");
			// 新建一个实例
			Object owner = clazz.newInstance();
			// 获取print方法，传入参数并执行
			Object obj = clazz.getDeclaredMethod("print", String.class).invoke(owner,
					"Hello");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.lang.InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
