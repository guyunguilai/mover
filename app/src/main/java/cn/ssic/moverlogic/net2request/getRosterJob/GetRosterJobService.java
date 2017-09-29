package cn.ssic.moverlogic.net2request.getRosterJob;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetRosterJobService extends HttpService {
    NTGetRosterJobReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.getRosterJob,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.getRosterJob,body,mAdapter);
    }

    public GetRosterJobService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetRosterJobReqBean) bean;
    }
}
