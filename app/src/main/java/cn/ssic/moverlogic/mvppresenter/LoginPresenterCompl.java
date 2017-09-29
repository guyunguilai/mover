package cn.ssic.moverlogic.mvppresenter;


import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.mvpview.ILoginView;
import cn.ssic.moverlogic.mvpview.LoginActivity;
import cn.ssic.moverlogic.net2request.NTRespBean;
import cn.ssic.moverlogic.net2request.Staff;
import cn.ssic.moverlogic.net2request.login.LoginHttpService;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.net2request.login.NTLoginRespBean;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.netokhttp2.LoadingAdapter;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.utils.SHA;
import cn.ssic.moverlogic.utils.SharedprefUtil;

/**
 * Created by Administrator on 2016/8/25.
 */
public class LoginPresenterCompl implements ILoginPresenter {
    ILoginView mView;
    LoginActivity mActivity;

    public LoginPresenterCompl(ILoginView v, LoginActivity c) {
        mView = v;
        mActivity = c;
    }

    @Override
    public void clearName() {
        mView.onClearName();
    }

    @Override
    public void clearPwd() {

    }

    @Override
    public void rememberUsername() {
        mView.onRemmeberUsername();
    }

    @Override
    public void login(String name, String pwd) {
//        Subscription subscription = RetrofitHttp.getInstance().login("test", "123456")
//        .compose(RxResultHelper.handleResult())
//        .compose(RxResultHelper.applyIoSchedulers())
//        .subscribe(loginRespBean -> {
//                Logger.e(loginRespBean.toString());
//                App.app.setLoginUser(loginRespBean.getStaff());
//                System.out.println("bean.getStatus():"+loginRespBean.getStatus());
//                EventBus.getDefault().postSticky(loginRespBean);//post给MainActivity的onEventLogin方法
//                mView.onLogin("");
//        }, error -> {
//            Logger.e(error.toString());
//        });
//
//        mActivity.add_sub(subscription);

        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername(name);
        reqBean.setPwd(SHA.encodeByMD5(pwd));
//        reqBean.setPwd(pwd);
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(mActivity, NTLoginRespBean.class) {
            @Override
            public void onStart() {
                super.onStart();
                System.out.println("onStart");
            }

            @Override
            public void onLoading(Object o) {
                super.onLoading(o);
                System.out.println("onLoading");
            }

            @Override
            public void onCancle(Object o) {
                super.onCancle(o);
                System.out.println("onCancle");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
//                Toast.makeText(mActivity, "Login fail", Toast.LENGTH_SHORT).show();
//                System.out.println("onFail:" + o);
                if (o instanceof NTRespBean){
                    NTRespBean bean = (NTRespBean) o;
                    mView.onFail(bean);
                }else {
                    mView.onFail(null);
                }
            }

            @Override
            public void onTimeOut() {
                super.onTimeOut();
                System.out.println("onTimeOut");
                mView.onTimeOut();
            }

            @Override
            public void onFailNoPermission(Object o) {
                super.onFailNoPermission(o);
                NTRespBean ntRespBean = (NTRespBean) o;
                mView.onFailNoPermission(ntRespBean);
            }

            @Override
            public void onFailIsNull() {
                super.onFailIsNull();
                mView.onFailIsNull();
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mView.onFail(null);
            }

            @Override
            public void onRespbean(Object o) {
//                Toast.makeText(mActivity, "Login success", Toast.LENGTH_SHORT).show();
                NTLoginRespBean bean = (NTLoginRespBean) o;
                App.app.setLoginUser(bean.getData().getStaff());

                System.out.println("bean.getStatus():" + bean.getStatus());
                Staff staff = bean.getData().getStaff();
                EventBus.getDefault().postSticky(staff);//post给MainActivity的onEventLogin方法
                SharedprefUtil.getInstance(mActivity).save("uid", staff.getUid() + "");
                SharedprefUtil.getInstance(mActivity).save("token", staff.getToken());
                SharedprefUtil.getInstance(mActivity).save("companyName", staff.getCompanyName() + "");
                SharedprefUtil.getInstance(mActivity).save("email", staff.getEmail() + "");
                SharedprefUtil.getInstance(mActivity).save("firstName", staff.getFirstname() + "");
                SharedprefUtil.getInstance(mActivity).save("lastName", staff.getLastname() + "");
                SharedprefUtil.getInstance(mActivity).save("phone", staff.getPhone() + "");
                SharedprefUtil.getInstance(mActivity).save("photoUrl", staff.getPhotoUrl() + "");
                SharedprefUtil.getInstance(mActivity).save("staffId", staff.getStaffId() + "");
                SharedprefUtil.getInstance(mActivity).save("surname", staff.getSurname() + "");
                SharedprefUtil.getInstance(mActivity).save("companyId", staff.getCompanyId() + "");
                SharedprefUtil.getInstance(mActivity).save("isAlert", staff.getIsAlert() + "");
                SharedprefUtil.getInstance(mActivity).save("username", staff.getUsername() + "");
                SharedprefUtil.getInstance(mActivity).saveInt("pwdSize", pwd.length());
//                SharedprefUtil.getInstance(mActivity).save("uuid", UUIDGenerator.getUUID());
                mView.onLogin("");
                RetrofitHttp.getRetrofitHttpInstance().setRetrofitUrl(HttpConstants.TEST_URL);
            }
        });
        service.enqueue();
    }

    @Override
    public void register() {
//        GetUserInfoService infoService = newmsg GetUserInfoService("getuserinfo");
//        NTGetUserInfoReqBean reqBean1 = newmsg NTGetUserInfoReqBean();
//        reqBean1.setUid(85);
//        infoService.setParam(reqBean1);
//        infoService.callBack(newmsg LoadingAdapter(mContext, NTGetUserInfoRespBean.class){
//            @Override
//            public void onRespbean(Object o) {
//                super.onRespbean(o);
//                NTGetUserInfoRespBean respBean = (NTGetUserInfoRespBean)o;
//                System.out.println("respBean.getStatus():"+respBean.getStatus());
//            }
//
//            @Override
//            public void onFail(Object o) {
//                super.onFail(o);
//                System.out.println("respBean.getStatus():"+o.toString());
//            }
//        });
//        infoService.enqueue();
    }

    @Override
    public void loading(int visiblity) {

    }
}
