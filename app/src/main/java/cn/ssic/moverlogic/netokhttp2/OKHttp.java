package cn.ssic.moverlogic.netokhttp2;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.CookieStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.netokhttp3.SslContextFactory;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2016/8/30.
 */
public class OKHttp implements IHttpMethods {
    static SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket(App.app).getSocketFactory();
    private static final OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            })

            .sslSocketFactory(sslSocketFactory, new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            })
            .connectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(new RedirectInterceptor())
            .build();

    static class RedirectInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Response response = chain.proceed(originalRequest);
            return response;
        }
    }

    public void addTag(Request.Builder builder, Object... tag) {
        if (tag != null && tag.length == 1) {
            builder.tag(tag[0]);
        }
    }

    private Object getTag(Object... tag) {
        if (tag != null && tag.length == 1) {
            return tag[0];
        }
        return null;
    }

    @Override
    public Call get(String url, LoadingListener listener, Object... tag) {
        return get(url, null, listener, tag);
    }

    @Override
    public Call synchGet(String url, LoadingListener listener, Object... tag) {
        return synchGet(url, null, listener, tag);
    }

    @Override
    public Call get(String url, HashMap<String, String> header, LoadingListener listener, Object... tag) {
        Request.Builder builder = new Request.Builder().url(url);
        if (null != header) {
            for (HashMap.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        addTag(builder, tag);
        return execute(builder.build(), listener, getTag(tag));
    }

    @Override
    public Call synchGet(String url, HashMap<String, String> header, LoadingListener listener, Object... tag) {
        Request.Builder builder = new Request.Builder().url(url);
        if (null != header) {
            for (HashMap.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        addTag(builder, tag);

        return synchExecute(builder.build(), listener, getTag(tag));
    }

    @Override
    public Call post(String url, HashMap<String, String> header, RequestBody body, LoadingListener listener, Object... tag) {
        if (null != body) {
            Request.Builder builder = new Request.Builder().url(url).post(body);

            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return execute(builder.build(), listener, getTag(tag));
        } else {
            listener.onFail("params is null");
            return null;
        }
    }

    @Override
    public Call synchPost(String url, HashMap<String, String> header, RequestBody body, LoadingListener listener, Object... tag) {
        if (null != body) {
            Request.Builder builder = new Request.Builder().url(url).post(body);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return synchExecute(builder.build(), listener, getTag(tag));
        } else {
            listener.onFail("params is null");
            return null;
        }
    }

    @Override
    public Call uploading(String url, HashMap<String, String> header, String filePath, LoadingListener listener, Object... tag) {
        RequestBody formBody = new MultipartBody.Builder()
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"data\""),
                        RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath))).build();

        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).post(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return execute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call synchUploading(String url, HashMap<String, String> header, String filePath, LoadingListener listener, Object... tag) {
        RequestBody formBody = new MultipartBody.Builder()
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"data\""),
                        RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath))).build();

        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).post(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return synchExecute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call put(String url, HashMap<String, String> header, HashMap<String, Object> map, LoadingListener listener, Object... tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        RequestBody formBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);

//        RequestBody formBody = addParams(params);
        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).put(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return execute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call synchPut(String url, HashMap<String, String> header, HashMap<String, Object> map, LoadingListener listener, Object... tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        RequestBody formBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);

//        RequestBody formBody = addParams(params);
        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).put(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return synchExecute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call delete(String url, HashMap<String, String> header, HashMap<String, Object> map, LoadingListener listener, Object... tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        RequestBody formBody = RequestBody.create(MediaType
                .parse("application/json; charset=utf-8"), jsonStr);

        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).delete(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return execute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call synchDelete(String url, HashMap<String, String> header, HashMap<String, Object> map, LoadingListener listener, Object... tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        RequestBody formBody = RequestBody.create(MediaType
                .parse("application/json; charset=utf-8"), jsonStr);

        if (formBody == null) {
            listener.onFail("params is null");
            return null;
        } else {
            Request.Builder builder = new Request.Builder().url(url).delete(formBody);
            if (header != null) {
                for (HashMap.Entry<String, String> entry : header.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            addTag(builder, tag);
            return synchExecute(builder.build(), listener, getTag(tag));
        }
    }

    @Override
    public Call download(String url, HashMap<String, String> header, LoadingListener listener, Object... tag) {
        Request.Builder builder = new Request.Builder().url(url);
        if (header != null) {
            for (HashMap.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        addTag(builder, tag);
        return executedownload(builder.build(), listener, getTag(tag));
    }

    @Override
    public Call synchDownload(String url, HashMap<String, String> header, LoadingListener listener, Object... tag) {
        Request.Builder builder = new Request.Builder().url(url);
        if (header != null) {
            for (HashMap.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        addTag(builder, tag);
        return synchExecutedownload(builder.build(), listener, getTag(tag));
    }

    @Override
    public Call cancle(LoadingListener listener, Object... tag) {//cancle和request是主线程传过来的俩个方法，所以会出现不同步的问题。onRun和onCancel要判断非空
        LoadingHandler handler = new LoadingHandler(listener);
        TaskPool.getInstance().cancleTask(handler, tag);
        return null;
    }

    @Override
    public Call synchCancle(LoadingListener listener, Object... tag) {
        TaskPool.getInstance().cancleTask(null, tag);
        return null;
    }

    public Call execute(Request request, LoadingListener listener, Object tag) {
        Call call = mOkHttpClient.newCall(request);
        LoadingHandler handler = new LoadingHandler(listener);//这里都还是在UI线程，所以要在这里new handler
        RunningTask taskNow = new RunningTask(tag) {
            @Override
            public void onRun() {
                onTimeAccessHandler(handler);//发通知消息之前必须调用该方法
                notifyLoading(getTag());

                System.out.println("OKHttp-execute-onRun:" + request.url().toString());
                try {
                    Response response = call.execute();
                    notifySuccess(response.body().string());
                    ResponseBody body = response.body();
                    Headers session = response.headers();
                    Set<String> entry = session.names();
                    for (String s : entry){
                        System.out.println(session.get(s));
                    }

                } catch (IOException e) {
                    if (e.toString().toLowerCase().contains("canceled")
                            || e.toString().toLowerCase().contains("closed")) {
                        notifyCancle();
                    } else {
                        notifyFail(e);
                    }
//                    e.printStackTrace();
                }
            }

            @Override
            public void onCancle() {
                System.out.println("OKHttp-execute-onCancle:" + request.url().toString());
                if (null != call) {
                    try {
                        call.cancel();
                    } catch (Exception e) {
                        Logger.e(" java.io.IOException: Canceled");
                    }
                }
            }
        };
        TaskPool.getInstance().excute(taskNow);
        return call;
    }

    public Call synchExecute(Request request, LoadingListener listener, Object tag) {//同步方法直接用listener进行返回
        Call call = mOkHttpClient.newCall(request);
        listener.onLoading(getTag());

        System.out.println("OKHttp-execute-onRun:" + request.url().toString());
        try {
            Response response = call.execute();
            listener.onSuccess(response.body().string());
        } catch (IOException e) {
            if (e.toString().toLowerCase().contains("canceled")
                    || e.toString().toLowerCase().contains("closed")) {
                listener.onCancle(e);
            } else {
                listener.onFail(e);
            }
//                    e.printStackTrace();
        }

        return call;
    }

    public Call executedownload(Request request, LoadingListener listener, Object tag) {
        Call call = mOkHttpClient.newCall(request);
        LoadingHandler handler = new LoadingHandler(listener);//这里都还是在UI线程，所以要在这里new handler
        RunningTask task = new RunningTask(tag) {
            @Override
            public void onRun() {
                System.out.println("OKHttp-executedownload-onRun:" + request.url().toString());
                onTimeAccessHandler(handler);
                try {
                    Response response = call.execute();
                    notifySuccess(response.body().bytes());
                } catch (IOException e) {
                    if (e.toString().toLowerCase().contains("canceled")
                            || e.toString().toLowerCase().contains("closed")) {
                        notifyCancle();
                    } else {
                        notifyFail(e);
                    }
//                    e.printStackTrace();
                }
            }

            @Override
            public void onCancle() {
                System.out.println("OKHttp-executedownload-onCancle:" + request.url().toString());
                call.cancel();
            }
        };
        TaskPool.getInstance().excute(task);
        return call;
    }

    public Call synchExecutedownload(Request request, LoadingListener listener, Object tag) {
        Call call = mOkHttpClient.newCall(request);
        LoadingHandler handler = new LoadingHandler(listener);//这里都还是在UI线程，所以要在这里new handler
        RunningTask task = new RunningTask(tag) {
            @Override
            public void onRun() {
                System.out.println("OKHttp-executedownload-onRun:" + request.url().toString());
                try {
                    Response response = call.execute();
                    listener.onSuccess(response.body().bytes());
                } catch (IOException e) {
                    if (e.toString().toLowerCase().contains("canceled")
                            || e.toString().toLowerCase().contains("closed")) {
                        listener.onCancle(e);
                    } else {
                        listener.onFail(e);
                    }
//                    e.printStackTrace();
                }
            }

            @Override
            public void onCancle() {
                System.out.println("OKHttp-executedownload-onCancle:" + request.url().toString());
                call.cancel();
            }
        };
        TaskPool.getInstance().excute(task);
        return call;
    }
}
