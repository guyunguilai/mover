package cn.ssic.moverlogic;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.ssic.moverlogic.net2request.Staff;
import cn.ssic.moverlogic.utils.SharedprefUtil;
import cn.ssic.moverlogic.utils.UUIDGenerator;

/**
 * Created by Administrator on 2016/8/31.
 */
public class App extends MultiDexApplication {
    public static App app;
    public static String uuid;
    private Staff mUser;
    public LocationClient mBaiduLocationClient = null;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initBaiduLocation();
        if (TextUtils.isEmpty(SharedprefUtil.getInstance(this).get("registrationId",""))){
            uuid = UUIDGenerator.getUUID();
            SharedprefUtil.getInstance(this).save("registrationId",uuid);
        }else {
            uuid =  SharedprefUtil.getInstance(this).get("registrationId","");
        }

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.initCrashHandler(getApplicationContext());
    }
    public Staff getLoginUser(){
        return mUser;
    }

    public void setLoginUser(Staff user){
        mUser = user;
    }

    private void initBaiduLocation() {
        if (mBaiduLocationClient == null) {
            mBaiduLocationClient = new LocationClient(this);     //声明LocationClient类
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        mBaiduLocationClient.setLocOption(option);

    }

}
