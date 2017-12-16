package lj.gomusic.lrc.zyl;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raincloud on 17-12-13.
 * 歌词行
 * 包括该行歌词的时间，内容
 */

public class LrcRow{
    public final static String TAG = "LrcRow";
    //歌词开始时间
    public String strTime;
    //转换时间为long
    public long time;
    //歌词内容
    public String content;

    public LrcRow(){}

    public LrcRow(String strTime,long time,String content){
        this.strTime=strTime;
        this.time=time;
        this.content=content;
        Log.d(TAG,"strTime:"+strTime+" time:"+time+" content:"+content);
    }
    /**
     * 按行读取歌词内容并转换为LrcRow
     */
    public static List<LrcRow> createRows(String standardLrcLine){
        /**
         *[00:02.59] 飘洋过海来看你
         *[00:09.82]常安演唱
         *[00:13.77]遂愿编辑同步歌词
         *[00:18.08]音乐
         *[02:17.62][00:27.46]为你我用了半年的积蓄
         *[02:21.05][00:31.99]飘洋过海的来看你
         *[02:24.81][00:35.60]为了这次相聚
         *[02:27.59][00:38.16]我连见面时的呼吸都曾反复练习
         */
        try{
            //index0f返回字符串在父串首次出现位置
            if(standardLrcLine.indexOf("[")!=0||standardLrcLine.indexOf("]")!=9){
                return null;
            }
            //找到最后一个']'位置
            int lastIndex0fRightBracket = standardLrcLine.lastIndexOf("]");
            //歌词文本
            String content = standardLrcLine.substring(lastIndex0fRightBracket + 1,standardLrcLine.length());
            /**
             * 转换时间格式，用'-'代替'[',']'
             */
            String times=standardLrcLine.substring(0,lastIndex0fRightBracket+1).replace("[","-").replace("]","-");
            //通过'-'拆分字符串
            String arrTimes[] = times.split("-");
            List<LrcRow> listTimes = new ArrayList<LrcRow>();
            for(String temp : arrTimes){
                /**
                 * split()处离字符串中连续的分隔符会产生""
                 * 通个识别""可以拆分重复的歌词时间
                 * [02:17.62][00:27.46]为你我用了半年的积蓄
                 * 拆分为两句：
                 * [02:17.62]为你我用了半年的积蓄
                 * [00:27.46]为你我用了半年的积蓄
                 */

                if(temp.trim().length()==0){
                    continue;
                }
                LrcRow lrcRow = new LrcRow(temp,timeConvert(temp),content);
                listTimes.add(lrcRow);
            }
            return listTimes;
        }catch(Exception e){
            Log.e(TAG,"createRows exception:"+e.getMessage());
            return null;
        }
    }
    /**
     * 转换时间为long
     */
    private static long timeConvert(String timeString){
        timeString=timeString.replace('.',':');
        String[] times=timeString.split(":");
        return Integer.valueOf(times[0])*60*1000 + //分
        Integer.valueOf(times[1])*1000 + //秒
        Integer.valueOf(times[2]); //毫秒
    }
    /**
     * 根据时间按排序
     */
    public int compareTo(LrcRow another){
        return (int)(this.time-another.time);
    }
}
