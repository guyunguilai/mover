package cn.ssic.moverlogic.jobinspection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.mvppresenter.IPresenter;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/29.
 * @E-mail:377855879@qq.com
 */

public class InspectionActivity extends BaseActivity {
    @BindView(R.id.base_title_left)
    TextView baseTitleLeft;
    @BindView(R.id.base_title)
    TextView baseTitle;
    @BindView(R.id.base_title_right)
    TextView baseTitleRight;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_inspection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initTitle();
    }

    /**
     * 显示控件
     */
    private void initTitle() {
        setLeftMenuBack();
        baseTitle.setText(getString(R.string.job_inspection));
        baseTitleRight.setVisibility(View.VISIBLE);
        inPlayPanel();
    }
    public void setLeftMenuBack() {
        baseTitleLeft.setVisibility(View.VISIBLE);
        baseTitleLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mis_back_title, 0, 0, 0);
    }
    void inPlayPanel() {
        panelLeft.setImageResource(R.drawable.camera);
        panelMid.setImageResource(R.drawable.next);
        panelRight.setImageResource(R.drawable.add_sub);
    }

    @OnClick({R.id.base_title_left, R.id.base_title_right, R.id.panel_left, R.id.panel_mid, R.id.panel_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_title_left:
                break;
            case R.id.base_title_right:
                break;
            case R.id.panel_left:
                break;
            case R.id.panel_mid:
                Intent intent = new Intent(App.app,EstimateActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.panel_right:
                break;
        }
    }
}
