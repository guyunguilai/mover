package cn.ssic.moverlogic.net2request.getAlerts;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetAlertsService extends HttpService {
    NTGetAlertsReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.getAlerts,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.getAlerts,body,mAdapter);
    }

    public GetAlertsService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetAlertsReqBean) bean;
    }
}
