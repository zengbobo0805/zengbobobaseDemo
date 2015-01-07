package com.zengbobobase.demo.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zengbobobase.demo.R;

public class GsonParseOrToJsonActivity extends Activity {

	private String str1 = "{code:\"code1\",name: \"name1\",key: \"key1\",value: \"value1\",age: \"11\",\"play\":{code:\"code3\",name: \"name4\"}}";
	private Map<String, Object> map1;
	private Map<String, Object> map2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map1 = parseJson(str1);
		map2 = new HashMap<String, Object>();
		map2.put("code", "code3");
		map2.put("name", "name3");
		map2.put("key", "key3");
		map2.put("value", "value3");
		map2.put("age", "age3");
		Model model = new Model();
		model.code ="code----";
		model.name ="name----";
		model.key ="key----";
		model.value ="value----";
		model.age ="age----";
		map2.put("play", model);
		Iterator<String> it = map1.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			System.out.println("MainActivity map1 key:"+key+",value:"+map1.get(key));
		}
		
		System.out.println("MainActivity map1:"+toJson(map1));
		System.out.println("MainActivity map2:"+toJson(map2));
	}

	public Map<String, Object> parseJson(String response) {

		Map<String, Object> map;
		try {
			Type listType = new TypeToken<Map<String, Object>>() {
			}.getType();
			map = new Gson().fromJson(response, listType);

			return map;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String toJson(Map<String, Object> map) {
		try {
			Type listType = new TypeToken<Map<String, Object>>() {
			}.getType();
			String buf = new Gson().toJson(map, listType);

			return buf;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public class Model{
		private String code;
		private String name;
		private String key;
		private String value;
		private String age;
		
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getAge() {
			return age;
		}
		public void setAge(String age) {
			this.age = age;
		}
		
		
		
	}
}
