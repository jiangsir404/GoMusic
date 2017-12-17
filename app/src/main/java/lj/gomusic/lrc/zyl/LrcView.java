package lj.gomusic.lrc.zyl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import lj.gomusic.lrc.ILrcView;

/**
 * Created by raincloud on 17-12-16.
 */

public class LrcView extends View implements ILrcView {
    public final static String TAG = "LrcView";
    //歌词集合
    private List<LrcRow> mLrcRows;
    //当前高亮歌词行号
    private int mHighlightRow = 0;
    //当前高亮歌词颜色
    private int mHignlightRowColor = Color.YELLOW;
    //不高亮歌词颜色
    private int mNormalRowColor = Color.WHITE;
    //歌词字体大小
    private int mLrcFontSize = 60;
    //歌词行间距
    private int mPaddingY = 20;
    //无歌词时展示的内容
    private String mNoLrcTip = "Cannot find lrc...";
    //画笔
    private Paint mPaint;

    public LrcView(Context context, AttributeSet attr){
        super(context,attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    public void setNoLrcTipText(String text){
        mNoLrcTip = text;
    }

    /**
     * 重写onDraw(Canvas canvas)方法，绘制歌词
     * @param canvas
     */
    protected void onDraw(Canvas canvas){
        final int height = getHeight(); //height of view
        final int width = getWidth();   //width of view

        //无歌词
        if(mLrcRows == null || mLrcRows.size() == 0){
            if(mNoLrcTip != null){
                //draw tip when no lrc
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                //在布局中部显示无歌词的提示
                canvas.drawText(mNoLrcTip,width/2,height/2 - mLrcFontSize,mPaint);
            }
            return;
        }

        int rowY = 0;
        final int rowX = width/2;
        int rowNum = 0;
        /**
         * 分三步绘制歌词
         * 1.绘制正在播放的歌词
         * 2.绘制播放歌词上部壳显示的歌词
         * 3.绘制下面可显示的歌词
         */
        //1.绘制正在播放的歌词
        String highlightText = mLrcRows.get(mHighlightRow).content;
        int highlightRowY = height/2 - mLrcFontSize;
        mPaint.setColor(mHignlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(highlightText,rowX,highlightRowY,mPaint);

        //2.绘制播放歌词上部壳显示的歌词
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        rowNum = mHighlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        //先画一行
//        if(rowY > -mLrcFontSize && rowNum >= 0){
//            String text = mLrcRows.get(rowNum).content;
//            canvas.drawText(text,rowX,rowY,mPaint);
//        }

        //画出上面所有
        while(rowY > -mLrcFontSize && rowNum >= 0){
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text,rowX,rowY,mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum --;
        }

        //3.画出下面的歌词
        rowNum = mHighlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;

        while(rowY < height && rowNum < mLrcRows.size()){
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text,rowX,rowY,mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum ++;
        }
    }
    /**
     * 设置高亮的是第几行歌词
     * position 要高亮的歌词行数
     */
    public void seekLrc(int position){
        if(mLrcRows == null || position < 0 || position > mLrcRows.size()){
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHighlightRow = position;
        invalidate(); //重绘View树
    }

    /**
     * 设置歌词行集合
     */
    public void setLrc(List<LrcRow> lrcRows){
        mLrcRows = lrcRows;
        invalidate();
    }
    /**
     * 播放时调用该方法滚动歌词，高亮正在播放的歌词
     */
    public void seekLrcToTime(long time){
        if(mLrcRows == null || mLrcRows.size() == 0){
            return;
        }
        Log.d(TAG,"seekLrcToTime:" + time);

        for(int i = 0;i < mLrcRows.size();i++){
            LrcRow current = mLrcRows.get(i);
            LrcRow next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i+1);
            /**
             *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
             *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
             */
            if((time >= current.time && next != null && time < next.time)
                    || (time > current.time && next == null)){
                seekLrc(i);
                return;
            }
        }
    }
}
