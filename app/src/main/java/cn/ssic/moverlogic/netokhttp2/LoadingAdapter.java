package cn.ssic.moverlogic.netokhttp2;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/8/30.
 * listener接口的适配器类，通过适配器类实现在调用的地方不用实现全部接口方法，只调用想用的方法，但是要先super。
 */
public class LoadingAdapter implements LoadingListener {
    private Class responseClass;//作为json类型转换的class
    private Context mContext;//有需要全局操作的action通过广播来发送

    public NTRespBean respBean;//转换的class通过respBean进行强制类型转换

    public LoadingAdapter(Context context, Class beanClass) {
        mContext = context;
        responseClass = beanClass;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onLoading(Object o) {

    }

    @Override
    public void onSuccess(Object o) {
        Logger.e(o.toString());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            respBean = gson.fromJson(o.toString(), NTRespBean.class);//做初步判断status是否成功
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == respBean) {//表明未转换成功出现异常
            onFail(respBean);
            return;
        } else {//表明转换成功，进一步进行转换类型
            if (respBean.getStatus().equals("000")) {
                respBean = (NTRespBean) gson.fromJson(o.toString(), responseClass);
                onRespbean(respBean);
                return;
            }

            //添加返回值判断，比如403，500.或者被踢掉登录需要发广播之类。
            if (TextUtils.equals("101", respBean.getStatus())) {
                mContext.sendBroadcast(new Intent(Constants.ACTION_KICKOFF));
                return;
            }

            //添加返回值判断，比如403，500.或者被踢掉登录需要发广播之类。
            if (TextUtils.equals("102", respBean.getStatus())) {
                onFail(respBean);
//                Toast.makeText(mContext,"user do not exist",Toast.LENGTH_LONG).show();
                return;
            }

            //添加返回值判断，比如403，500.或者被踢掉登录需要发广播之类。
            if (TextUtils.equals("103", respBean.getStatus())) {
                onFail(respBean);
//                Toast.makeText(mContext,"wrong password_hint",Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.equals("999", respBean.getStatus())) {
                onFail(respBean);
                return;
            }
            if (TextUtils.equals("108", respBean.getStatus())) {
                onFailNoPermission(respBean);
                return;
            }

        }
        Logger.e("respBean:" + respBean.toString());
        return;
    }

    @Override
    public void onCancle(Object o) {

    }

    @Override
    public void onRespbean(Object o) {

    }

    @Override
    public void onFail(Object o) {
//        Logger.e("返回respBeany异常:"+o.toString());
    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onFailNoPermission(Object o) {

    }

    @Override
    public void onFailIsNull() {

    }
}
