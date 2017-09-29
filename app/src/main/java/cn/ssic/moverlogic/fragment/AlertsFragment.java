package cn.ssic.moverlogic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.adapter.AlertInfoAdapter;
import cn.ssic.moverlogic.adapter.AlertInfoScrollAdapter;
import cn.ssic.moverlogic.adapter.AlertsRecyclerAdapter;
import cn.ssic.moverlogic.customviews.GravitySnapHelper;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.DialogUtil;
import cn.ssic.moverlogic.utils.FileUtils;
import rx.Subscription;

import static cn.ssic.moverlogic.R.id.panel_left;
import static cn.ssic.moverlogic.R.id.point;


/**
 * Created by Administrator on 2016/9/14.
 */
public class AlertsFragment extends Fragment implements AlertsRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    AlertsRecyclerAdapter alertsRecyclerAdapter;
    AlertInfoAdapter alertInfoAdapter;
    AlertInfoScrollAdapter alertInfoScrollAdapter;
    public int alertCardInfoIndex;
    MainActivity mActivity;
    @Nullable
    @BindView(panel_left)
    ImageView panelLeft;
    @Nullable
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @Nullable
    @BindView(R.id.panel_right)
    ImageView panelRight;

    XRefreshView refreshView;
    public int fragmentViewStatus = 0;
    public static final int ALERT_FRAGMENT_MAIN = 0;
    public static final int ALERT_FRAGMENT_INFO = 1;
    public static final int ALERT_FRAGMENT_ACCEPT = 2;
    public static final int ALERT_FRAGMENT_DECLINE = 3;
    List<View> views = new ArrayList<>();
    List<Job> mJobs = new ArrayList<Job>();

    Job selectedJob;
    private TextView mNoDataTv;
    private static final String TAG = "AlertsFragment";
    private LinearLayout mLlPoint;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewStatus = ALERT_FRAGMENT_MAIN;
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alerts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.alert_recyclerview);
        mNoDataTv = (TextView) view.findViewById(R.id.no_data_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        alertsRecyclerAdapter = new AlertsRecyclerAdapter(mActivity, mJobs, this);
        recyclerView.setAdapter(alertsRecyclerAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);

        refreshView = (XRefreshView) view.findViewById(R.id.alert_refreshview);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getAlertsList();
                refreshView.stopRefresh();
            }
        });

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAlertsList();
    }

    void getAlertsList() {
        mActivity.getDialog().showLoading();
        Subscription subscription = RetrofitHttp.getInstance()
                .getAlerts(App.app.getLoginUser().getUid() + "")
//                .getAlerts("搬家")
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getAlertsRespBean -> {
                    mActivity.getDialog().dismiss();
                    mJobs.clear();
                    FileUtils.saveStringToFile("net_request_logs", "getAlertList success" + new Date().toString(), mActivity);
                    if (getAlertsRespBean.getAvailableNow() != null && getAlertsRespBean.getAvailableNow().size() != 0) {
                        for (Job job : getAlertsRespBean.getAvailableNow()) {
                            job.setStatus(AlertsRecyclerAdapter.JOB_TYPE_AVIALBLE);
                        }
                    }
                    if (getAlertsRespBean.getNewJob() != null && getAlertsRespBean.getNewJob().size() != 0) {
                        for (Job job : getAlertsRespBean.getNewJob()) {
                            job.setStatus(AlertsRecyclerAdapter.JOB_TYPE_NEW);
                        }
                    }
                    if (getAlertsRespBean.getChangeJob() != null && getAlertsRespBean.getChangeJob().size() != 0) {
                        for (Job job : getAlertsRespBean.getChangeJob()) {
                            job.setStatus(AlertsRecyclerAdapter.JOB_TYPE_CHANGES);
                        }
                    }
                    if (getAlertsRespBean.getCancelJob() != null && getAlertsRespBean.getCancelJob().size() != 0) {
                        for (Job job : getAlertsRespBean.getCancelJob()) {
                            job.setStatus(AlertsRecyclerAdapter.JOB_TYPE_CANCEL);
                        }
                    }
                    mJobs.addAll(getAlertsRespBean.getAvailableNow());
                    mJobs.addAll(getAlertsRespBean.getNewJob());
                    mJobs.addAll(getAlertsRespBean.getChangeJob());
                    mJobs.addAll(getAlertsRespBean.getCancelJob());
                    if (mJobs.size() == 0) {
                        mNoDataTv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        mNoDataTv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    mActivity.showBadgeView(MainActivity.FRAGMENT_ALERTS, getAlertsRespBean.getUnreadNumber());
                    mActivity.showBadgeView(MainActivity.FRAGMENT_JOB, getAlertsRespBean.getActiveJobUnreadNuber());
                    alertsRecyclerAdapter.refreshList(mJobs);
                    alertsRecyclerAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mActivity.getDialog().changeDialog("Load failed", "Load the data failure", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
                });
    }

    @Override
    public void onItemClick(View view, int position, Job job) {
        selectedJob = job;
        mActivity.setTv_title(R.string.alerts_detail);
        loadInfoView();
    }

    View getAlertsListView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts, null);
    }

    View getAlertInfoView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview, null);
    }

    View getAcceptView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_alert_accept, null);
    }

    View getDeclineView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_alert_decline, null);
    }

    void loadMainView() {
        getAlertsList();
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();
        ll.addView(getAlertsListView());
        ButterKnife.bind(this, ll);
        refreshView = (XRefreshView) view.findViewById(R.id.alert_refreshview);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getAlertsList();
                refreshView.stopRefresh();
            }
        });
        recyclerView = (RecyclerView) ll.findViewById(R.id.alert_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        alertsRecyclerAdapter = new AlertsRecyclerAdapter(mActivity, mJobs, this);
        recyclerView.setAdapter(alertsRecyclerAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);
        fragmentViewStatus = ALERT_FRAGMENT_MAIN;
        mActivity.showLefuMenuSlide();
    }

    //    RecyclerViewPager mRecyclerView;
    ViewPager mViewPager;
    ;

    void loadInfoView() {
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();
        ll.addView(getAlertInfoView());
        mActivity.hidePanel();
        mActivity.hideLeftMenu();
        ButterKnife.bind(this, ll);
        alertInfoScrollAdapter = new AlertInfoScrollAdapter(mActivity, selectedJob, this);
        mViewPager = (ViewPager) ll.findViewById(R.id.alert_viewpager);
        mLlPoint = (LinearLayout) ll.findViewById(point);
        addPoints(4);       //对应的点数量
        fragmentViewStatus = ALERT_FRAGMENT_INFO;
    }

    private void addPoints(int num) {
        views.clear();
        mLlPoint.removeAllViews();
        for (int i = 0; i < num; i++) {
            int j = i;
            View point = View.inflate(getActivity(), R.layout.item_point, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.leftMargin = 4;
            params.rightMargin = 4;
            views.add(point);
            mLlPoint.addView(point, params);
            point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
        if (views.size() != 0) {
            views.get(0).setBackgroundResource(R.drawable.round_red_point);
        }

        mViewPager.setAdapter(alertInfoScrollAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    void loadAcceptView() {
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();
        ll.addView(getAcceptView());
        ButterKnife.bind(this, ll);
        panelMid.setImageResource(R.drawable.confirm);
        panelRight.setVisibility(View.INVISIBLE);
        fragmentViewStatus = ALERT_FRAGMENT_ACCEPT;
    }

    void loadDeclineView() {
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();
        ll.addView(getDeclineView());
        ButterKnife.bind(this, ll);
        panelRight.setVisibility(View.INVISIBLE);
        panelMid.setImageResource(R.drawable.confirm);
        fragmentViewStatus = ALERT_FRAGMENT_DECLINE;
    }

    @Optional
    @OnClick({panel_left, R.id.panel_mid, R.id.panel_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case panel_left:
                switch (fragmentViewStatus) {
                    case ALERT_FRAGMENT_INFO:
                        mActivity.setTv_title(R.string.position3_fragment);
                        loadMainView();
                        mActivity.showPanel();
                        break;
                    case ALERT_FRAGMENT_ACCEPT:
                        loadInfoView();
                        break;
                    case ALERT_FRAGMENT_DECLINE:
                        loadInfoView();
                        break;
                }
                break;
            case R.id.panel_mid:
                switch (fragmentViewStatus) {
                    case ALERT_FRAGMENT_INFO:
                        loadAcceptView();
                        break;
                    case ALERT_FRAGMENT_ACCEPT:
                        acceptOrDeclineJob(true);
                        break;
                    case ALERT_FRAGMENT_DECLINE:
                        acceptOrDeclineJob(false);
                        break;
                }
                break;
            case R.id.panel_right:
                switch (fragmentViewStatus) {
                    case ALERT_FRAGMENT_INFO:
                        loadDeclineView();
                        break;
                    case ALERT_FRAGMENT_ACCEPT:
                        break;
                    case ALERT_FRAGMENT_DECLINE:
                        break;
                }
                break;
        }
    }

    void acceptOrDeclineJob(boolean yes) {
        Subscription subscription = RetrofitHttp.getInstance()
                .confirmJob(App.app.getLoginUser().getUid() + "", selectedJob.getJobId() + "", yes ? 2 : 3)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(confirmJobRespBean -> {
                    Logger.e("confirmJobRespBean" + confirmJobRespBean.toString());
                    if (yes) {
//                        Toast.makeText(mActivity, "accept job success", Toast.LENGTH_SHORT).show();
                        mActivity.showFragment(mActivity.mRosterFragment);
                        fragmentViewStatus = ALERT_FRAGMENT_MAIN;
                    } else {
//                        Toast.makeText(mActivity, "decline job success", Toast.LENGTH_SHORT).show();
                        loadMainView();
                    }
                    mActivity.showPanel();
                }, throwable -> {
                    Logger.e("confirmJob exception");
                    if (yes) {
//                        Toast.makeText(mActivity, "accept job fail", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(mActivity, "decline job fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
