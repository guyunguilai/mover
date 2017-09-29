package cn.ssic.moverlogic.net2request.login;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.Constants;
import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.LoadingHandler;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LoginHttpService extends HttpService {
    public LoginHttpService(Object tag){
        super(tag);
    }
    private NTLoginReqBean loginBean;
    @Override
    public void enqueue() {
        super.enqueue();//调用start状态

        FormBody body = new FormBody.Builder()
                .add("username",loginBean.getUsername())
                .add("password",loginBean.getPwd())
                .add("registrationId", App.app.uuid+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.login,body,mAdapter);
    }

    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("username",loginBean.getUsername())
                .add("password",loginBean.getPwd())
                .add("registrationId", App.app.uuid+"")
                .build();
        call = OkHttpUtils.synchPost(mTag,HttpConstants.login,body,mAdapter);
    }

    public void setParam(NTReqBean bean){
        loginBean = (NTLoginReqBean) bean;
    }
}
