package cn.ssic.moverlogic.netokhttp3;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.bean.RespBean;
import cn.ssic.moverlogic.utils.DialogUtil;
import cn.ssic.moverlogic.utils.SharedprefUtil;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *  Created by SQ on 16/7/12.
 */
public class RxResultHelper {



    public static <T> Observable.Transformer<RespBean<T>, T> handleResult() {
        return apiResponseObservable -> apiResponseObservable.flatMap(new Func1<RespBean<T>, Observable<T>>() {

            @Override
            public Observable<T> call(RespBean<T> tApiResponse) {
                if ("000".equals(tApiResponse.getStatus())) {
                    //表示成功
                    return createData(tApiResponse.getResp());
                }else if ("555".equals(tApiResponse.getStatus())){
                    return createData(tApiResponse.getResp());
                }else if(TextUtils.equals("401", tApiResponse.getStatus()) || TextUtils.equals("403", tApiResponse.getStatus())){
                    return Observable.error(new Exception(tApiResponse.getMsg()));
                }else if(TextUtils.equals("101", tApiResponse.getStatus())){
                    Intent intent = new Intent(Constants.ACTION_KICKOFF);
                    intent.putExtra("message",tApiResponse.getStatus());
                    App.app.sendBroadcast(intent);
                    SharedprefUtil.getInstance(App.app).save("uid","");
                    SharedprefUtil.getInstance(App.app).save("token","");
                    SharedprefUtil.getInstance(App.app).save("companyName","");
                    SharedprefUtil.getInstance(App.app).save("email","");
                    SharedprefUtil.getInstance(App.app).save("firstName","");
                    SharedprefUtil.getInstance(App.app).save("lastName","");
                    SharedprefUtil.getInstance(App.app).save("phone","");
                    SharedprefUtil.getInstance(App.app).save("photoUrl","");
                    SharedprefUtil.getInstance(App.app).save("staffId","");
                    SharedprefUtil.getInstance(App.app).save("surname","");
                    SharedprefUtil.getInstance(App.app).save("companyId","");
                    SharedprefUtil.getInstance(App.app).save("isAlert","");
                    SharedprefUtil.getInstance(App.app).saveInt("pwdSize",0);
                    return Observable.error((new Exception(tApiResponse.getMsg())));
                }
                else if(TextUtils.equals("106", tApiResponse.getStatus())){
                    Intent intent = new Intent(Constants.ACTION_KICKOFF);
                    App.app.sendBroadcast(intent);
                    SharedprefUtil.getInstance(App.app).save("uid","");
                    SharedprefUtil.getInstance(App.app).save("token","");
                    SharedprefUtil.getInstance(App.app).save("companyName","");
                    SharedprefUtil.getInstance(App.app).save("email","");
                    SharedprefUtil.getInstance(App.app).save("firstName","");
                    SharedprefUtil.getInstance(App.app).save("lastName","");
                    SharedprefUtil.getInstance(App.app).save("phone","");
                    SharedprefUtil.getInstance(App.app).save("photoUrl","");
                    SharedprefUtil.getInstance(App.app).save("staffId","");
                    SharedprefUtil.getInstance(App.app).save("surname","");
                    SharedprefUtil.getInstance(App.app).save("companyId","");
                    SharedprefUtil.getInstance(App.app).save("isAlert","");
                    SharedprefUtil.getInstance(App.app).saveInt("pwdSize",0);
                    return Observable.error((new Exception(tApiResponse.getMsg())));
                }else if(TextUtils.equals("107", tApiResponse.getStatus())){
                    Intent intent = new Intent(Constants.ACTION_KICKOFF);
                    intent.putExtra("message",tApiResponse.getStatus());
                    App.app.sendBroadcast(intent);
                    SharedprefUtil.getInstance(App.app).save("uid","");
                    SharedprefUtil.getInstance(App.app).save("token","");
                    SharedprefUtil.getInstance(App.app).save("companyName","");
                    SharedprefUtil.getInstance(App.app).save("email","");
                    SharedprefUtil.getInstance(App.app).save("firstName","");
                    SharedprefUtil.getInstance(App.app).save("lastName","");
                    SharedprefUtil.getInstance(App.app).save("phone","");
                    SharedprefUtil.getInstance(App.app).save("photoUrl","");
                    SharedprefUtil.getInstance(App.app).save("staffId","");
                    SharedprefUtil.getInstance(App.app).save("surname","");
                    SharedprefUtil.getInstance(App.app).save("companyId","");
                    SharedprefUtil.getInstance(App.app).save("isAlert","");
                    SharedprefUtil.getInstance(App.app).saveInt("pwdSize",0);

                    return Observable.error((new Exception(tApiResponse.getMsg())));
                }else{
                    return Observable.error(new Exception(tApiResponse.getMsg()));
                }
            }
        });
    }

    public static <T> Observable<T> createData(T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private final static Observable.Transformer ioTransformer = o -> ((Observable)o).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
//    private final static Observable.Transformer ioTransformer = o -> ((Observable)o).unsubscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread());


    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }

//    Func1 funcException = new Func1<Throwable, Observable>() {
//        @Override
//        public Observable call(Throwable o) {
//            return Observable.switchOnNextDelayError();
//        }
//    };
}

