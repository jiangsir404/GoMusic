package lj.gomusic;

import android.content.ContentValues;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {
    private ArrayList<Musicitem> musicList;
    private Button button;
    private ImageView persional;
    private ImageView reset;
    public final static String SER_KEY = "ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        ListView listView = (ListView)findViewById(R.id.list_view);
        persional = (ImageView)findViewById(R.id.persional);
        reset = (ImageView)findViewById(R.id.reset);

        musicList = new ArrayList<Musicitem>();
        initData();
        MusicitemAdapter itemAdapter = new MusicitemAdapter(this,R.layout.activity_music_item,musicList);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Toast.makeText(MusicListActivity.this,"您选择了"+ musicList.get(i).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MusicListActivity.this,MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                String server = "http://59.64.78.184:6100";
                bundle.putSerializable(SER_KEY,musicList);
                bundle.putInt("position",i);
                bundle.putString("musicPath",server+musicList.get(i).getMusicPath());
                intent.putExtras(bundle);
                startActivity(intent);
                MusicListActivity.this.finish();
            }
        });

        persional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicListActivity.this,PersionalActivity.class);
                startActivity(intent);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicListActivity.this,MusicListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
//                    ContentValues postParams = new ContentValues();
//                    //要传递的参数
//                    postParams.put("servicetype","2");
                    String s = UrlManager.httpUrlConnectionGet("http://59.64.78.184:6100/work/music.php");
                    Log.i("s",s);
                    JSONObject result = new JSONObject(s);
                    Log.i("result",result.toString());
                    JSONArray array = result.getJSONArray("infos");
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        Musicitem mitem = new Musicitem();
                        mitem.setName(object.getString("musicName"));
                        mitem.setAuthor(object.getString("musicAuthor"));
                        mitem.setMusicPath(object.getString("musicPath"));
                        mitem.setPhoto(object.getString("musicPhoto"));
                        mitem.setMusicText(object.getString("musicText"));
                        musicList.add(mitem);
                    }
                    Thread.sleep(1000);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


