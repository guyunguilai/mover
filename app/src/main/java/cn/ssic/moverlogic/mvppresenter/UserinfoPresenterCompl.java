package cn.ssic.moverlogic.mvppresenter;

import com.orhanobut.logger.Logger;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.bean.LogoutRespBean;
import cn.ssic.moverlogic.mvpview.IUserInfoView;
import cn.ssic.moverlogic.net2request.Staff;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.SharedprefUtil;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/13.
 */
public class UserinfoPresenterCompl {
    IUserInfoView mView;
    MainActivity mActivity;
    boolean isAlertMessageOn;
    public UserinfoPresenterCompl(IUserInfoView v, MainActivity c){
        mView = v;
        mActivity = c;
//        isAlertMessageOn = SharedprefUtil.getBoolean(mActivity,"isAlertMessageOn",true);
    }

    public void onEditUserinfo() {
        mView.onEditUserinfo();
    }

    public void onChangeAlert(Staff staff){
        isAlertMessageOn = isAlertMessageOn ? false:true;
        Subscription subscription = RetrofitHttp.getInstance().editUserInfo(App.app.getLoginUser().getUid()+"",
                "",
                "",
                staff.getFirstname(),
                "",
                "",
                "",
                (short) (isAlertMessageOn ? 0 : 1))
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(editUserInfoRespBean -> {
                    Logger.e(editUserInfoRespBean.toString());
                },throwable -> {
                    Logger.e("edituserinfo exception");
                });

        mView.onChangeAlert(isAlertMessageOn);
    }

    public void onLogout(){
        Subscription subscription = RetrofitHttp.getInstance().logout(App.app.getLoginUser().getUid()+"")
                        .compose(RxResultHelper.handleResult())
                        .compose(RxResultHelper.applyIoSchedulers())
                        .subscribe(new Action1<LogoutRespBean>() {
                            @Override
                            public void call(LogoutRespBean logoutRespBean) {
                                Logger.e(logoutRespBean.toString());
                                App.app.setLoginUser(null);
                                SharedprefUtil.getInstance(mActivity).save("uid","");
                                SharedprefUtil.getInstance(mActivity).save("token","");
                                SharedprefUtil.getInstance(mActivity).save("companyName","");
                                SharedprefUtil.getInstance(mActivity).save("email","");
                                SharedprefUtil.getInstance(mActivity).save("firstName","");
                                SharedprefUtil.getInstance(mActivity).save("lastName","");
                                SharedprefUtil.getInstance(mActivity).save("phone","");
                                SharedprefUtil.getInstance(mActivity).save("photoUrl","");
                                SharedprefUtil.getInstance(mActivity).save("staffId","");
                                SharedprefUtil.getInstance(mActivity).save("surname","");
                                SharedprefUtil.getInstance(mActivity).save("companyId","");
                                SharedprefUtil.getInstance(mActivity).save("isAlert","");
                                SharedprefUtil.getInstance(mActivity).saveInt("pwdSize",0);
                                mView.onLogout();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                App.app.setLoginUser(null);
                                SharedprefUtil.getInstance(mActivity).save("uid","");
                                SharedprefUtil.getInstance(mActivity).save("token","");
                                SharedprefUtil.getInstance(mActivity).save("companyName","");
                                SharedprefUtil.getInstance(mActivity).save("email","");
                                SharedprefUtil.getInstance(mActivity).save("firstName","");
                                SharedprefUtil.getInstance(mActivity).save("lastName","");
                                SharedprefUtil.getInstance(mActivity).save("phone","");
                                SharedprefUtil.getInstance(mActivity).save("photoUrl","");
                                SharedprefUtil.getInstance(mActivity).save("staffId","");
                                SharedprefUtil.getInstance(mActivity).save("surname","");
                                SharedprefUtil.getInstance(mActivity).save("companyId","");
                                SharedprefUtil.getInstance(mActivity).save("isAlert","");
                                SharedprefUtil.getInstance(mActivity).saveInt("pwdSize",0);
                                mView.onLogout();
                            }
                        });
    }
}
