package cn.ssic.moverlogic.netokhttp3;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.utils.NetWorkUtil;
import cn.ssic.moverlogic.utils.UUIDGenerator;
import okhttp3.CacheControl;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RetrofitHttp {
    private static IHttp mHttp;
    private static RetrofitHttp mRetrofitHttp;
    private static final Object WATCH = new Object();
    private static Retrofit retrofit;
    String url;
    public RetrofitHttp(){
        url = HttpConstants.TEST_URL;

        retrofit = new Retrofit.Builder().baseUrl(url)
                .client(getClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mHttp = retrofit.create(IHttp.class);
    }

    public static IHttp getInstance(){
        if (null == mHttp){
            synchronized (WATCH){
                mRetrofitHttp = new RetrofitHttp();//new的过程要等到app初始化加载完成，才能将app信息add入header中。
            }
        }
        return mHttp;
    }

    public static RetrofitHttp getRetrofitHttpInstance(){
        if (null == mRetrofitHttp){
            mRetrofitHttp = new RetrofitHttp();
        }
        return mRetrofitHttp;
    }

    public void setRetrofitUrl(String newUrl){
        retrofit = new Retrofit.Builder().baseUrl(newUrl)
                .client(getClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mHttp = retrofit.create(IHttp.class);
    }

    public OkHttpClient getClient(){
        return getClientBuilder().build();
    }

    public Interceptor getHeader(){
        Interceptor apiKey;
        if (null != App.app.getLoginUser()){
            apiKey  = chain -> chain.proceed(chain.request().newBuilder()
                    .addHeader("token",App.app.getLoginUser().getToken())
                    .addHeader("registrationId", App.app.uuid+"")
                    .addHeader("uid", App.app.getLoginUser().getUid()+"")
                    .build());
        }else {
            apiKey  = chain -> chain.proceed(chain.request().newBuilder()
                    .build());
        }
        return apiKey;
    }

    public OkHttpClient.Builder getClientBuilder(){
//                    File cacheFile = newmsg File(App.getInstance().getCacheDir(), "cache");
//                    Cache cache = newmsg Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        loggingInterceptor.setLevel(level);
        SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket(App.app).getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.readTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.connectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.addInterceptor(getHeader());
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new OKHTTPLogInterceptor());//应用层拦截器，打印返回的完整数据
//                .addNetworkInterceptor(newmsg HttpCacheInterceptor())//网络层拦截器，无网络情况下强制返回cache
//                .cache(cache)
          builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        return builder;
    }

    HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
    //新建log拦截器
    HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            System.out.println(message);
        }
    });


    class OKHTTPLogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger.e(request.url().url().toString());
            okhttp3.Response response = chain.proceed(chain.request());

            okhttp3.MediaType mediaType = response.body().contentType();

            String content = response.body().string();
//            if (content.length() > 500){
//                Logger.e(content.substring(0,500));
//                Logger.e(content.substring(500,content.length()));
//            }else {
//                Logger.e(content);
//            }

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


    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(App.app)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();

                Logger.e("no network");

            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(App.app)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
