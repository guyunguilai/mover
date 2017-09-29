package cn.ssic.moverlogic.netokhttp2;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface LoadingListener {
    public void onStart();//网络任务开始，准备工作可能会有诸多共同操作，所以不带参数

    public void onLoading(Object o);//网络任务加载中，参数用来标明任务

    public void onSuccess(Object o);//网络任务成功,统一预处理。然后根据不同的status进行分发到fail，respbean，或者重新登录等等

    public void onRespbean(Object o);//网络任务成功,并且成功解析respbean进行返回

    public void onCancle(Object o);//网络任务取消

    public void onFail(Object o);//网络任务失败，参数用来标明任务

    public void onTimeOut();//网络任务超时

    public void onFailNoPermission(Object o);

    public void onFailIsNull();
}
