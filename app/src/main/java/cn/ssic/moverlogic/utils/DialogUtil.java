package cn.ssic.moverlogic.utils;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Lawliet on 2015/10/22.
 */
public class DialogUtil {
    private Context context;
    private SweetAlertDialog pDialog;

    /**
     * 正规型
     */
    public static final int NORMAL_TYPE = 0;

    /**
     * 错误
     */
    public static final int ERROR_TYPE = 1;

    /**
     * 成功
     */
    public static final int SUCCESS_TYPE = 2;

    /**
     * 警告
     */
    public static final int WARNING_TYPE = 3;

    /**
     * 自定义图像类型
     */
    public static final int CUSTOM_IMAGE_TYPE = 4;

    /**
     * 进展型
     */
    public static final int PROGRESS_TYPE = 5;

    private SweetAlertDialog.OnSweetClickListener onCancelClickListeners;
    private SweetAlertDialog.OnSweetClickListener onConfirmClickListeners;

    public DialogUtil(Context context) {
        this.context = context;
    }


    /**
     * 加载Loading...
     */
    public void showLoading() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * 卸载Dialog...
     */
    public void dismiss() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    /**
     * 状态
     *
     * @param type                   状态
     * @param titleText              标题文本
     * @param contentText            内容文本
     * @param cancelText             取消按钮文本
     * @param confirmText            确认按钮文本
     * @param showCancelButton       是否有取消按钮
     * @param onCancelClickListener  取消按钮点击事件
     * @param onConfirmClickListener 确认按钮点击事件
     */
    public void showDialog(int type, String titleText, String contentText, String cancelText, String confirmText
            , boolean showCancelButton
            , SweetAlertDialog.OnSweetClickListener onCancelClickListener
            , SweetAlertDialog.OnSweetClickListener onConfirmClickListener) {

        if (onCancelClickListener == null) {
            onCancelClickListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismiss();
                }
            };
        }

        if (onConfirmClickListener == null) {
            onConfirmClickListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismiss();
                }
            };
        }
        pDialog = new SweetAlertDialog(context, type);
        pDialog.setTitleText(titleText)
                .setContentText(contentText)
                .setCancelText(cancelText)
                .setConfirmText(confirmText)
                .showCancelButton(showCancelButton)
                .setCancelClickListener(onCancelClickListener)
                .setConfirmClickListener(onConfirmClickListener).show();
    }

    /**
     * 动画转换Dialog状态
     *
     * @param titleText              转换后标题文本
     * @param contentText            转换后内容文本
     * @param cancelText             转换后取消按钮文本
     * @param confirmText            转换后确定按钮文本
     * @param showCancelButton       转换后是否显示取消按钮
     * @param onCancelClickListener  转换后取消按钮点击事件
     * @param onConfirmClickListener 转换后确定按钮点击事件
     * @param type                   转换后状态（确定，警告，错误等）
     */
    public void changeDialog(String titleText, String contentText, String cancelText, String confirmText
            , boolean showCancelButton
            , SweetAlertDialog.OnSweetClickListener onCancelClickListener
            , SweetAlertDialog.OnSweetClickListener onConfirmClickListener, int type) {

        if (onCancelClickListener == null) {
            onCancelClickListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismiss();
                }
            };
        }

        if (onConfirmClickListener == null) {
            onConfirmClickListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismiss();
                }
            };
        }
        if (pDialog != null) {
            pDialog.setTitleText(titleText)
                    .setContentText(contentText)
                    .setCancelText(cancelText)
                    .setConfirmText(confirmText)
                    .showCancelButton(showCancelButton)
                    .setCancelClickListener(onCancelClickListener)
                    .setConfirmClickListener(onConfirmClickListener)
                    .changeAlertType(type);
        } else {
            pDialog = new SweetAlertDialog(context, type);
            pDialog.setTitleText(titleText)
                    .setContentText(contentText)
                    .setCancelText(cancelText)
                    .setConfirmText(confirmText)
                    .showCancelButton(showCancelButton)
                    .setCancelClickListener(onCancelClickListener)
                    .setConfirmClickListener(onConfirmClickListener).show();
        }
    }
}
