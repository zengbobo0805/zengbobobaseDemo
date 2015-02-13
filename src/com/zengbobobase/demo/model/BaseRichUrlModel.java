package com.zengbobobase.demo.model;

public class BaseRichUrlModel {
	public static final String BASE_RICH_URL_HOST = "musturl://ckeditor?";
	public static final String BASE_RICH_URL_MENUSTATE = "menustate";
	public static final String BASE_RICH_URL_AUDIO = "audio";
	public static final String BASE_RICH_URL_IMG = "img";
	public static final String BASE_RICH_URL_EDITORCONTENT = "editorcontent";
	public static final String BASE_RICH_URL_INIT_COMPLETED ="initCompleted";
	public static final String BASE_RICH_URL_RESIZE_HEIGHT="resizeheight";
	private String type; // menustate,
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
