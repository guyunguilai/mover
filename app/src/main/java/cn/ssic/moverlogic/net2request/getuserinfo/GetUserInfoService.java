package cn.ssic.moverlogic.net2request.getuserinfo;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetUserInfoService extends HttpService {
    NTGetUserInfoReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = null;
//        if (null != App.app.getLoginUser()) {
            body = new FormBody.Builder()
                    .add("uid",reqBean.getUid()+"")
//                    .add_sub("token",App.app.getLoginUser().getToken())
                    .build();
//        }
        call = OkHttpUtils.post(mTag, HttpConstants.getUserInfo,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = null;
        if (null != App.app.getLoginUser()) {
            body = new FormBody.Builder()
                    .add("uid",reqBean.getUid()+"")
//                    .add_sub("token",App.app.getLoginUser().getToken())
                    .build();
        }

        call = OkHttpUtils.synchPost(mTag, HttpConstants.getUserInfo,body,mAdapter);
    }

    public GetUserInfoService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetUserInfoReqBean) bean;
    }
}
