package cn.ssic.moverlogic.net2request.getAlertJob;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.editUserImage.NTEditUserImageReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetAlertJobService extends HttpService {
    NTGetAlertJobReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.getAlertJob,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("jobId",reqBean.getJobId()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.getAlertJob,body,mAdapter);
    }

    public GetAlertJobService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetAlertJobReqBean) bean;
    }
}
