package cn.ssic.moverlogic;

import android.support.test.filters.LargeTest;
import android.test.ApplicationTestCase;
//import android.test.suitebuilder.annotation.LargeTest;

import cn.ssic.moverlogic.net2request.activeJob.ActiveJobService;
import cn.ssic.moverlogic.net2request.activeJob.NTActiveJobReqBean;
import cn.ssic.moverlogic.net2request.activeJob.NTActiveJobRespBean;
import cn.ssic.moverlogic.net2request.edituserinfo.EditUserInfoService;
import cn.ssic.moverlogic.net2request.edituserinfo.NTEditUserInfoReqBean;
import cn.ssic.moverlogic.net2request.getAlertJob.NTGetAlertJobReqBean;
import cn.ssic.moverlogic.net2request.getAlertJob.NTGetAlertJobRespBean;
import cn.ssic.moverlogic.net2request.getAlerts.GetAlertsService;
import cn.ssic.moverlogic.net2request.getAlerts.NTGetAlertsReqBean;
import cn.ssic.moverlogic.net2request.getAlerts.NTGetAlertsRespBean;
import cn.ssic.moverlogic.net2request.getRosters.GetRostersService;
import cn.ssic.moverlogic.net2request.getRosters.NTGetRostersReqBean;
import cn.ssic.moverlogic.net2request.getRosters.NTGetRostersRespBean;
import cn.ssic.moverlogic.net2request.getuserinfo.GetUserInfoService;
import cn.ssic.moverlogic.net2request.getuserinfo.NTGetUserInfoReqBean;
import cn.ssic.moverlogic.net2request.getuserinfo.NTGetUserInfoRespBean;
import cn.ssic.moverlogic.net2request.login.LoginHttpService;
import cn.ssic.moverlogic.net2request.login.NTLoginRespBean;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.net2request.logout.LogoutService;
import cn.ssic.moverlogic.net2request.logout.NTLogoutReqBean;
import cn.ssic.moverlogic.net2request.logout.NTLogoutRespBean;
import cn.ssic.moverlogic.netokhttp2.LoadingAdapter;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<App> {
    public ApplicationTest() {
        super(App.class);
//        Looper.prepare();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername("zhoufei");
        reqBean.setPwd("e10adc3949ba59abbe56e057f20f883e");
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTLoginRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                NTLoginRespBean bean = (NTLoginRespBean)o;
                App.app.setLoginUser(bean.getData().getStaff());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                assertTrue(false);
            }
        });
        service.synchEnqueue();
    }

    @LargeTest
    public void testLogin() throws Exception {
//        LoginHttpService service = newmsg LoginHttpService("login");
//        NTLoginReqBean reqBean = newmsg NTLoginReqBean();
//        reqBean.setUsername("zhoufei");
//        reqBean.setPwd("2c1f484a44257f53d292810da9776d72");
//        service.setParam(reqBean);
//        service.callBack(newmsg LoadingAdapter(App.app, NTLoginRespBean.class) {
//            @Override
//            public void onSuccess(Object o) {
//                super.onSuccess(o);
//                NTLoginRespBean bean = (NTLoginRespBean)respBean;
//                App.app.setLoginUser(bean.getData().getStaff());
//                assertEquals(respBean.getStatus(), "000");
//            }
//        });
//        service.synchEnqueue();

    }

    @LargeTest
    public void testLoginWithWrongPwd() throws Exception {
        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername("test");
        reqBean.setPwd("zhoufei");
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTLoginRespBean.class) {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "103");
            }
        });
        service.synchEnqueue();
    }

    @LargeTest
    public void testLoginWithWrongUser() throws Exception {
        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername("4345151515");
        reqBean.setPwd("zhoufei");
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTLoginRespBean.class) {

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println(respBean.toString());
//                assertEquals(respBean.getStatus(), "102");
            }
        });
        service.synchEnqueue();
    }

    @LargeTest
    public void testLogout() throws Exception {
//        LogoutService service = newmsg LogoutService("logout");
//        NTLogoutReqBean reqBean = newmsg NTLogoutReqBean();
//        reqBean.setUid(85);
//        service.setParam(reqBean);
//        service.callBack(newmsg LoadingAdapter(App.app, NTLogoutRespBean.class) {
//            @Override
//            public void onRespbean(Object o) {
//                super.onRespbean(o);
//                System.out.println(respBean.toString());
//                assertEquals(respBean.getStatus(), "000");
//            }
//
//            @Override
//            public void onFail(Object o) {
//                super.onFail(o);
//                System.out.println(respBean.toString());
//                assertTrue(false);
//            }
//        });
//        service.synchEnqueue();
    }

    @LargeTest
    public void testGetUserInfo() throws Exception {
        GetUserInfoService service0 = new GetUserInfoService("logout");
        NTGetUserInfoReqBean reqBean0 = new NTGetUserInfoReqBean();
        reqBean0.setUid(85);
        service0.setParam(reqBean0);
        service0.callBack(new LoadingAdapter(App.app, NTGetUserInfoRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println("testGetUserInfo:"+respBean.toString());
                assertTrue(false);
            }
        });
        service0.synchEnqueue();
    }

    @LargeTest
    public void testEditUserInfo() throws Exception {
        EditUserInfoService service = new EditUserInfoService("edituserinfo");
        short alert = 0;
        NTEditUserInfoReqBean reqBean = new NTEditUserInfoReqBean();
        reqBean.setUid(85);
        reqBean.setUsername("zhoufei");
        reqBean.setEmail("fdjka@msn.com");
        reqBean.setFirstname("test");
        reqBean.setIsAlert(alert);
        reqBean.setPassword("");
        reqBean.setNpassword("");
        reqBean.setPhone("");
        reqBean.setSurname("testsurname");
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTGetUserInfoRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println("testEditUserInfo:"+respBean.toString());
                assertTrue(false);
            }
        });
//        service.synchEnqueue();
    }

    @LargeTest
    public void testGetAlerts() throws Exception {
        GetAlertsService service = new GetAlertsService("getAlerts");
        NTGetAlertsReqBean reqBean = new NTGetAlertsReqBean();
        reqBean.setUid(85);
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTGetAlertsRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println("testGetAlerts:"+respBean.toString());
                assertTrue(false);
            }
        });
        service.synchEnqueue();
    }

    @LargeTest
    public void testActiveJob() throws Exception {
        ActiveJobService service = new ActiveJobService("activeJob");
        NTActiveJobReqBean reqBean = new NTActiveJobReqBean();
        reqBean.setAppJobId(2);
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTActiveJobRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println("activeJob:"+respBean.toString());
                assertTrue(false);
            }
        });
        service.synchEnqueue();
    }

    @LargeTest
    public void testGetRoster() throws Exception {
        GetRostersService service = new GetRostersService("GetRoster");
        NTGetRostersReqBean reqBean = new NTGetRostersReqBean();
        reqBean.setUid(85);
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(App.app, NTGetRostersRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                System.out.println(respBean.toString());
                assertEquals(respBean.getStatus(), "000");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                System.out.println("GetRoster:"+respBean.toString());
                assertTrue(false);
            }
        });
        service.synchEnqueue();
    }
}