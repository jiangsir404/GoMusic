package lj.gomusic.lrc;

import lj.gomusic.lrc.zyl.LrcRow;

import java.util.List;

/**
 * Created by raincloud on 17-12-15.
 * 解析歌词，得到LrcRow的List
 */

public interface ILrcBuilder {
    List<LrcRow> getLrcRows(String rawLrc);
}
