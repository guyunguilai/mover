package cn.ssic.moverlogic.mvppresenter;

/**
 * Created by Administrator on 2016/8/25.
 */
public interface ILoginPresenter extends IPresenter {
    void clearName();
    void clearPwd();
    void login(String name,String pwd);
    void register();
    void loading(int visiblity);
    void rememberUsername();
}
