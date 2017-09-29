package cn.ssic.moverlogic.utils;

import android.text.TextUtils;

/**
 * @Author: he.zhao
 * @Date:on 2017/1/14.
 * @E-mail:377855879@qq.com
 */

public class SplitTimeUtils {

    //截取后台返回的日期
    public static String splitDate(String s){
        String startDate=null;
        if(!TextUtils.isEmpty(s) && s.contains(" ")){
            String date = s.substring(0,s.indexOf(" "));
            String[] listDate = date.split("-");
            startDate=listDate[2]+"/"+listDate[1]+"/"+listDate[0];
        }
        return startDate;
    }

    public static String splitTime(String s){
        String time=null;
        if(!TextUtils.isEmpty(s) && s.contains(" ")){
            String date = s.substring(s.indexOf(" ")+1);
            String[] listTime = date.split(":");
            if(Integer.parseInt(listTime[0])>12){
                time = Integer.parseInt(listTime[0])-12+":"+listTime[1]+" pm";
            }else if(Integer.parseInt(listTime[0])==12){
                time = 12+":"+listTime[1]+" pm";
            }
            else{
                time = Integer.parseInt(listTime[0])+":"+listTime[1]+" am";
            }

        }
        return time;
    }
   //截取时分秒  格式为00:00:00
    public static String splitTotal(String s){
        String time=null;
        String h= null;
        String m= null;
        String sec= null;
        if(!TextUtils.isEmpty(s) && s.contains(":")){
            String[] listTime = s.split(":");
          /*  if("0".equals(listTime[0]) && "0".equals(listTime[1]) && "0".equals(listTime[2])){
                time="";
            }
            if("0".equals(listTime[0]) && "0".equals(listTime[1]) && "0".equals(listTime[2])){
                time="";
            }*/
            if("0".equals(listTime[0])){
                h="";
            }else{
              h= listTime[0]+"Hr(s)" ;
            }
            if("0".equals(listTime[1])){
                m="";
            }else{
                m= listTime[1] +"Min(s)";
            }
            if("0".equals(listTime[2])){
                sec="";
            }else{
                sec= listTime[2]+"Sec(s)" ;
            }
            time=h+m+sec;
        }
        return time;
    }
}
