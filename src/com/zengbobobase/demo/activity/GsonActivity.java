package com.zengbobobase.demo.activity;

import com.google.gson.reflect.TypeToken;
import com.zengbobobase.demo.utils.JsonUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GsonActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		setContentView(tv);
		BaseModel baseModel = new BaseModel();
		baseModel.code = 1;
		baseModel.title ="标题";
		baseModel.content ="内容";
		DataModel dataModel = new DataModel();
		dataModel.model=baseModel;
		dataModel.name="姓名";
		String json = JsonUtil.toJson(dataModel);
		System.out.println("GsonActivity onCreate json:"+json);
		json="{\"model\":{\"model\":{\"title\":\"标题\",\"content\":\"内容\",\"code\":1},\"name\":\"姓名\"}}";
		JSDataModel<DataModel> newModel = JsonUtil.parseJson(json,new TypeToken<JSDataModel<DataModel>>(){}.getType());
		
		DataModel model = newModel.model;
		if(null != model){
			StringBuffer sb = new StringBuffer();
			sb.append("title:").append(model.model.title);
			sb.append("\n");
			sb.append("content:").append(model.model.content);
			sb.append("\n");
			sb.append("name:").append(model.name);
			tv.setText(sb.toString());
		}
		System.out.println("11111111111");
		
	}
//	{"model":{"model":{"title":"标题","content":"内容","code":1},"name":"姓名"}}
	class JSDataModel<T>{
		T model;
	}
	
	class DataModel{
		BaseModel model;
		String name;
	}
	
	 class BaseModel {
		public int code;
		public String title;
		public String content;
	}
}
