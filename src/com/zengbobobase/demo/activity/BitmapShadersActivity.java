package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.widget.ImageView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.BitmapShaders;

public class BitmapShadersActivity extends Activity {

	private BitmapShaders bitmapShaders = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		bitmapShaders = new BitmapShaders(this);
//		setContentView(bitmapShaders);
		ImageView img = new ImageView(this);
		img.setImageBitmap(createReflectionImageWithOrigin(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
		setContentView(img);

}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {  
	    final int reflectionGap = 1;  
	    int w = bitmap.getWidth();  
	    int h = bitmap.getHeight();  
	  
	    Matrix matrix = new Matrix();  
	    matrix.preScale(1, -0.5f);  
	  
	    Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, 0, w,  
	            h , matrix, false);  
	  
	    Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),  
	            Config.ARGB_8888);  
	  
	    Canvas canvas = new Canvas(bitmapWithReflection);  
	    canvas.drawBitmap(bitmap, 0, 0, null);  
	    Paint deafalutPaint = new Paint();  
	    canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);  
	  
	    canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);  
	  
	    Paint paint = new Paint();  
	    LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,  
	            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,  
	            0x00ffffff, TileMode.CLAMP);  
	    paint.setShader(shader);  
	    // Set the Transfer mode to be porter duff and destination in  
	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
//	     Draw a rectangle using the paint with our linear gradient  
	    canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()  
	            + reflectionGap, paint);  
	  
	    return bitmapWithReflection;  
	}  
	
}
