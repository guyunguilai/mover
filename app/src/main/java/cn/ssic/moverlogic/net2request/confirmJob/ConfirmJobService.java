package cn.ssic.moverlogic.net2request.confirmJob;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.edituserinfo.NTEditUserInfoReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ConfirmJobService extends HttpService {
    NTConfirmJobReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("appJobId",reqBean.getAppJobId()+"")
                .add("status",reqBean.getStatus()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.confirmJob,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("appJobId",reqBean.getAppJobId()+"")
                .add("status",reqBean.getStatus()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.confirmJob,body,mAdapter);
    }

    public ConfirmJobService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTConfirmJobReqBean) bean;
    }
}
