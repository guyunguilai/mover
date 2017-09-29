package cn.ssic.moverlogic.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.adapter.JobSheetInfoScrollAdapter;
import cn.ssic.moverlogic.customviews.BannerViewPager;
import cn.ssic.moverlogic.jobinspection.ParamKey;
import cn.ssic.moverlogic.mvppresenter.IPresenter;
import cn.ssic.moverlogic.utils.SharedprefUtil;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/29.
 * @E-mail:377855879@qq.com
 */

public class JobSheet extends BaseActivity {
    @BindView(R.id.base_title_left)
    TextView baseTitleLeft;
    @BindView(R.id.base_title)
    TextView baseTitle;
    @BindView(R.id.base_title_right)
    TextView baseTitleRight;
    @BindView(R.id.point)
    LinearLayout mLlPoint;
    @BindView(R.id.job_sheet_viewpager)
    BannerViewPager mViewpager;
    List<View> views = new ArrayList<>();
    JobSheetInfoScrollAdapter jobSheetInfoScrollAdapter;
    private int jobId=0;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_job_sheet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        baseTitle.setText(R.string.alerts_detail);
        baseTitleRight.setVisibility(View.VISIBLE);
        jobId = SharedprefUtil.getInstance(JobSheet.this).getInt(ParamKey.SP_JOB_ID,0);
        addPoints(13);      //对应的点数量
    }

    @OnClick({R.id.base_title_right, R.id.point})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.base_title_right:
                finish();
                break;
            case R.id.point:
                break;
        }
    }



    private void addPoints(int num) {
        views.clear();
        mLlPoint.removeAllViews();
        for (int i = 0; i < num; i++) {
            int j = i;
            View point = View.inflate(JobSheet.this, R.layout.item_point, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.leftMargin = 4;
            params.rightMargin = 4;
            views.add(point);
            mLlPoint.addView(point, params);
            point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewpager.setCurrentItem(j);
                }
            });
        }
        if (views.size() != 0) {
            views.get(0).setBackgroundResource(R.drawable.round_red_point);
        }

        jobSheetInfoScrollAdapter = new JobSheetInfoScrollAdapter(JobSheet.this, jobId);
        mViewpager.setAdapter(jobSheetInfoScrollAdapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < views.size(); i++) {
                    if (position == i) {
                        views.get(i).setBackgroundResource(R.drawable.round_red_point);
                    } else {
                        views.get(i).setBackgroundResource(R.drawable.round_black_point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
