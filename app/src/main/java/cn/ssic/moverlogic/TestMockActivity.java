package cn.ssic.moverlogic;

import android.app.Activity;
import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cn.ssic.moverlogic.bean.RespBean;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/8.
 */

public class TestMockActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new OKHTTPLogInterceptor());
//        builder.sslSocketFactory(SSLContext.getInstance("TLS").getSocketFactory(),new TrustManagerFactory())

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://10.10.11.155:5638")
                .build();

        HttpService service = retrofit.create(HttpService.class);
        Observable<RespBean> observable = service.login("阿斯顿法国和健康");
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<RespBean>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(RespBean responseBean) {
                        System.out.println("onNext" + responseBean.toString());
                    }
                });
    }
}

interface HttpService{
    @POST("/login")
    @FormUrlEncoded
    Observable<RespBean> login(@Field(value = "name",encoded=true) String name);
}
class OKHTTPLogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println(request.url().url().toString());

        okhttp3.Response response = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        if (response.body() != null) {
            // 深坑！
            // 打印body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = ResponseBody.create(mediaType, content);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }
}
