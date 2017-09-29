package cn.ssic.moverlogic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.ssic.moverlogic.App;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class MultipartUtil {


    /**
     * 单张上传
     * @param path
     * @param key
     * @return
     */
    public static Map<String, RequestBody> getFilesBody(String path, String key) {

        Map<String, RequestBody> bodys = new IdentityHashMap<>();

        File file = new File(path);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
        bodys.put(key + "\"; filename=\"" + file.getName() + "", requestBody);

        return bodys;

    }

    public static Map<String, Object> getFilesBodyReturnObject(String path, String key) {

        Map<String, Object> bodys = new IdentityHashMap<>();

        File file = new File(path);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
        bodys.put(key + "\"; filename=\"" + file.getName() + "", requestBody);

        return bodys;

    }

    /**
     * 批量上传
     * 生成上传文件
     * 纯文件
     *
     * @param path
     * @return
     */
    public static Map<String, RequestBody> getFilesBody(List<String> path, String key) {
        Map<String, RequestBody> bodys = new IdentityHashMap<>();
        List<File> files = getFiles(path);
        for (File file : files) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
            bodys.put(key + "\"; filename=\"" + file.getName() + "", requestBody);
        }
        return bodys;
    }

    static File optimizeImageFiles(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = ImageUtils.compressScale(bitmap);
        File file = new File(path.substring(0,path.lastIndexOf("/")) + "/optimized/");
        if (!file.exists()){
            file.mkdir();
        }
        File file1 = new File(path.substring(0,path.lastIndexOf("/")) + "/optimized",path.substring(path.lastIndexOf("/")));
        Log.i("file",path.substring(0,path.lastIndexOf("/")) + "/optimized"+path.substring(path.lastIndexOf("/")));
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
            if (path.contains("png")){
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            }else if (path.contains("jpg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file1;
    }

    private static List<File> getFiles(List<String> path) {
        List<File> files = new ArrayList<>();
        for (String s : path) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            File file = optimizeImageFiles(s);
            if (file.exists())
                files.add(file);
        }
        return files;
    }

    /**
     * 带其他参数类型
     *
     * @param path
     * @param options
     * @param fileKey
     * @return
     */
    public static Map<String, RequestBody> getFilesRequestBody(List<String> path, Map<String, String> options, String fileKey) {
        Map<String, RequestBody> bodys = new IdentityHashMap<>();
        Iterator<Map.Entry<String, String>> iterator = options.entrySet().iterator();
        //遍历添加普通参数
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            bodys.put(next.getKey(), RequestBody.create(MediaType.parse("text/plain"), next.getValue()));

        }
        if (null != path && path.size() > 0) {
            List<File> files = getFiles(path);
            for (File file : files) {
                RequestBody fileBody =
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                //bodys.put("name=\"\"; filename=\"7411759_164157418126_2.jpg", fileBody);
                bodys.put(fileKey + "\"; filename=\"" + file.getName() + "", fileBody);


            }
        }
        //添加文件参数

        return bodys;
    }


    /**
     * 带其他参数类型
     *
     * @param path
     * @param options
     * @param fileKey
     * @return
     */
    public static Map<String, RequestBody> getFilesRequestBody(String path, Map<String, String> options, String fileKey) {
        List<String> files = new ArrayList<>();
        files.add(path);


        return getFilesRequestBody(files, options, fileKey);
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
