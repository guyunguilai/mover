package cn.ssic.moverlogic.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void addStringToFile(String fileName,String string, Context context){
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
        File destDir = new File(APK_dir + fileName);
        if (!destDir.exists())// 判斷文件夾是否存在
        {
            destDir.mkdirs();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destDir, true));
            bw.write(string);
            bw.write("\r\n");
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStringToFile(String fileName,String string, Context context){
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
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(APK_dir + fileName);
            fos.write(string.getBytes());
            fos.write("\r\n".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Intent getHtmlFileIntent(String Path)
    {
        File file = new File(Path);
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }
    //android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }
    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
    //android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    //android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }
    //android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }


    //android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }


    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }
    //android获取一个用于打开PPT文件的intent
    public static Intent getPPTFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }
    //android获取一个用于打开apk文件的intent
    public static Intent getApkFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    public static final int PIC = 1;
    public static final int DOC = 2;
    public static final int TXT = 3;
    public static final int EXCEL = 4;
    public static final int PPT = 5;
    public static final int WPS = 6;
    public static final int DPS = 7;
    public static final int ET = 8;
    public static final int PDF = 9;
    public static final int ZIP = 10;
    public static final int RAR = 11;
    public static final int TRA = 12;
    public static final int ARJ = 13;
    public static final int FLV = 14;
    public static final int AVI = 15;
    public static final int MP4 = 16;
    public static final int RMVB = 17;
    public static final int WMV = 18;
    public static final int HTML = 19;
    public static final int HTM = 20;
    public static final int TMP = 21;
    public static final int SWF = 22;

    public static void readDocx(){

    }
}
