package com.zengbobobase.demo.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zengbobobase.demo.R;
import com.zengbobobase.demo.utils.DisplayUtil;

/**
 * 项目名称：xrz_application
 * 类描述：
 * 创建人：bobo
 * 创建时间：2015/8/24 15:06
 * 修改人：bobo
 * 修改时间：2015/8/24 15:06
 * 修改备注：
 */
public class VideoActivity extends Activity {
    private String videoPath = "http://ocs.icaikee.com/app/vedio/xrz_vedio.mp4";
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        mVideoView = (VideoView) findViewById(R.id.videoView);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("VideoActivity ","onPrepared getCurrentPosition:"+mVideoView.getCurrentPosition());
                Log.i("VideoActivity ","onPrepared getDuration:"+mVideoView.getDuration());
            }
        });


        int screntWidth = DisplayUtil.getScrentWidth(this);
//        float scale = 720 / (float) screntWidth;
        float scale = 1.8181819f;
        int height = (int) (screntWidth / scale);

//        L.i("VideoActivity initView screntWidth :" + screntWidth + ",scale:" + scale + ",height:" + height);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
//
//        L.i("VideoActivity initView params.width:" + params.width + ",height:" + params.height);
//        if (params == null) {
//            params = new LinearLayout.LayoutParams(screntWidth, height);
//        } else {
//            params.width=screntWidth;
//            params.height = height;
//        }
//        mVideoView.setLayoutParams(params);

        mVideoView.setMinimumWidth(screntWidth);
        mVideoView.setMinimumHeight(height);
        mVideoView.requestLayout();
         /* 获取MediaController对象，控制媒体播放 */
        MediaController mc = new MediaController(this);
        mVideoView.setMediaController(mc);

        /* 设置URI ， 指定数据 */
        mVideoView.setVideoURI(Uri.parse(videoPath));

        /* 开始播放视频 */
        mVideoView.start();

        /*  请求获取焦点 */
        mVideoView.requestFocus();
    }
}
