package cn.ssic.moverlogic.net2request.checkVersion;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CheckVersionService extends HttpService {

    public CheckVersionService(Object tag){
        super(tag);
    }
    private NTCheckVersionReqBean checkVersionReqBean;
    @Override
    public void enqueue() {
        super.enqueue();//调用start状态

        FormBody body = new FormBody.Builder()
                .add("deviceId",checkVersionReqBean.getDevicesId())
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.checkVersion,body,mAdapter);
    }


    public void setParam(NTReqBean bean){
        checkVersionReqBean = (NTCheckVersionReqBean) bean;
    }
}
