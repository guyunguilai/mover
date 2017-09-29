package cn.ssic.moverlogic.jobinspection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.customviews.DialogCreater;
import cn.ssic.moverlogic.customviews.wheelView.LoopView;
import cn.ssic.moverlogic.customviews.wheelView.OnItemSelectedListener;
import cn.ssic.moverlogic.mvppresenter.IPresenter;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/29.
 * @E-mail:377855879@qq.com
 */

public class EstimateActivity extends BaseActivity {
    @BindView(R.id.base_title_left)
    TextView baseTitleLeft;
    @BindView(R.id.base_title)
    TextView baseTitle;
    @BindView(R.id.base_title_right)
    TextView baseTitleRight;
    @BindView(R.id.estimate_min_time)
    TextView estimateMinTime;
    @BindView(R.id.estimate_max_time)
    TextView estimateMaxTime;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;
    private ArrayList<String> spinnerList = new ArrayList<>();
    private Dialog mSpinnerDialog =null;
    private String minTimeStr=null;
    int minTimeIndex = 0;
    int maxTimeIndex = 0;
    private String maxTimeStr=null;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_estimate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        baseTitle.setText(R.string.job_prejob);
        baseTitleRight.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.base_title_right, R.id.panel_left, R.id.panel_mid,R.id.estimate_min_time,R.id.estimate_max_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_title_right:
                break;
            case R.id.panel_left:
                finish();
                break;
            case R.id.panel_mid:
                if (maxTimeIndex <= minTimeIndex) {
                    Toast.makeText(EstimateActivity.this, "Wrong time selection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(EstimateActivity.this,CustomerAcceptActivity.class);
                intent.putExtra("minTimeIndex",minTimeIndex);
                intent.putExtra("maxTimeIndex",maxTimeIndex);
                startActivity(intent);
                finish();
                break;
            case R.id.estimate_min_time:
                showMinTimeSpinnerDialog(view.getId());
                break;
            case R.id.estimate_max_time:
                showMaxTimeSpinnerDialog(view.getId());
                break;
        }
    }

    void showMinTimeSpinnerDialog(int id) {
        for (int i = 0; i < 24; i++) {
            spinnerList.add(i, i + 1 + " Hours");
        }
        showSpinnerDialog(id);
    }
    void showMaxTimeSpinnerDialog(int id) {
        for (int i = 0; i < 24; i++) {
            spinnerList.add(i, i + 1 + " Hours");
        }
        showSpinnerDialog(id);
    }

    void showSpinnerDialog(int id) {
        View dialogView = LayoutInflater.from(App.app).inflate(R.layout.dialog_spinner, null);
        LoopView loopView = (LoopView) dialogView.findViewById(R.id.spinner);
        loopView.setId(id);
        loopView.setNotLoop();
        loopView.setItems(spinnerList);
        loopView.setListener(new SpinnerSelectedListener());

        mSpinnerDialog = DialogCreater.createSpinnerDialog(EstimateActivity.this, dialogView);
        mSpinnerDialog.setOnDismissListener(new DismissListener());
        mSpinnerDialog.show();
    }
    class SpinnerSelectedListener implements OnItemSelectedListener{

        @Override
        public void onItemSelected(int index, LoopView arg0) {
            switch (arg0.getId()) {
                case R.id.job_min_time:
                    if (spinnerList.size() == 0) return;//滑动过程中把dialog dismiss掉，spinnerlist就被置空了。
                    minTimeStr = spinnerList.get(index);
                    estimateMinTime.setText(minTimeStr);
                    minTimeIndex = index;
                    break;
                case R.id.job_max_time:
                    if (spinnerList.size() == 0) return;
                    maxTimeStr = spinnerList.get(index);
                    estimateMaxTime.setText(maxTimeStr);
                    maxTimeIndex = index;
                    break;
            }
        }
    }
    class DismissListener implements DialogInterface.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            spinnerList.clear();
        }
    }
}
