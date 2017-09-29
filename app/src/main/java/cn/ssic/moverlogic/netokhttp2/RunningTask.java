package cn.ssic.moverlogic.netokhttp2;

import android.os.Bundle;
import android.os.Message;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import cn.ssic.moverlogic.Constants;

/**
 * Created by Administrator on 2016/8/29.
 */
public abstract class RunningTask implements Runnable {
    private WeakReference<Object> mTag;
    LoadingHandler mHandler;
    private String mTaskName;
    boolean isCanceled;

    public RunningTask(Object tag){
        init(tag.toString(),tag);
    }

    void init(String name,Object tag){
        setTaskName(name);
        setTag(tag);
    }

    public abstract void onRun();
    public abstract void onCancle();

    @Override
    public void run() {
        mLifeCycleListener.onStart(this);
        onRun();
        clear();
        mLifeCycleListener.onFinish(this);
    }

    void notifyStart(){
        sendMsg(Constants.NET_LOAD_START,null);
    }

    void notifyLoading(Object result){
        sendMsg(Constants.NET_LOAD_LOADING,result);
    }

    void notifySuccess(Object result){
        sendMsg(Constants.NET_LOAD_SUCCESS,result);
    }

    void notifyFail(Object result){
        sendMsg(Constants.NET_LOAD_FAIL,result);
    }

    void notifyCancle(){
        sendMsg(Constants.NET_LOAD_CANCLE,null);
    }

    RunningTask setTaskName(String name){
        mTaskName = name;
        return this;
    }
    RunningTask setTag(Object tag){
        mTag = new WeakReference<Object>(tag);
        return this;
    }

    public String getmTaskName(){
        return mTaskName;
    }

    public Object getTag() {
        if (mTag == null) {
            return null;
        } else {
            return mTag.get();
        }
    }

    void sendMsg(int status,Object result){
        if (isCanceled){//如果是cancel动作先进来，表明该请求被取消，那么hanlder就可以丢弃不处理。同时也可以避免多个地方调用cancel方法（比如在异常里捕捉到的cancel动作）
//            Logger.e("task is isCanceled");
            return;
        }

        if (null == mHandler){
//            Logger.e("handler is null");
            return;
        }

        if (status == Constants.NET_LOAD_CANCLE){
            isCanceled = true;
        }

        if (result != null){
            Message msg = Message.obtain();
            msg.what = status;
            Bundle bundle = new Bundle();
            bundle.putSerializable("result",(Serializable)result);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }else {
            mHandler.sendEmptyMessage(status);
        }
    }

    void clear(){
        if (null != mTag){
            mTag.clear();
            mTag = null;
        }
        if (null != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    void cancle(){//cancel的过程十分不稳定，可能在run方法的任意一个地方被中断。所以只能在最终发消息的地方进行丢弃
        onCancle();
        notifyCancle();
        clear();
    }
    private boolean isAccessed = false;
    public void onTimeAccessHandler(LoadingHandler handler){//变量一次访问的控制。要么正常请求给handler赋值，要么cancel动作给hanlder赋值
        if (!isAccessed){
            mHandler = handler;
            isAccessed = true;
        }
    }

    TaskLifeCycleListener mLifeCycleListener;
    public void setLiftCycleListener(TaskLifeCycleListener lifeCycleListener) {
        mLifeCycleListener = lifeCycleListener;
    }
    public interface TaskLifeCycleListener {//任务监听器
        void onStart(RunningTask task);

        void onFinish(RunningTask task);
    }

}
