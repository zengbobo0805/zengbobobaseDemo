package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO 
//new Handler() 在主线程下，不管咋哪里new；
//new Handler(loop)  和loop有关；

//handler工作原理：1.必须有Looper,Looper.prepare()绑定线程、产生消息队列；
//2.new handler（looper）已looper，已经looper消息队列对应；
//3.通过handler发送消息message，同时message里成员为该handler；
//4.looper.loop()不断读取消息队列里消息message，调用handler里的方法处理消息；

public class HandlerUIActivity extends Activity {
	private TextView tv1, tv2;
	private boolean flag = true;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		tv1 = new TextView(this);
		tv1.setText("0");
		tv2 = new TextView(this);
		tv2.setText("0");
		layout.addView(tv1);
		layout.addView(tv2);
		setContentView(layout);

		// TODO
		// handler 与线程绑定；
		// MyHandlerThread ht = new MyHandlerThread("aa");
		// ht.start();
		MyThread th = new MyThread();
		th.start();
		new MyThread1().start();
	}

	// TODO
	// 线程looper，给new handler(looper)，这样handler就在该线程中了;
	class MyThread extends Thread {
		int count = 0;
		Looper looper;

		public class MyHandler extends Handler {

			public MyHandler() {
				super();
			}

			public MyHandler(Looper looper) {
				super(looper);
			}

			// TODO
			// 添加Callback:
			// 1.当在callback.handleMessage返回true时，handler里的handleMessage不执行
			// 2.当在callback.handleMessage返回false时，handler里的handleMessage执行
			public MyHandler(Looper looper, Callback callback) {
				super(looper, callback);
			}

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				count++;
				if (msg.what == 0x0001) {
					// tv1.setText(count + "");
					System.out.println("1111111111111");
				} else if (msg.what == 0x0002) {
					System.out.println("2222222222222");
					// tv2.setText(count + "");

				}

			}
		}

		@Override
		public void run() {
			super.run();
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new MyHandler(this.getLooper(), new Handler.Callback() {

				@Override
				public boolean handleMessage(Message msg) {

					System.out.println("Handler.Callback()");
					return true;
				}
			});
			Looper.loop();
		}

		public Looper getLooper() {
			if (!isAlive()) {
				return null;
			}

			// If the thread has been started, wait until the looper has been
			// created.
			synchronized (this) {
				while (isAlive() && looper == null) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}
			return looper;
		}
	}

	class MyThread1 extends Thread {
		@Override
		public void run() {
			super.run();
			while (flag) {
				try {
					Thread.sleep(3000);
					if (handler != null) {
						System.out.println("1111111 continue");
						Message msg = new Message();
						msg.what = 0x0001;
						handler.sendMessage(msg);

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		flag = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		flag = false;
	}

	class MyHandlerThread extends HandlerThread {

		public MyHandlerThread(String name) {
			super(name);
		}

		public MyHandlerThread(String name, int priority) {
			super(name, priority);
		}

		@Override
		public void run() {
			super.run();
		}

		@Override
		protected void onLooperPrepared() {
			super.onLooperPrepared();
			try {
				Thread.sleep(3000);
				if (handler == null) {
					System.out.println("2222222222222 continue");
				}
				Message msg = new Message();
				msg.what = 0x0002;
				handler.sendMessage(msg);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
