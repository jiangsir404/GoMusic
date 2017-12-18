package lj.gomusic;

import android.content.ContentValues;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lj.gomusic.lrc.ILrcBuilder;
import lj.gomusic.lrc.ILrcView;
import lj.gomusic.lrc.zyl.DefaultLrcBuilder;
import lj.gomusic.lrc.zyl.LrcRow;

public class MusicPlayActivity extends AppCompatActivity {
    private ImageView btstart;
    private ImageView btbefore;
    private ImageView btnext;
    private ImageView btback;
    private MediaPlayer media;
    private Uri uriSource;
    private SeekBar seek;
    private TextView muName;
    private TextView muAuthor;
    private int change;
    private Timer time;
    private TimerTask task;
    private int flag;
    private int position;

    //自定义LrcView,用来展示歌词
    ILrcView mLrcView;
    //更新歌词屏率，单位毫秒
    private int mPlayTimeDuration = 1000;
    //跟新歌词的定时器
    private Timer mTimer;
    //更新歌词的定时任务
    private TimerTask mTask;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        Bundle bundle = this.getIntent().getExtras();
        //获取ListActivity传过来的Musicitem 的ArrayList对象（通过反序列化方法传递)
        final ArrayList<Musicitem> musicInfos = (ArrayList<Musicitem>)getIntent().getSerializableExtra(MusicListActivity.SER_KEY);
        position = this.getIntent().getExtras().getInt("position"); //判断当前点击的是那首歌
        Log.i("position",""+position);
        Log.i("musicPath",musicInfos.get(position).getMusicPath());


        mLrcView=(ILrcView)findViewById(R.id.lrcView);
        btback = (ImageView) findViewById(R.id.back);
        btstart=(ImageView) findViewById(R.id.start);
        btbefore=(ImageView) findViewById(R.id.before);
        btnext = (ImageView) findViewById(R.id.next);
        seek = (SeekBar) findViewById(R.id.seekBar);
        muName = (TextView) findViewById(R.id.musicname);
        muAuthor = (TextView)findViewById(R.id.author);
        media=new MediaPlayer();
        change=1;
        flag = 0;
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                media.reset();
                Intent intent = new Intent();
                intent.setClass(MusicPlayActivity.this,MusicListActivity.class);
                startActivity(intent);
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override  //进度条该表的时候调用
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                change = 0; //该状态在播放过程中会一直加载，拖动进度条向前移动
            }

            @Override   //进度条开始拖动的时候调用
            public void onStartTrackingTouch(SeekBar seekBar) {
                change=1;
            }

            @Override  //进度条停止拖动的时候调用
            public void onStopTrackingTouch(SeekBar seekBar) {
                media.seekTo(seek.getProgress()); //拖动后让歌曲同步到这个时间片位置
                change=0;
            }
        });

        uriSource = Uri.parse(musicInfos.get(position).getMusicPath());
        Log.i("muscname:",musicInfos.get(position).getName());
        muName.setText(musicInfos.get(position).getName());
        muAuthor.setText(musicInfos.get(position).getAuthor());
        getFromAssets(musicInfos.get(position).getMusicText());
        try {  //setDataSource()和prepare方法都需要捕获异常
            media.setDataSource(MusicPlayActivity.this, uriSource);
            //准备播放歌曲监听
            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
                    if(mTimer == null){
                        mTimer = new Timer();
                        mTask = new LrcTask();
                        mTimer.scheduleAtFixedRate(mTask,0,mPlayTimeDuration);
                    }
                }
            });
            //歌曲播放监听完毕
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            media.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }

        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("flag","f"+flag);

                Log.i("media","m"+media);

                if(!media.isPlaying()){
                    media.start();
                    flag = 1;
                    btstart.setImageResource(R.drawable.ic_pause);
                    seek.setMax(media.getDuration()); //获取音乐文件的时间，用来设置seek的Max属性

                } else{
                    media.pause();
                    btstart.setImageResource(R.drawable.ic_play); //修改图标

                }
                Log.i("end1","end1");
                time = new Timer();
                task=new TimerTask() {
                    @Override
                    public void run() { //定时刷新进度条
                        if(change==1){
                            return;
                        }
                        seek.setProgress(media.getCurrentPosition()); //启动任务循环设置进度条的值
                    }
                };
                time.schedule(task,100,100);
            }
        });
        Log.i("end2","end2");


        btbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.reset();
                position = position -1;  //定位到上一首歌的位置
                Log.i("position:",""+position);
                if(position < 0){ //第一首歌
                    position = 0;
                }
                uriSource = Uri.parse(musicInfos.get(position).getMusicPath());
                muName.setText(musicInfos.get(position).getName());
                muAuthor.setText(musicInfos.get(position).getAuthor());
                getFromAssets(musicInfos.get(position).getMusicText());
                try {
                    media.setDataSource(MusicPlayActivity.this,uriSource);
                    media.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                media.start();
                btstart.setImageResource(R.drawable.ic_pause);
            }
        });

        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.reset();
                position = position + 1;
                Log.i("position:",""+position);
                if(position >= musicInfos.size()){
                    position = musicInfos.size() - 1;
                }
                uriSource = Uri.parse(musicInfos.get(position).getMusicPath());
                muName.setText(musicInfos.get(position).getName());
                muAuthor.setText(musicInfos.get(position).getAuthor());
                getFromAssets(musicInfos.get(position).getMusicText());
                try {
                    media.setDataSource(MusicPlayActivity.this,uriSource);
                    media.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                media.start();
                btstart.setImageResource(R.drawable.ic_pause);
            }
        });
    }

    public String getFromAssets(final String fileName){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = UrlManager.httpUrlConnectionGet("http://59.64.78.184:6100/lrc/"+fileName);
                    //解析歌词构造器
                    ILrcBuilder builder =new DefaultLrcBuilder();
                    //解析歌词返回LrcRow
                    List<LrcRow> rows = builder.getLrcRows(result);
                    //将得到的歌词集合传给mLrcView来显示
                    mLrcView.setLrc(rows);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了


        return "";
    }


    public void stopLrcPlay(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    class LrcTask extends TimerTask{
        public void run(){
            //获取歌曲播放位置
            final long timePassed = media.getCurrentPosition();
            MusicPlayActivity.this.runOnUiThread(new Runnable(){
                public void run(){
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });
        }

    }

}