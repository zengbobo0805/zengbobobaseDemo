package com.zengbobobase.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.zengbobobase.demo.utils.DisplayUtil;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/9/2 13:42
 * 修改人：bobo
 * 修改时间：2015/9/2 13:42
 * 修改备注：
 */
public class MyVideoView extends VideoView {
    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screntWidth = DisplayUtil.getScrentWidth(getContext());
//        float scale = 720 / (float) screntWidth;
        float scale = 1.8181819f;
        int height = (int) (screntWidth / scale);
        super.onMeasure(screntWidth, height);
    }
}
