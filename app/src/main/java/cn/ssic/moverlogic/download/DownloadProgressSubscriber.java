package cn.ssic.moverlogic.download;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/3/2.
 */

public class DownloadProgressSubscriber<T> extends Subscriber<T> implements DownloadProgressListener{
    private SoftReference<HttpProgressOnNextListener> mSubscriberOnNextListener;
    private DownloadItemInfo info;

    @Override
    public void onStart() {
        if (mSubscriberOnNextListener.get() != null){
            mSubscriberOnNextListener.get().onStart();
        }
        info.sDownload_Status = DownloadItemInfo.DOWNLOAD_START;
    }

    public DownloadProgressSubscriber(DownloadItemInfo itemInfo){
        mSubscriberOnNextListener = new SoftReference<>(itemInfo.getListener());
        info = itemInfo;
    }
    public void setDownInfo(DownloadItemInfo itemInfo) {
        mSubscriberOnNextListener = new SoftReference<>(itemInfo.getListener());
        info=itemInfo;
    }
    @Override
    public void update(long read, long count, boolean done) {
        if (info.getCountLength() > count){
            read = info.getCountLength() - count + read;
        }else {
            info.setCountLength(count);
        }
        info.setReadLength(read);
        if (mSubscriberOnNextListener.get() != null) {
//            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            rx.Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                      /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                            if(info.sDownload_Status==DownloadItemInfo.DOWNLOAD_PAUSE
                                    ||info.sDownload_Status==DownloadItemInfo.DOWNLOAD_STOP)
                                return;
                            info.sDownload_Status = DownloadItemInfo.DOWNLOAD_ING;
                            mSubscriberOnNextListener.get().updateProgress(aLong,info.getCountLength());
                        }
                    });
        }
    }

    @Override
    public void onCompleted() {
        if (mSubscriberOnNextListener.get() != null){
            mSubscriberOnNextListener.get().onComplete();
        }
        DownloadManager.getInstance().remove(info);
        info.sDownload_Status = DownloadItemInfo.DOWNLOAD_FINISH;
//        DbDownUtil.getInstance().update(info);
    }

    @Override
    public void onError(Throwable e) {
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onError(e);
        }
        DownloadManager.getInstance().remove(info);
        info.sDownload_Status = DownloadItemInfo.DOWNLOAD_ERROR;
//        DbDownUtil.getInstance().update(info);
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }
}
