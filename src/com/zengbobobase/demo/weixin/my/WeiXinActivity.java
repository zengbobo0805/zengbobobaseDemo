package com.zengbobobase.demo.weixin.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.zengbobobase.demo.R;

public class WeiXinActivity extends Activity {
	private IWXAPI iwxApi;
	private Button btn1;
	private TextView tv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_layout);
		btn1=(Button) findViewById(R.id.btn1);
		tv1 = (TextView) findViewById(R.id.tv1);
		
		iwxApi = WXAPIFactory.createWXAPI(WeiXinActivity.this, Constant.WEIXIN_APP_ID, true);
		iwxApi.registerApp(Constant.WEIXIN_APP_ID);
		
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendMessageToWX();
			}
		});
	}
	
	private void sendMessageToWX(){
		WXTextObject textObject = new WXTextObject();
		textObject.text="hello weixin sdk!";
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.description="text";
		msg.mediaObject=textObject;
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
//		req.scene ="WXSceneSession";//WXSceneTimeline
		
		iwxApi.sendReq(req);
				
	}
}
