package cn.ssic.moverlogic;

import org.junit.Test;

import cn.ssic.moverlogic.net2request.login.LoginHttpService;
import cn.ssic.moverlogic.net2request.login.NTLoginRespBean;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.netokhttp2.LoadingAdapter;


import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
/**
 * Created by Administrator on 2016/9/6.
 */
public class JUnitTest {
    @Test
    public void testLogin(){
            LoginHttpService service = new LoginHttpService("login");
            NTLoginReqBean reqBean = new NTLoginReqBean();
            reqBean.setUsername("zhoufei");
            reqBean.setPwd("2c1f484a44257f53d292810da9776d72");
            service.setParam(reqBean);
            service.callBack(new LoadingAdapter(App.app, NTLoginRespBean.class){
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    assertEquals("dd","100");
                }
            });
            service.synchEnqueue();


    }

//    @Test
//    public void testLoginWithWrongPwd(){
//        try{
//            LoginHttpService service = newmsg LoginHttpService("login");
//            NTLoginReqBean reqBean = newmsg NTLoginReqBean();
//            reqBean.setUsername("zhoufei");
//            reqBean.setPwd("zhoufei");
//            service.setParam(reqBean);
//            service.callBack(newmsg LoadingAdapter(App.app, NTLoginRespBean.class){
//                @Override
//                public void onSuccess(Object o) {
//                    super.onSuccess(o);
////                    assertEquals(respBean.getStatus(),"103");
//                }
//
//                @Override
//                public void onFail(Object o) {
//                    super.onFail(o);
//                    System.out.println(o.toString());
//                    assertTrue(true);
//                }
//            });
//            service.synchEnqueue();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void testLoginWithWrongUser(){
//        try{
//            LoginHttpService service = newmsg LoginHttpService("login");
//            NTLoginReqBean reqBean = newmsg NTLoginReqBean();
//            reqBean.setUsername("4345151515");
//            reqBean.setPwd("zhoufei");
//            service.setParam(reqBean);
//            service.callBack(newmsg LoadingAdapter(App.app, NTLoginRespBean.class){
//                @Override
//                public void onFail(Object o) {
//                    super.onFail(o);
//                    System.out.println(o.toString());
//                    assertTrue(false);
//                }
//            });
//            service.synchEnqueue();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
