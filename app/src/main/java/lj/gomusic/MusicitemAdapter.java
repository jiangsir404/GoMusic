package lj.gomusic;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

//public class MusicAdapter extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}

public class MusicitemAdapter extends ArrayAdapter<Musicitem> {
    private List<Musicitem> list;
    private Context context;
    private int resourceId;
    private LayoutInflater inflater;

    public MusicitemAdapter(Context context,int resourceId,List<Musicitem> list){
        super(context,resourceId,list);
        this.resourceId = resourceId;
        this.list = list;
    }
    @Override
    public View getView(int position,final View convertView,ViewGroup parent){ //重写getView函数
        Musicitem music = getItem(position); //获取ListView中每一条数据，getItem()是内置函数
        //通过Adapter 将数据和ListVIew绑定
        final View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView musicName = (TextView) view.findViewById(R.id.musicname);
//        TextView musicAuthor = (TextView) view.findViewById(R.id.author);
        //TextView editNum = (TextView) view.findViewById(R.id.editnum);
        musicName.setText(music.getName());
//        musicAuthor.setText(music.getAuthor());
//        editNum.setText((""+(position+1)));
        return view;
    }
}




























