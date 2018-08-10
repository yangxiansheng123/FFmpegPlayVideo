package com.retrofit.ffmpegplayvideo;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/***
 *
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "--------------->";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    String source1 = "http://video.7k.cn/app_video/20171202/6c8cf3ea/v.m3u8.mp4";
    MyLayoutManager myLayoutManager;
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }
    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);
        myLayoutManager = new MyLayoutManager(this, OrientationHelper.VERTICAL,false);

        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(myLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG,"释放位置:"+position +" 下一页:"+isNext);
                int index = 0;
                if (isNext){
                    index = 0;
                }else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isNext) {
                Log.e(TAG,"释放位置:"+position +" 下一页:"+isNext);

                int index = 0;
                if (isNext){
                    index = 0;
                }else {
                    index = 1;
                }
                playVideo(index);
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
             public MyAdapter(){
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //增加封面
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.xxx1);
            holder.videoPlayer.setThumbImageView(imageView);
            //增加title
            holder.videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
            //设置返回键
            holder.videoPlayer.getBackButton().setVisibility(View.VISIBLE);
            //设置旋转
            orientationUtils = new OrientationUtils(MainActivity.this, holder.videoPlayer);
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            holder.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orientationUtils.resolveByClick();
                }
            });
            //是否可以滑动调整
            holder.videoPlayer.setIsTouchWiget(true);
            //设置返回按键功能
            holder.videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            holder.videoPlayer.startPlayLogic();
        }

        @Override
        public int getItemCount() {
            return 15;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView img_thumb;
            ImageView img_play;
            StandardGSYVideoPlayer videoPlayer;
            RelativeLayout rootView;
            public ViewHolder(View itemView) {
                super(itemView);
                videoPlayer = itemView.findViewById(R.id.video_view);
            }
        }
    }

    private void releaseVideo(int index){
        View itemView = mRecyclerView.getChildAt(index);
        final StandardGSYVideoPlayer videoPlayer = itemView.findViewById(R.id.video_view);
        videoPlayer.onVideoPause();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final StandardGSYVideoPlayer videoPlayer = itemView.findViewById(R.id.video_view);
        videoPlayer.setUp(source1, false, "测试视频");
        videoPlayer.startPlayLogic();

/**
 *
 */
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
    }

}