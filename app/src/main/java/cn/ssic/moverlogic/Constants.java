package cn.ssic.moverlogic;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Constants {
    public static final boolean isTest = true;


    //net load constants;
    public static final int NET_LOAD_START = 10;
    public static final int NET_LOAD_LOADING = 11;
    public static final int NET_LOAD_SUCCESS = 12;
    public static final int NET_LOAD_FAIL = 13;
    public static final int NET_LOAD_TIMEOUT = 14;
    public static final int NET_LOAD_CANCLE = 15;

    public static final int HTTP_CONNECT_TIMEOUT = 16000;

    //BroadCast Action
    public static final String ACTION_KICKOFF = "action_kickoff";
}
