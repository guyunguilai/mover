package cn.ssic.moverlogic.download;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import cn.ssic.moverlogic.netokhttp3.IHttp;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;

/**
 * Created by Administrator on 2017/3/2.
 */
@Entity
public class DownloadItemInfo {
    /*存储位置*/
    @Id
    private long id;

    private String savePath;
    @Transient
    public IHttp iHttp;
    /*下载url*/
    private String url;
    /*基础url*/
    private String baseUrl;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*回调监听*/
    @Transient
    private HttpProgressOnNextListener listener;
    public int sDownload_Status = 0;
    @Transient
    public static final int DOWNLOAD_START = 10;
    @Transient
    public static final int DOWNLOAD_ING = 11;
    @Transient
    public static final int DOWNLOAD_PAUSE = 12;
    @Transient
    public static final int DOWNLOAD_STOP = 13;
    @Transient
    public static final int DOWNLOAD_ERROR = 14;
    @Transient
    public static final int DOWNLOAD_FINISH = 15;
    /*超时设置*/
    private int connectonTime = 6;

    @Generated(hash = 594734836)
    public DownloadItemInfo(long id, String savePath, String url, String baseUrl,
            long countLength, long readLength, int sDownload_Status,
            int connectonTime) {
        this.id = id;
        this.savePath = savePath;
        this.url = url;
        this.baseUrl = baseUrl;
        this.countLength = countLength;
        this.readLength = readLength;
        this.sDownload_Status = sDownload_Status;
        this.connectonTime = connectonTime;
    }

    @Generated(hash = 2084943347)
    public DownloadItemInfo() {
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public IHttp getiHttp() {
        return iHttp;
    }

    public void setiHttp(IHttp iHttp) {
        this.iHttp = iHttp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public HttpProgressOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpProgressOnNextListener listener) {
        this.listener = listener;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public int getSDownload_Status() {
        return this.sDownload_Status;
    }

    public void setSDownload_Status(int sDownload_Status) {
        this.sDownload_Status = sDownload_Status;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
