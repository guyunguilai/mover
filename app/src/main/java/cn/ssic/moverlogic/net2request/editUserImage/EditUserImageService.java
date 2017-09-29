package cn.ssic.moverlogic.net2request.editUserImage;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.confirmJob.NTConfirmJobReqBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.HttpService;
import cn.ssic.moverlogic.netokhttp2.OkHttpUtils;
import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class EditUserImageService extends HttpService {
    NTEditUserImageReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        String url  =  HttpConstants.editUserImage+"?"+"uid="+reqBean.getUid();
        call = OkHttpUtils.uploading(mTag, url,reqBean.getPhotoFile(),mAdapter);
    }
    public void synchEnqueue(){
        String url  =  HttpConstants.editUserImage+"?"+"uid="+reqBean.getUid();
        call = OkHttpUtils.synchUploading(mTag, url,reqBean.getPhotoFile(),mAdapter);
    }

    public EditUserImageService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTEditUserImageReqBean) bean;
    }
}
