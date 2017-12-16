package lj.gomusic.lrc.zyl;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lj.gomusic.lrc.ILrcBuilder;
/**
 * Created by raincloud on 17-12-15.
 */

public class DefaultLrcBuilder implements ILrcBuilder {
    static final String TAG = "DefaultLrcBuilder";

    public List<LrcRow> getLrcRows(String rawLrc) {
        Log.d(TAG,"getLrcRows by rawString");
        if(rawLrc == null || rawLrc.length() == 0){
            Log.e(TAG,"getLrcRows rawLrc null or empty");
            return null;
        }
        StringReader reader = new StringReader(rawLrc);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        List<LrcRow> rows = new ArrayList<LrcRow>();
        try{
            //循环地读取歌词的每一行
            do{
                line = br.readLine();
                Log.d(TAG,"lrc raw line: " + line);
                if(line != null && line.length() > 0){
                    //解析每一行歌词 得到每行歌词的集合，因为有些歌词重复有多个时间，就可以解析出多个歌词行来
                    List<LrcRow> lrcRows = LrcRow.createRows(line);
                    if(lrcRows != null && lrcRows.size() > 0){
                        for(LrcRow row : lrcRows){
                            rows.add(row);
                        }
                    }
                }
            }while(line != null);

            if( rows.size() > 0 ){
                // 根据歌词行的时间排序
                Collections.sort(rows);
                if(rows!=null&&rows.size()>0){
                    for(LrcRow lrcRow:rows){
                        Log.d(TAG, "lrcRow:" + lrcRow.toString());
                    }
                }
            }
        }catch(Exception e){
            Log.e(TAG,"parse exceptioned:" + e.getMessage());
            return null;
        }finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return rows;
    }
}
