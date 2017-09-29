package cn.ssic.moverlogic.download;

/**
 * Created by Administrator on 2017/3/2.
 */

public interface DownloadProgressListener  {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
