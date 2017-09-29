package cn.ssic.moverlogic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import me.nereo.multi_image_selector.utils.FileUtils;

/**
 * Created by Administrator on 2016/11/25.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    public Context mContext;
    public static CrashHandler instance;
    public Map<String ,String> deviceInfo = new HashMap<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss.sss");
    Thread.UncaughtExceptionHandler exceptionHandler;
    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        if (instance == null){
            instance = new CrashHandler();
        }
        return instance;
    }

    public void initCrashHandler(Context c){
        mContext = c;
        exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e == null) return;
        Logger.d(TAG,"code exception，find it"+e.getMessage());
        boolean isHandled = handleException(e);
        if (!isHandled && exceptionHandler !=null){
            exceptionHandler.uncaughtException(t,e);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    public boolean handleException(Throwable th){
        if (th == null) return false;
        collectDeviceInfo();
        new Thread("CrashHanlder Thread"){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                th.printStackTrace();
                Looper.loop();
            }
        }.start();
        FileUtils.saveExceptionToFile(th,deviceInfo,App.app);
        return true;
    }

    public void collectDeviceInfo(){
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
            if (pi != null){
                String versionName = pi.versionName;
                int versionCode = pi.versionCode;

                deviceInfo.put("versionName",versionName);
                deviceInfo.put("versionCode",versionCode+"");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,e.getMessage());
        }

        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        Field[] fields = Build.class.getDeclaredFields();
        for(Field f:fields){
            try {
                // 取消java的权限控制检查,即使是public方法，其accessible 属相默认也是false
                f.setAccessible(true);
                deviceInfo.put(f.getName(),f.get(null).toString());
                Log.e(TAG,f.getName()+" : "+ f.get(null));
            } catch (IllegalAccessException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }
}
