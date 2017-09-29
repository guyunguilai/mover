package cn.ssic.moverlogic.net2request.logout;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/5.
 */
public class LogoutService extends HttpService {
    public LogoutService(Object o) {
        super(o);
    }

    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder().add("uid",reqBean.getUid()+"").build();
        call = OkHttpUtils.post(mTag, HttpConstants.logout,body,mAdapter);
    }

    public void synchEnqueue() {
        FormBody body = new FormBody.Builder().add("uid",reqBean.getUid()+"").build();
        call = OkHttpUtils.synchPost(mTag,HttpConstants.logout,body,mAdapter);
    }
    NTLogoutReqBean reqBean;
    public void setParam(NTReqBean bean){
        reqBean = (NTLogoutReqBean) bean;
    }
}
