package com.zengbobobase.demo.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Class Name: JsonUtil.java
 * 
 * @author yangyang DateTime 2013-5-11 上午10:06:47
 * @version 1.0
 */
public class JsonUtil {

	private static Gson gson = new Gson();
	public static String toJson(Object object) {
		try {
			return gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("hiding")
	public static <T> T parseJson(String response, Class<T> clazz) {
		try {
			return gson.fromJson(response, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static <T> T parseJson(String response, Type type){
		try{
			return gson.fromJson(response, new TypeToken<T>(){}.getType());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

//	public static <T> List<T> getPersons(String jsonString, Class<T> cls) {
//		cls.getAnnotation(cls);
//		List<T> list = new ArrayList<T>();
//		try {
//			Gson gson = new Gson();
//			list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {
//			}.getType());
//		} catch (Exception e) {
//		}
//		return list;
//	}

	public static List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
}
