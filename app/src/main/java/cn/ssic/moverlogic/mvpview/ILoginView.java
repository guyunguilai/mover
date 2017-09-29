package cn.ssic.moverlogic.mvpview;

import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/8/25.
 */
public interface ILoginView {
    void onClearName();
    void onClearPwd();
    void onLogin(String success);
    void onLoading(int visiablity);
    void onRemmeberUsername();
    void onTimeOut();
    void onFail(NTRespBean bean);
    void onFailNoPermission(NTRespBean bean);
    void onFailIsNull();
}
