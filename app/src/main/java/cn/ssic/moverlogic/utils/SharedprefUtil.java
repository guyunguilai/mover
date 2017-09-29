package cn.ssic.moverlogic.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享数据存取类
 * 
 *
 */
public class SharedprefUtil {

	private Context mContext;
	private static SharedprefUtil instance;

	public static SharedprefUtil getInstance(Context c){
		if (instance == null)
		{
			instance = new SharedprefUtil(c.getApplicationContext());
		}
		return instance;
	}

	private SharedprefUtil(Context c){
		mContext = c;
	}

	public boolean contains( String key) {
		SharedPreferences settings = mContext.getSharedPreferences(
				"movelogic", Context.MODE_PRIVATE );
		return settings.contains(key);
	}

	/**
	 * 重设指定字段
	 * 
	 * @param ctx
	 * @param key
	 */
	public void resetByKey( String key) {
		SharedPreferences settings = mContext.getSharedPreferences(
				"movelogic", Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 共享数据中存储数据
	 * 
	 * @param ctx
	 * @param key
	 * @param value
	 */
	public  void save( String key, String value) {
		 SharedPreferences settings =
				 mContext.getSharedPreferences("movelogic",
		 Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}
	/**
	 * 共享数据中存储数据
	 *
	 * @param ctx
	 * @param key
	 * @param value
	 */
	public  void saveInt( String key, int value) {
		SharedPreferences settings =
				mContext.getSharedPreferences("movelogic",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * 共享数据中获取数据
	 *
	 * @param ctx
	 * @param key
	 * @param defVal
	 * @return
	 */
	public int getInt( String key, int defVal) {
		SharedPreferences settings =
				mContext.getSharedPreferences("movelogic",
						Context.MODE_PRIVATE);
		return settings.getInt(key,defVal);
	}


	/**
	 * 共享数据中获取数据
	 * 
	 * @param ctx
	 * @param key
	 * @param defVal
	 * @return
	 */
	public  String get( String key, String defVal) {
		 SharedPreferences settings =
				 mContext.getSharedPreferences("movelogic",
				Context.MODE_PRIVATE);
		return settings.getString(key, defVal);
	}

	/**
	 * 共享数据中存储布尔型数据
	 * 
	 * @param ctx
	 * @param key
	 * @param value
	 */
	public  void saveBoolean( String key, boolean value) {
		 SharedPreferences settings =
		 mContext.getSharedPreferences("movelogic",
		 Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 共享数据中获取布尔型数据
	 * 
	 * @param ctx
	 * @param key
	 * @param defVal
	 * @return
	 */
	public  boolean getBoolean( String key, boolean defVal) {
		 SharedPreferences settings =
				 mContext.getSharedPreferences("movelogic",
		 Context.MODE_PRIVATE);
		return settings.getBoolean(key, defVal);
	}

	/*
	 * public static int getLocalVersion(int ver){ return
	 * pref.getInt(Settings.LOCAL_VERSION, ver); }
	 * 
	 * public static void setLocalVersion(int ver) { SharedPreferences.Editor
	 * editor = pref.edit(); editor.putInt(Settings.LOCAL_VERSION, ver);
	 * editor.commit(); }
	 */
	
}
