package lj.gomusic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lj.gomusic.lrc.ILrcBuilder;
import lj.gomusic.lrc.zyl.DefaultLrcBuilder;
import lj.gomusic.lrc.zyl.LrcRow;

public class MusicPlayActivity extends AppCompatActivity {
    private Button btstart;
    private Button btstop;
    private Button btnew;
    private MediaPlayer media;
    private Uri uriSource;
    private SeekBar seek;
    private int change;
    private Timer time;
    private TimerTask task;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

//        //从assets读取歌词文件内容
//        String lrc = getFromAssets("piaoyang.lrc");
//        //解析歌词构造器
//        ILrcBuilder builder =new DefaultLrcBuilder();
//        //解析歌词返回LrcRow
//        List<LrcRow> rows = builder.getLrcRows(lrc);

        Bundle bundle = this.getIntent().getExtras();
//        ArrayList<Musicitem> musicInfos = bundle.getSerializable("music");
        ArrayList<Musicitem> musicInfos = (ArrayList<Musicitem>)getIntent().getSerializableExtra(MusicListActivity.SER_KEY);
        int position = this.getIntent().getExtras().getInt("position");
        Log.i("position",""+position);
        Log.i("musicPath",musicInfos.get(position).getMusicPath());
        btstart=(Button)findViewById(R.id.start);
        btnew=(Button) findViewById(R.id.newmusic);
        btstop = (Button) findViewById(R.id.stop);
        seek = (SeekBar) findViewById(R.id.seekBar);
        change=1;
        flag = 0;
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                change = 0; //该状态在播放过程中会一直加载，拖动进度条向前移动
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                change=1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                media.seekTo(seek.getProgress());
                change=0;
            }
        });

        media=new MediaPlayer();
        uriSource = Uri.parse(musicInfos.get(position).getMusicPath());
        try {  //setDataSource()和prepare方法都需要捕获异常
            media.setDataSource(MusicPlayActivity.this, uriSource);
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
                    btstart.setText("暂停");
                    seek.setMax(media.getDuration());

                } else{
                    media.pause();
                    btstart.setText("播放");

                }
                Log.i("end1","end1");
                time = new Timer();
                task=new TimerTask() {
                    @Override
                    public void run() {
                        if(change==1){
                            return;
                        }
                        seek.setProgress(media.getCurrentPosition());
                    }
                };
                time.schedule(task,100,100);
            }
        });
        Log.i("end2","end2");


//        btstop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(media.isPlaying()){
//                    media.stop();
//                }
//                else{
//                    Toast.makeText(MainActivity.this,"音乐没有播放",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        btnew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                media.reset();
//                uri = Uri.parse("http://59.64.78.184/love.mp3");
//                try {
//                    media.setDataSource(MainActivity.this,uri);
//                    media.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                media.start();
//            }
//        });
    }
//    public String getFromAssets(String fileName){
//        try {
//            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line="";
//            String result="";
//            while((line = bufReader.readLine()) != null){
//                if(line.trim().equals(""))
//                    continue;
//                result += line + "\r\n";
//            }
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
