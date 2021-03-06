package example.com.jniexample;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.audioplayer.player.AudioPlayer;
import com.example.audioplayer.player.OnPlayCompleteListener;
import com.example.audioplayer.player.OnPlayErrorListener;
import com.example.audioplayer.player.OnPlayLoadListener;
import com.example.audioplayer.player.OnPlayPreparedListener;
import com.example.audioplayer.player.OnPlayResumeAndPauseListener;
import com.example.audioplayer.player.OnPlayTimeInfoListener;
import com.example.audioplayer.player.TimeInfo;

import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AudioPlayer mAudioPlayer = null;
    private TextView mTvTimeInfo = null;
    private SeekBar mSeekBarSeek = null;
    private SeekBar mSeekBarVolume;
    private TextView mTvVolume = null;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                TimeInfo timeInfo = (TimeInfo) msg.obj;
                int currentTime = timeInfo.currentTime;
                int duration = timeInfo.duration;

                long timestamp = (duration - currentTime) * 1000;
                mTvTimeInfo.setText(stampToDate(timestamp));

                mSeekBarSeek.setMax(duration);
                mSeekBarSeek.setProgress(currentTime);
            }
        }
    };


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeStamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date(timeStamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTimeInfo = findViewById(R.id.tv_time_info);
        mSeekBarSeek = findViewById(R.id.seekbar_seek);
        mSeekBarVolume = findViewById(R.id.seekbar_volume);
        mTvVolume = findViewById(R.id.tv_volume);
        mAudioPlayer = new AudioPlayer();
        mAudioPlayer.setVolumePercent(50);
        mTvVolume.setText("音量：" + mAudioPlayer.getVolumePercent() + "%");
        mSeekBarVolume.setProgress(mAudioPlayer.getVolumePercent());
        mAudioPlayer.setOnPlayPreparedListener(new OnPlayPreparedListener() {
            @Override
            public void onPrepared() {
                Log.i(TAG, "onPrepared");
                mAudioPlayer.start();
            }
        });

        mAudioPlayer.setOnPlayLoadListener(new OnPlayLoadListener() {
            @Override
            public void onLoad(boolean onLoad) {
                Log.i(TAG, onLoad ? "正在加载中..." : "正在播放");
            }
        });

        mAudioPlayer.setOnPlayResumeAndPauseListener(new OnPlayResumeAndPauseListener() {
            @Override
            public void onPause() {
                Log.i(TAG, "onPause");
            }

            @Override
            public void onResume() {
                Log.d(TAG, "onResume");
            }
        });

        mAudioPlayer.setOnPlayTimeInfoListener(new OnPlayTimeInfoListener() {
            @Override
            public void onPlayTimeInfo(TimeInfo timeInfo) {
                Message msg = Message.obtain();
                msg.obj = timeInfo;
                msg.what = 1000;
                mHandler.sendMessage(msg);

            }
        });

        mAudioPlayer.setOnPlayErrorListener(new OnPlayErrorListener() {
            @Override
            public void onError(int errCode, String errMsg) {
                Log.e(TAG, "errCode = " + errCode + ",errMsg = " + errMsg);
            }
        });

        mAudioPlayer.setOnPlayCompleteListener(new OnPlayCompleteListener() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });

        mSeekBarSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAudioPlayer.seek(seekBar.getProgress());
            }
        });

        mSeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "音量:" + progress);
                mAudioPlayer.setVolumePercent(progress);
                mTvVolume.setText("音量：" + mAudioPlayer.getVolumePercent() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void hello_ffmpeg(View view) {
//        JniDemo jniDemo = new JniDemo();
//        jniDemo.test();
    }

//    public void hello_jni_thread(View view) {
//        JniThreadDemo jniThreadDemo = new JniThreadDemo();
//
//        jniThreadDemo.createThread();
//    }
//
//    public void callJavaMethodOnCppMainThread(View view) {
//        JniThreadDemo jniThreadDemo = new JniThreadDemo();
//
//        jniThreadDemo.callJavaMethodOnCPPMainThread();
//    }
//
//    public void callJavaMethodOnCppChildThread(View view) {
//        JniThreadDemo jniThreadDemo = new JniThreadDemo();
//
//        jniThreadDemo.callJavaMethodOnCppChildThread();
//    }

//    //生产者与消费者
//    public void mutex(View view) {
//        JniThreadDemo jniThreadDemo = new JniThreadDemo();
//
//        jniThreadDemo.mutex();
//    }
//
//
//    //生产者与消费者
//    public void stopMutex(View view) {
//        JniThreadDemo jniThreadDemo = new JniThreadDemo();
//
//        jniThreadDemo.stopMutex();
//    }

    public void audio_prepare(View view) {
//        mAudioPlayer.setSource("/storage/emulated/0/houlai.mp3");
        mAudioPlayer.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        mAudioPlayer.prepare();
    }

//    public void OpenSLES_PCM_PLAYER(View view) {
//        PCMPlayer player = new PCMPlayer();
//        player.play();
//    }

    public void audio_pause(View view) {
        //暂停，是暂停播放线程，解码线程并不会暂停
        mAudioPlayer.pause();
    }

    public void audio_resume(View view) {
        mAudioPlayer.onResume();
    }


    public void stop(View view) {
        mSeekBarSeek.setProgress(0);
        mTvTimeInfo.setText(stampToDate(0));
        mAudioPlayer.stop();
    }


    /**
     * 播放下一首
     *
     * @param view
     */
    public void playNext(View view) {
        String nextUrl = "http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3";
        mAudioPlayer.playNext(nextUrl);
    }
}


