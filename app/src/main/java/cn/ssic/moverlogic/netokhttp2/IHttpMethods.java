package cn.ssic.moverlogic.netokhttp2;


import java.util.HashMap;

import okhttp3.Call;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2016/8/30.
 */
public interface IHttpMethods {
    public Call get(String url, LoadingListener listener, Object ...tag);
    public Call synchGet(String url, LoadingListener listener, Object ...tag);
    public Call get(String url, HashMap<String, String> header, LoadingListener listener, Object ...tag);
    public Call synchGet(String url, HashMap<String, String> header, LoadingListener listener, Object ...tag);
    public Call post(String url, HashMap<String, String> header, RequestBody body, LoadingListener listener, Object ...tag);
    public Call synchPost(String url, HashMap<String, String> header, RequestBody body, LoadingListener listener, Object ...tag);
    public Call uploading(String url, HashMap<String, String> header, String filePath,  LoadingListener listener, Object ...tag);
    public Call synchUploading(String url, HashMap<String, String> header, String filePath,  LoadingListener listener, Object ...tag);
    public Call put(String url, HashMap<String, String> header,HashMap<String, Object> map,  LoadingListener listener, Object ...tag);
    public Call synchPut(String url, HashMap<String, String> header,HashMap<String, Object> map,  LoadingListener listener, Object ...tag);
    public Call delete(String url, HashMap<String, String> header,HashMap<String, Object> map,  LoadingListener listener, Object ...tag);
    public Call synchDelete(String url, HashMap<String, String> header,HashMap<String, Object> map,  LoadingListener listener, Object ...tag);
    public Call download(String url, HashMap<String, String> header,LoadingListener listener, Object ...tag);
    public Call synchDownload(String url, HashMap<String, String> header,LoadingListener listener, Object ...tag);
    public Call cancle(LoadingListener listener,Object... tag);
    public Call synchCancle(LoadingListener listener,Object... tag);
}
