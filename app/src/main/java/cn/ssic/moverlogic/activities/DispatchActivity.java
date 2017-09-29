package cn.ssic.moverlogic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.jobinspection.InspectionActivity;
import cn.ssic.moverlogic.mvppresenter.IPresenter;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/28.
 * @E-mail:377855879@qq.com
 */

public class DispatchActivity extends BaseActivity {

    @BindView(R.id.base_title_left)
    TextView baseTitleLeft;
    @BindView(R.id.base_title)
    TextView baseTitle;
    @BindView(R.id.base_title_right)
    TextView baseTitleRight;
    @BindView(R.id.tv_dispatch)
    TextView tvDispatch;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;
    private int jobId;
    private int start;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_dispatch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        baseTitle.setText(getString(R.string.job_dispatch));
        initData();//初始化操作
    }

    private void initData() {
        jobId = getIntent().getIntExtra("jobId",0);
        start = getIntent().getIntExtra("start",0);
        tvDispatch.setText(R.string.job_dispatch_1);
        panelRight.setVisibility(View.GONE);
        baseTitleRight.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.panel_left, R.id.panel_mid, R.id.panel_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.panel_left:
                start--;
                if(start == 3){
                    tvDispatch.setText(R.string.job_dispatch_3);
                }
                else if(start == 2){
                    tvDispatch.setText(R.string.job_dispatch_2);
                }
                else if(start == 1){
                    tvDispatch.setText(R.string.job_dispatch_1);
                }else{
                    finish();
                }
                break;
            case R.id.panel_mid:
                dispatchJob(start);
                break;
            case R.id.panel_right:
                break;
        }
    }


    private void dispatchJob(int item) {
        RetrofitHttp.getInstance()
                .dispatchJob(jobId, App.app.getLoginUser().getUid(), item)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(respBean -> {
//                    Toast.makeText(mActivity, "dispatchJob success", Toast.LENGTH_SHORT).show();
                    if (respBean.getSkipMark() == 555) {
                        start++;
                        switch (start){
                            case 2:
                                tvDispatch.setText(R.string.job_dispatch_2);
                                break;
                            case 3:
                                tvDispatch.setText(R.string.job_dispatch_3);
                                break;
                            case 4:
                                Intent intent = new Intent(App.app, InspectionActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }

                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "dispatchJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("dispatchJob:" + throwable.getMessage());
                });
    }

}
