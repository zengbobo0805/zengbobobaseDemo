package com.zengbobobase.demo.activity;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.download.downloader.DownloadProgressListener;
import com.zengbobobase.demo.download.downloader.DownloadTask;
import com.zengbobobase.demo.download.downloader.FileDownloader;
import com.zengbobobase.demo.utils.Contast;

public class MulThreadDownLoadFileActivity extends Activity {

	/** 下载路径文本框 **/
	private EditText pathText;
	/** 下载按钮 **/
	private Button downloadButton;
	/** 停止下载按钮 **/
	private Button stopbutton;
	/** 下载进度条 **/
	private ProgressBar progressBar;
	/** 下载结果文本框，显示下载的进度值 **/
	private TextView resultView;

	/** Hanlder的作用是用于往创建Hander对象所在的线程所绑定的消息队列发送消息 **/
	private Handler handler = new UIHander();
	/** 设置按钮的监听 **/
	ButtonClickListener listener = new ButtonClickListener();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mul_thread_file_download);

		/** 初始化各控件 **/
		pathText = (EditText) this.findViewById(R.id.path);
		downloadButton = (Button) this.findViewById(R.id.downloadbutton);
		stopbutton = (Button) this.findViewById(R.id.stopbutton);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		resultView = (TextView) this.findViewById(R.id.resultView);
		downloadButton.setOnClickListener(listener);
		stopbutton.setOnClickListener(listener);
	}

	/**
	 * 按钮监听类
	 */
	private final class ButtonClickListener implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			/** 如果是下载按钮 */
			case R.id.downloadbutton:
				String path = pathText.getText().toString();// 获取下载路径
				// 判断SD卡是否存在并且可写
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获取SD卡的路径
					File saveDir = Environment.getExternalStorageDirectory();
					// 开始下载的相关操作
					download(path, saveDir);
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.sdcarderror, 1).show();
				}
				downloadButton.setEnabled(false);
				stopbutton.setEnabled(true);
				break;
			/** 如果是停止下载按钮 */
			case R.id.stopbutton:
				exit();// 退出下载
				downloadButton.setEnabled(true);
				stopbutton.setEnabled(false);
				break;
			}
		}

		/** end of DownloadTask */

		/**
		 * 由于用户的输入事件(点击button, 触摸屏幕....)是由主线程负责处理的，如果主线程处于工作状态，
		 * 此时用户产生的输入事件如果没能在5秒内得到处理，系统就会报“应用无响应”错误。
		 * 所以在主线程里不能执行一件比较耗时的工作，否则会因主线程阻塞而无法处理用户的输入事件，
		 * 导致“应用无响应”错误的出现。耗时的工作应该在子线程里执行。
		 */
		private DownloadTask task;

		/**
		 * 退出下载
		 */
		public void exit() {
			if (task != null)
				task.exit();
		}

		/**
		 * 下载方法，运行在主线程，负责开辟子线程完成下载操作，这操作耗时不超过1秒
		 * 
		 * @param path
		 *            下载路径
		 * @param saveDir
		 *            保存路径
		 */
		private void download(String path, File saveDir) {
			task = new DownloadTask(MulThreadDownLoadFileActivity.this, path,
					saveDir, handler);
			new Thread(task).start();// 开辟子线程完成下载操作
		}
	}
	@Override
	protected void onDestroy() {
		listener.exit();
		super.onDestroy();
	}
	/** end of ButtonClickListener **/

	/**
	 * Hanlder的作用是用于往创建Hander对象所在的线程所绑定的消息队列发送消息
	 */
	private final class UIHander extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Contast.TOTAL_SIZE:
				int total = (Integer) msg.obj; // 获取下载的进度值
				progressBar.setMax(total);
				break;

			case Contast.UPDATE_SIZE:
				int size = (Integer) msg.obj; // 获取下载的进度值
				progressBar.setProgress(size); // 实时更新，设置下载进度值
				/** 计算下载的进度百分比 */
				float num = (float) progressBar.getProgress()
						/ (float) progressBar.getMax();
				int result = (int) (num * 100);
				resultView.setText(result + "%"); // 设置下载结果文本框显示下载的进度值

				break;
			case Contast.SUCCESS_SIZE:
				progressBar.setProgress(progressBar.getMax()); // 实时更新，设置下载进度值

				resultView.setText("100%"); // 设置下载结果文本框显示下载的进度值
				// 如果进度达到了进度最大值，即下载完毕
				Toast.makeText(getApplicationContext(), R.string.success, 1)
						.show();// 下载成功

				break;
			case Contast.ERROR_SIZE:
				String error = (String) msg.obj;
				Toast.makeText(getApplicationContext(), error, 1)
						.show();// 下载出错
				break;
			case Contast.FAILURE_SIZE:
				String fail = (String) msg.obj;
				Toast.makeText(getApplicationContext(), fail, 1)
						.show();// 下载出错
				break;
			case Contast.TOAST_SIZE:
				String toast = (String) msg.obj;
				Toast.makeText(getApplicationContext(), toast, 1)
						.show();// 下载出错
				break;
			
			}
		}
	}



}
