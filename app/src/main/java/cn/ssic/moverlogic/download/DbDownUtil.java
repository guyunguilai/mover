package cn.ssic.moverlogic.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.ssic.moverlogic.App;


/**
 * 断点续传
 * 数据库工具类-geendao运用
 * Created by WZG on 2016/10/25.
 */

public class DbDownUtil {
    private static DbDownUtil db;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;


    public DbDownUtil() {
        context= App.app;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    /**
     * 获取单例
     * @return
     */
    public static DbDownUtil getInstance() {
        if (db == null) {
            synchronized (DbDownUtil.class) {
                if (db == null) {
                    db = new DbDownUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void save(DownloadItemInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownloadItemInfoDao downInfoDao = daoSession.getDownloadItemInfoDao();
        downInfoDao.insert(info);
    }

    public void update(DownloadItemInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownloadItemInfoDao downInfoDao = daoSession.getDownloadItemInfoDao();
        downInfoDao.update(info);
    }

    public void deleteDowninfo(DownloadItemInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownloadItemInfoDao downInfoDao = daoSession.getDownloadItemInfoDao();
        downInfoDao.delete(info);
    }


    public DownloadItemInfo queryDownBy(long Id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownloadItemInfoDao downInfoDao = daoSession.getDownloadItemInfoDao();
        QueryBuilder<DownloadItemInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownloadItemInfoDao.Properties.Id.eq(Id));
        List<DownloadItemInfo> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<DownloadItemInfo> queryDownAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownloadItemInfoDao downInfoDao = daoSession.getDownloadItemInfoDao();
        QueryBuilder<DownloadItemInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
