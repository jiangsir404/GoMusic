package lj.gomusic.lrc.zyl;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raincloud on 17-12-13.
 * 歌词行
 * 包括该行歌词的时间，内容
 */

public class LrcRow implements Comparable<LrcRow>{
    public final static String TAG = "LrcRow";

    /** 该行歌词要开始播放的时间，格式如下：[02:34.14] */
    public String strTime;

    /**
     * 该行歌词要开始播放的时间，由[02:34.14]格式转换为long型
     */
    public long time;

    /** 该行歌词的内容 */
    public String content;


    public LrcRow(){}

    public LrcRow(String strTime,long time,String content){
        this.strTime = strTime;
        this.time = time;
        this.content = content;
//      Log.d(TAG,"strTime:" + strTime + " time:" + time + " content:" + content);
    }

    @Override
    public String toString() {
        return "[" + strTime + " ]"  + content;
    }

    /**
     * 读取歌词的每一行内容，转换为LrcRow，加入到集合中
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
            if(standardLrcLine.indexOf("[") != 0 || standardLrcLine.indexOf("]") != 9 ){
                return null;
            }
            //找到最后一个 ‘]’ 的位置
            int lastIndexOfRightBracket = standardLrcLine.lastIndexOf("]");
            //歌词内容就是 ‘]’ 的位置之后的文本
            String content = standardLrcLine.substring(lastIndexOfRightBracket + 1, standardLrcLine.length());
            //歌词时间就是 ‘]’ 的位置之前的文本

            /**
             将时间格式转换一下  [mm:ss.SS][mm:ss.SS] 转换为  -mm:ss.SS--mm:ss.SS-
             即：[02:34.14][01:07.00]  转换为      -02:34.14--01:07.00-
             */
            String times = standardLrcLine.substring(0,lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
            //通过 ‘-’ 来拆分字符串
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
                if(temp.trim().length() == 0){
                    continue;
                }
                LrcRow lrcRow = new LrcRow(temp, timeConvert(temp), content);
                listTimes.add(lrcRow);
            }
            return listTimes;
        }catch(Exception e){
            Log.e(TAG,"createRows exception:" + e.getMessage());
            return null;
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     */
    private static long timeConvert(String timeString){
        //因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        //将字符串 XX:XX.XX 转换为 XX:XX:XX
        timeString = timeString.replace('.', ':');
        //将字符串 XX:XX:XX 拆分
        String[] times = timeString.split(":");
        // mm:ss:SS
        return Integer.valueOf(times[0]) * 60 * 1000 +//分
                Integer.valueOf(times[1]) * 1000 +//秒
                Integer.valueOf(times[2]) ;//毫秒
    }

    /**
     * 排序的时候，根据歌词的时间来排序
     */
    public int compareTo(LrcRow another) {
        return (int)(this.time - another.time);
    }
}