package cn.ssic.moverlogic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;

import cn.ssic.moverlogic.net2request.login.LoginHttpService;
import cn.ssic.moverlogic.net2request.login.NTLoginRespBean;
import cn.ssic.moverlogic.net2request.login.NTLoginReqBean;
import cn.ssic.moverlogic.netokhttp2.LoadingAdapter;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest extends ActivityUnitTestCase<ExampleUnitTest.MyActivity>{
    public static class MyActivity extends Activity {
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            TextView view = new TextView(this);
            view.setText("Hello, activity.");
            view.setId(android.R.id.text1);
            setContentView(view);
        }
    }

    Intent startIntent;
    public ExampleUnitTest(Class<MyActivity> activityClass) {
        super(activityClass);
    }
    public ExampleUnitTest(){
        super(MyActivity.class);
    }
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        startIntent = new Intent(Intent.ACTION_MAIN);
    }

    public void test() throws Exception{
        final int expected = 1;
        final int reality = 1;

//        LoginHttpService service = newmsg LoginHttpService("login");
//        service.callBack(newmsg LoadingAdapter(App.app, NTLoginRespBean.class){
//
//        });

    }
    @Test
    public void testLogin() throws Exception{
        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername("zhoufei");
        reqBean.setPwd("zhoufei");
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(getActivity().getApplicationContext(), NTLoginRespBean.class){
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                System.out.println(respBean.getStatus());
                assertSame(String.class,respBean);
            }
        });
        service.enqueue();
    }
    public void testLogout() throws Exception{
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
    public void testGetUserInfo() throws Exception{
        final int expected = -1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
    public void testEditUserInfo() throws Exception{
        final int expected = -1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
}