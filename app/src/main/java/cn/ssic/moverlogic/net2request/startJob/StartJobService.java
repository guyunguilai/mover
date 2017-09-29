package cn.ssic.moverlogic.net2request.startJob;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.logout.NTLogoutReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/5.
 */
public class StartJobService extends HttpService {
    public StartJobService(Object o) {
        super(o);
    }

    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .add("jobStarted",reqBean.getJobStarted()+"")
                .add("startTime",reqBean.getStartTime()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.startJob,body,mAdapter);
    }

    public void synchEnqueue() {
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .add("jobStarted",reqBean.getJobStarted()+"")
                .add("startTime",reqBean.getStartTime()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag,HttpConstants.startJob,body,mAdapter);
    }
    NTStartJobReqBean reqBean;
    public void setParam(NTReqBean bean){
        reqBean = (NTStartJobReqBean) bean;
    }
}
