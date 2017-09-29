package cn.ssic.moverlogic.jobinspection;

import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.customviews.spinner.NiceSpinner;
import cn.ssic.moverlogic.mvppresenter.IPresenter;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/29.
 * @E-mail:377855879@qq.com
 */

public class FirstSignActivity extends BaseActivity {
    @BindView(R.id.sp_terms)
    NiceSpinner spTerms;
    @BindView(R.id.sp_charge)
    NiceSpinner spCharge;
    @BindView(R.id.terms)
    LinearLayout terms;
    @BindView(R.id.gesture_view)
    GestureOverlayView gestureView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.right_panel)
    LinearLayout rightPanel;
    private GestureOverlayView gestureOverlayView;
    boolean isSignature =false;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.fragment_job_inspection4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureOverlayView = (GestureOverlayView) findViewById(R.id.gesture_view);
        gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        isSignature = true;
                        break;
                }
                return false;
            }
        });
        //第一次签名
    }

    @OnClick({R.id.next, R.id.right_panel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                break;
            case R.id.right_panel:
                break;
        }
    }
}
