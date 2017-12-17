package lj.gomusic;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

//public class MusicItemActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_music_item);
//    }
//}

public class Musicitem implements Serializable {  //继承Serializable， 方便bundle传递对象
    private String name;
    private String author;
    private String photo;
    private String musicPath;
    private String musicText;
    private String server = "http://59.64.78.184:6100";

    public Musicitem(){

    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return author;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }
    public String getPhoto(){
        return photo;
    }

    public void setMusicPath(String musicPath){
        this.musicPath = server+musicPath;
    }
    public String getMusicPath(){
        return musicPath;
    }

    public void setMusicText(String musicText){
        this.musicText = musicText;
    }
    public String getMusicText(){
        return musicText;
    }
}