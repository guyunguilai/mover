package me.nereo.multi_image_selector.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 文件操作类
 * Created by cc on 2015/4/8.
 */
public class FileUtils {

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static File createTmpFile(Context context) throws IOException{
        File dir = null;
        if(TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }


    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link android.content.Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> (if card is mounted and app has appropriate permission) or
     * on device's file system depending incoming parameters.
     *
     * @param context        Application context
     * @param preferExternal Whether prefer external location for cache
     * @return Cache {@link File directory}.<br />
     * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
     * {@link android.content.Context#getCacheDir() Context.getCacheDir()} returns null).
     */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
     * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
     * appropriate permission. Else - Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
     * @return Cache {@link File directory}
     */
    public static File getIndividualCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(appCacheDir, cacheDir);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static String saveExceptionToFile(Throwable th,Map<String,String> deviceInfo,Context context){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,String> entry: deviceInfo.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value +"\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        th.printStackTrace(printWriter);
        Throwable throwable = th.getCause();
        while (throwable != null){
            throwable.printStackTrace(printWriter);
            throwable = throwable.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS");
        try {
            String time = dateFormat.format(new Date());
            String fileName = "crash-" + time.trim() + "-" + "log"
                    + ".txt";
            // 判斷是否插入SD卡
            String APK_dir = "";
            String status = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                APK_dir = context.getFilesDir().getAbsolutePath()
                        + context.getPackageName();// 保存到app的包名路徑下
            } else {
                APK_dir = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/mover/";// 保存到SD卡路径下
            }
            File destDir = new File(APK_dir);
            if (!destDir.exists())// 判斷文件夾是否存在
            {
                destDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(APK_dir + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
            // 发送Logcat到服务器
            readAndSendLogCat(APK_dir + fileName,context);
            return fileName;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return "";
    }

    public static void readAndSendLogCat(String path,Context context){
        if (!new File(path).exists()) {
            Toast.makeText(context, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        // File cr = new File(mContext.getFilesDir(), fileName);
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            // 打印到本地
            while (true) {
                s = reader.readLine();
                if (s == null)
                    break;
                // 打出log日誌。
                Log.d(TAG, s.toString());
            }
            // 发送到服务器
            // if (isNetworkAvailable(mContext)) {
            // postReport();
            // cr.delete();
            // }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // 关闭流
            try {
                reader.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readAndSendLogCat(Context context) {
        String[] crFiles = getCrashReportFiles(context);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles) {
                File cr = new File(context.getFilesDir(), fileName);
                if (isNetworkAvailable(context)) {
                    postReport();
                    cr.delete();// 删除已发送的报告
                }
            }
        }
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private static String[] getCrashReportFiles(Context ctx) {
        // 判斷是否插入SD卡
        String APK_dir = "";
        String status = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            APK_dir = ctx.getFilesDir().getAbsolutePath()
                    + ctx.getPackageName();// 保存到app的包名路徑下
        } else {
            APK_dir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/debug/";// 保存到SD卡路径下
        }

        File filesDir = new File(APK_dir);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        return filesDir.list(filter);
    }

    private static void postReport() {
        // 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
        // 教程来提交错误报告
    }
    static String TAG = "CrashHandler";
    /**
     * 打印错误的信息
     */
    protected static void calledMethodBringer(Throwable ex) {
        Log.d(TAG, "stackTrace by Throwable Exception :");
        String stackTrace = android.util.Log.getStackTraceString(ex);
        Log.d(TAG, stackTrace);
        //
        Log.d(TAG, "printStackTrace:");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        pw.flush();
        sw.flush();
        String sTemp = sw.toString();
        Log.d(TAG, sTemp);
    }

    /**
     * 检测网络连接是否可用
     *
     * @param ctx
     * @return true 可用; false 不可用
     */
    private static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (int i = 0; i < netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }
}
