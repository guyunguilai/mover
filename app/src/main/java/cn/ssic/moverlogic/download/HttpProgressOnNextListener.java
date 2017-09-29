package cn.ssic.moverlogic.download;

/**
 * Created by Administrator on 2017/3/2.
 */

public abstract class HttpProgressOnNextListener<T> {
    public abstract void onNext(T t);
    public abstract void onStart();
    public abstract void onComplete();
    public abstract void onError(Throwable e);
    public abstract void onPause();
    public abstract void onStop();
    public abstract void updateProgress(long readLength, long countLength);

}
