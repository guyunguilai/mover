package cn.ssic.moverlogic.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/2.
 */

public class DownloadInterceptor implements Interceptor {
    DownloadProgressListener progressListener;
    public DownloadInterceptor(DownloadProgressListener listener){
        progressListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response
                .newBuilder()
                .body(new DownloadResponseBody(response.body(),progressListener))
                .build();
    }
}
