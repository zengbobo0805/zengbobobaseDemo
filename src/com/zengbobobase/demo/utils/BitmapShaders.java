package com.zengbobobase.demo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;

import com.zengbobobase.demo.R;

public class BitmapShaders extends View  
{  
    private  BitmapShader bitmapShader = null;  
    private Bitmap bitmap = null;  
    private Paint paint = null;  
    private ShapeDrawable shapeDrawable = null;  
    private int BitmapWidth  = 0;  
    private int BitmapHeight = 0;  
    public BitmapShaders(Context context)  
    {  
        super(context);  
        
        Resources res = context.getResources();  
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.jiudian_bg);  
        //得到图像  
        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.jiudian_bg)).getBitmap();    
        BitmapWidth = bitmap.getWidth();  
        BitmapHeight = bitmap.getHeight();  
        //构造渲染器BitmapShader  
        bitmapShader = new BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.REPEAT);  
    }  
    @Override  
    protected void onDraw(Canvas canvas)  
    {  
        super.onDraw(canvas);  
        //将图片裁剪为椭圆形    
        //构建ShapeDrawable对象并定义形状为椭圆    
//        new OvalShape() 椭圆；
//       RectShape  默认矩形
//        ArcShape	Creates an arc shape. 参数一：x轴正方向水平角度 ；  参数二：顺时针角度
//        OvalShape	Defines an oval shape. 
//        PathShape	Creates geometric paths, utilizing the Path class. 
//        RectShape	Defines a rectangle shape. 
//        RoundRectShape	Creates a rounded-corner rectangle. 
        shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{12, 12, 12, 12, 0, 0, 0, 0 }, new RectF(60,60,60,60), new float[]{12, 12, 0, 0, 12, 12, 0, 0}));  
        //得到画笔并设置渲染器  
        shapeDrawable.getPaint().setShader(bitmapShader);  
        //设置显示区域  
        shapeDrawable.setBounds(20, 20,BitmapWidth-60,BitmapHeight-60);  
        //绘制shapeDrawable  
        shapeDrawable.draw(canvas);  
    }  
}  