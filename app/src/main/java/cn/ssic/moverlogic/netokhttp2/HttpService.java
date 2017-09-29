package cn.ssic.moverlogic.netokhttp2;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.bean.ReqBean;
import cn.ssic.moverlogic.bean.RespBean;
import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.utils.JsonUtils;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/30.
 */
public class HttpService {
    public Call call;
    public LoadingAdapter mAdapter;
    public Object mTag;
    public HttpService(Object o){
        mTag = o ;
    }
    public  void enqueue(){
        LoadingHandler handler = new LoadingHandler(mAdapter);
        handler.sendEmptyMessage(Constants.NET_LOAD_START);//start的过程是从调用到发送请求之前---OKHttp.excute
    };

    public void callBack(LoadingAdapter adapter){
        mAdapter = adapter;
    }

    public void cancel(){
        OkHttpUtils.cancel(mTag,mAdapter);
    }
}
