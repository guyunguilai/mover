package cn.ssic.moverlogic.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CustomDialog extends Dialog {
    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomDialog(Context context,int resId,int styleId,InitViewInterface viewInterface){
        super(context,styleId);
        View view = LayoutInflater.from(context).inflate(resId,null);
        setContentView(view);
        if (null != viewInterface){
            viewInterface.init(view);
        }
    }

    public CustomDialog(Context context,View view,int styleId,InitViewInterface viewInterface){
        super(context,styleId);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        if (null != viewInterface){
            viewInterface.init(view);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface InitViewInterface{
        public void init(View view);
    }
}
