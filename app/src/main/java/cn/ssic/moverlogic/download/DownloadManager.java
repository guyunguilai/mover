package cn.ssic.moverlogic.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.ssic.moverlogic.netokhttp3.IHttp;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.AppUtil;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/2.
 */

public class DownloadManager {
    static DownloadManager downloadManager;
    Set<DownloadItemInfo> itemInfos;
    HashMap<String,DownloadProgressSubscriber> subscriberHashMap;
//    private DbDownUtil db;

    public DownloadManager(){
        itemInfos = new HashSet<>();
        subscriberHashMap = new HashMap<>();
//        db = DbDownUtil.getInstance();
    }

    public static DownloadManager getInstance(){
        if (downloadManager == null){
            downloadManager = new DownloadManager();
        }
        return downloadManager;
    }

    public void startDownload(DownloadItemInfo itemInfo){
        if (itemInfo == null || subscriberHashMap.get(itemInfo.getUrl()) != null){
            subscriberHashMap.get(itemInfo.getUrl()).setDownInfo(itemInfo);
            return;
        }

        DownloadProgressSubscriber subscriber = new DownloadProgressSubscriber(itemInfo);
        subscriberHashMap.put(itemInfo.getUrl(),subscriber);

        IHttp iHttp ;
        if (itemInfo.getiHttp() != null){
            iHttp = itemInfo.getiHttp();
        }else {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            DownloadInterceptor downloadInterceptor = new DownloadInterceptor(subscriber);
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(itemInfo.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(downloadInterceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(AppUtil.getBasUrl(itemInfo.getUrl()))
                    .build();
            iHttp= retrofit.create(IHttp.class);
            itemInfo.setiHttp(iHttp);
            itemInfos.add(itemInfo);
        }
        iHttp.download("bytes=" + itemInfo.getReadLength() + "-",itemInfo.getUrl())
             .retryWhen(new RetryWhenNetworkException())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
            /*读取下载写入文件*/
            .map(new Func1<ResponseBody, DownloadItemInfo>() {
                @Override
                public DownloadItemInfo call(ResponseBody responseBody) {
                    try {
                        writeCache(responseBody,new File(itemInfo.getSavePath()),itemInfo);
                    } catch (IOException e) {
                        /*失败抛出异常*/
                        throw new HttpTimeException(e.getMessage());
                    }
                    return itemInfo;
                }
            })
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
    }

    /**
     * 写入文件
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCache(ResponseBody responseBody, File file, DownloadItemInfo info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getCountLength()==0){
            allLength=responseBody.contentLength();
        }else{
            allLength=info.getCountLength();
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(),allLength-info.getReadLength());
        byte[] buffer = new byte[1024*8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    public void stopDownload(DownloadItemInfo itemInfo){
        if (itemInfo == null) return;
        itemInfo.sDownload_Status = DownloadItemInfo.DOWNLOAD_STOP;
        itemInfo.getListener().onStop();
        if (subscriberHashMap.containsKey(itemInfo.getUrl())){
            DownloadProgressSubscriber subscriber = subscriberHashMap.get(itemInfo.getUrl());
            subscriber.unsubscribe();
            subscriberHashMap.remove(itemInfo.getUrl());
        }
//        db.save(itemInfo);
    }

    public void pauseDownload(DownloadItemInfo itemInfo){
        if (itemInfo == null) return;
        itemInfo.sDownload_Status = DownloadItemInfo.DOWNLOAD_PAUSE;
        itemInfo.getListener().onPause();
        if (subscriberHashMap.containsKey(itemInfo.getUrl())){
            DownloadProgressSubscriber subscriber = subscriberHashMap.get(itemInfo.getUrl());
            subscriber.unsubscribe();
            subscriberHashMap.remove(itemInfo.getUrl());
        }
//        db.update(itemInfo);
    }

    public void stopAllDownload(){
        for (DownloadItemInfo itemInfo : itemInfos){
            stopDownload(itemInfo);
        }
        subscriberHashMap.clear();
        itemInfos.clear();
    }

    public void pauseAllDownload(){
        for (DownloadItemInfo itemInfo : itemInfos){
            pauseDownload(itemInfo);
        }
        subscriberHashMap.clear();
        itemInfos.clear();
    }

    public void remove(DownloadItemInfo info){
        subscriberHashMap.remove(info.getUrl());
        itemInfos.remove(info);
    }
}
