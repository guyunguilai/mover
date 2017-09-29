package cn.ssic.moverlogic.netokhttp2;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.ssic.moverlogic.Constants;

/**
 * Created by Administrator on 2016/8/29.
 */
public class LoadingHandler extends Handler {
    LoadingListener mListener;
    private int mStatus = Constants.NET_LOAD_START;

    public LoadingHandler(LoadingListener listener) {
        mListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //这里需避免重复的请求，比如快速点击发出的俩次请求，或者数据返回时重复调用了success
        mStatus = msg.what;
//        Logger.e("Net Status = " + mStatus);
        Bundle bundle = msg.getData();
        Object o = null;
        if (bundle != null) {
            o = bundle.get("result");
        } else {
//            Logger.e("Net bundle handle no msg ");
        }

        switch (msg.what) {
            case Constants.NET_LOAD_START:
                mListener.onStart();
                break;
            case Constants.NET_LOAD_FAIL:
                mListener.onFail(o);
                break;
            case Constants.NET_LOAD_SUCCESS:
                mListener.onSuccess(o);
                break;
            case Constants.NET_LOAD_LOADING:
                mListener.onLoading(o);
                break;
            case Constants.NET_LOAD_CANCLE:
                mListener.onCancle(o);
                break;
            case Constants.NET_LOAD_TIMEOUT:
                mListener.onTimeOut();
                break;
        }
    }

    public int getNetStatus() {
        return mStatus;
    }
}
