package cn.ssic.moverlogic.netokhttp2;

import android.text.TextUtils;


import java.util.HashMap;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.utils.SharedprefUtil;
import cn.ssic.moverlogic.utils.UUIDGenerator;
import okhttp3.Call;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/8/30.
 */
public class OkHttpUtils {
    private static IHttpMethods httpMethods = new OKHttp();

    public static Call get(Object tag, String url, LoadingListener listener){
        return get(tag,url,null,listener);
    }
    //get方法通常向服务器获取资源，所以不带body
    public static Call get(Object tag,String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();//所有方法header是必须添加的。
        return get(tag, url, header,  params,listener);
    }

    //get方法通常向服务器获取资源，所以不带body
    public static Call synchGet(Object tag,String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();//所有方法header是必须添加的。
        return synchGet(tag, url, header,  params,listener);
    }

    private static Call get(Object tag, String url, HashMap<String, String> header, java.util.HashMap<String, Object> params,LoadingListener listener){
        String urpParams = getParams(params);
        url = url + urpParams;//拼接url "?"后面的key==value;需要拼接时，原地址一定要带有"?",因为拼接方法不自动生成 "?"
        url = gethostUrl(url);//测试地址还是真实地址
        return httpMethods.get(url, header,listener,tag);
    }

    private static Call synchGet(Object tag, String url, HashMap<String, String> header, java.util.HashMap<String, Object> params,LoadingListener listener){
        String urpParams = getParams(params);
        url = url + urpParams;//拼接url "?"后面的key==value;需要拼接时，原地址一定要带有"?",因为拼接方法不自动生成 "?"
        url = gethostUrl(url);//测试地址还是真实地址
        return httpMethods.synchGet(url, header,listener,tag);
    }

    public static Call download(Object tag, String url,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return httpMethods.download(url, header, listener, tag);
    }

    public static Call synchDownload(Object tag, String url,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return httpMethods.synchDownload(url, header, listener, tag);
    }

    //POST方法要求被请求服务器接受附在请求后面的数据，常用于提交表单。所以post可带body，也可不带body
    //post通过url拼接完成的请求不限定body。比如登录接口可以在url中拼接完成用户名密码的同时也带在body中发送给服务器
    public static Call post(Object tag, String url, RequestBody body, LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return post(tag, url, header, body, listener);
    }

    public static Call synchPost(Object tag, String url, RequestBody body, LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return synchPost(tag, url, header, body, listener);
    }
    private static Call post(Object tag, String url,HashMap<String, String> header,RequestBody body,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.post( url,header,body, listener,tag);
    }

    private static Call synchPost(Object tag, String url,HashMap<String, String> header,RequestBody body,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.synchPost( url,header,body, listener,tag);
    }

    public static Call uploading(Object tag, String url,String filePath,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return uploading(tag, url, header, filePath, listener);
    }
    public static Call synchUploading(Object tag, String url,String filePath,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return synchUploading(tag, url, header, filePath, listener);
    }
    private static Call uploading(Object tag, String url,HashMap<String, String> header,String filePath,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.uploading( url,header,filePath, listener,tag);
    }
    private static Call synchUploading(Object tag, String url,HashMap<String, String> header,String filePath,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.synchUploading( url,header,filePath, listener,tag);
    }

    public static Call put(Object tag, String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return put(tag, url, header, params, listener);
    }
    public static Call synchPut(Object tag, String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return synchPut(tag, url, header, params, listener);
    }
    private static Call put(Object tag, String url,HashMap<String, String> header,HashMap<String, Object> params,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.put( url,header,params, listener,tag);
    }
    private static Call synchPut(Object tag, String url,HashMap<String, String> header,HashMap<String, Object> params,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.synchPut( url,header,params, listener,tag);
    }

    public static Call delete(Object tag, String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return delete(tag, url, header, params, listener);
    }
    public static Call synchDelete(Object tag, String url,HashMap<String, Object> params,LoadingListener listener){
        HashMap<String, String> header = addHeader();
        return synchDelete(tag, url, header, params, listener);
    }
    private static Call delete(Object tag, String url,HashMap<String, String> header,HashMap<String, Object> params,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.delete( url,header,params, listener,tag);
    }
    private static Call synchDelete(Object tag, String url,HashMap<String, String> header,HashMap<String, Object> params,LoadingListener listener){
        url = gethostUrl(url);
        return httpMethods.synchDelete( url,header,params, listener,tag);
    }
    public static Call cancel(Object tag,LoadingListener listener){
        return httpMethods.cancle(listener,tag);
    }
    public static Call synchCancel(Object tag,LoadingListener listener){
        return httpMethods.synchCancle(listener,tag);
    }

    /**
     * @param @param asyncHttpClient 设定文件
     * @return void 返回类型
     * @throws
     * @Title: httpHeader
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static HashMap<String, String> addHeader() {

//        String AppVersion = AppInfo.getInstance().getVersionName();// 客户端版本
//
//        String PlatformVersion = AppInfo.getInstance().getPhoneVersion();// 平台版本
//
//        String Market = AppInfo.getInstance().getAppMarketId();
//
//        String App_Version ="yuepeng-" + AppVersion + "-android-" + PlatformVersion + "-" + Market;
//
//        String RequestId = UUIDGenerator.getUUID();
//
//        String ClientId = AppInfo.getInstance().getMacAddress()
//
//        long Timestamp = TimeUtil.getHundredNanosecond();
//
//        String SecretKey = "chono_rocks";
//
//        String AppSign = SHA.encodeSHA1(App_Version + RequestId + ClientId + SHA.encodeSHA1(SecretKey + ClientId) + Timestamp);
//
        HashMap<String, String> header = new HashMap<>();

//        header.put("ClientId", ClientId);
//        header.put("Timestamp", Timestamp + "");
//        header.put("AppVersion", App_Version);
//        header.put("RequestId", RequestId);

        if (null != App.app.getLoginUser()) {
            header.put("token", App.app.getLoginUser().getToken());
        }
        return header;
    }

    private static String getParams(HashMap<String, Object> linkedHashMap) {

        StringBuffer result = new StringBuffer();
        if (null != linkedHashMap) {
            for (HashMap.Entry<String, Object> entry : linkedHashMap.entrySet()) {
                if (result.length() > 0) result.append("&");
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }
        }
        return result.toString();
    }
    public static String gethostUrl(String url) {
        return HttpConstants.TEST_URL + url;
    }
}
