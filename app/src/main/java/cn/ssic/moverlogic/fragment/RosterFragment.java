package cn.ssic.moverlogic.fragment;

import android.app.Activity;
import android.content.Intent;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.activities.DispatchActivity;
import cn.ssic.moverlogic.adapter.RosterInfoScrollAdapter;
import cn.ssic.moverlogic.adapter.RosterRecyclerAdapter;
import cn.ssic.moverlogic.bean.ActiveJobRespBean;
import cn.ssic.moverlogic.customviews.GravitySnapHelper;
import cn.ssic.moverlogic.jobinspection.ParamKey;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.DialogUtil;
import cn.ssic.moverlogic.utils.FileUtils;
import cn.ssic.moverlogic.utils.SharedprefUtil;
import rx.Subscription;

import static cn.ssic.moverlogic.R.id.point;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RosterFragment extends Fragment implements RosterRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    RosterRecyclerAdapter rosterRecyclerAdapter;
    RosterInfoScrollAdapter rosterInfoScrollAdapter;

    MainActivity mActivity;
    List<Job> mJobs = new ArrayList<Job>();
    Job selectedJob;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;

    XRefreshView refreshView;
    private TextView mNoDataTv;
    List<View> views = new ArrayList<>();
    private LinearLayout mMLlPoint;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRosterList();
    }

    public void getRosterList() {
        mActivity.getDialog().showLoading();
        Subscription subscription = RetrofitHttp.getInstance().getRosters(App.app.getLoginUser().getUid() + "")
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getRosterRespBean -> {
                    mActivity.getDialog().dismiss();
                    mJobs.clear();
                    Logger.e("getRosterList" + getRosterRespBean.toString());
                    FileUtils.saveStringToFile("net_request_logs", "getRosterList" + new Date().toString(), mActivity);
                    if (getRosterRespBean.getToday() != null && getRosterRespBean.getToday().size() != 0) {
                        for (Job job : getRosterRespBean.getToday()) {
                            job.setDay(RosterRecyclerAdapter.TODAY);
                        }
                    }
                    if (getRosterRespBean.getTomorrow() != null && getRosterRespBean.getTomorrow().size() != 0) {
                        for (Job job : getRosterRespBean.getTomorrow()) {
                            job.setDay(RosterRecyclerAdapter.TOMMOROW);
                        }
                    }
                    if (getRosterRespBean.getUpcoming() != null && getRosterRespBean.getUpcoming().size() != 0) {
                        for (Job job : getRosterRespBean.getUpcoming()) {
                            job.setDay(RosterRecyclerAdapter.UPCOMING);
                        }
                    }
                    mJobs.addAll(getRosterRespBean.getToday());
                    mJobs.addAll(getRosterRespBean.getTomorrow());
                    mJobs.addAll(getRosterRespBean.getUpcoming());
                    if (mJobs.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        mNoDataTv.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        mNoDataTv.setVisibility(View.GONE);
                    }
                    mActivity.showBadgeView(MainActivity.FRAGMENT_ALERTS, getRosterRespBean.getUnreadAlertJob());
                    mActivity.showBadgeView(MainActivity.FRAGMENT_JOB, getRosterRespBean.getUnreadActiveJob());
                    rosterRecyclerAdapter.refreshList(mJobs);
                    rosterRecyclerAdapter.notifyDataSetChanged();
                }, throwable -> {
//                    mActivity.getDialog().changeDialog("Load failed", "Load the data failure", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
                    Logger.e("getRosterList" + throwable.toString());
                });
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_roster, container, false);
        refreshView = (XRefreshView) view.findViewById(R.id.roster_refreshview);
        mNoDataTv = (TextView) view.findViewById(R.id.no_data_tv);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getRosterList();
                refreshView.stopRefresh();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.roster_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        rosterRecyclerAdapter = new RosterRecyclerAdapter(mActivity, mJobs, this);
        recyclerView.setAdapter(rosterRecyclerAdapter);

        return view;
    }

    View getRosterListView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_roster, null);
    }

    View getRosterInfoView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_roster_recyleview, null);
    }

    @Override
    public void onItemClick(View view, int position, Job job) {
        selectedJob = job;
        loadRosterInfoView();
    }

    void loadMainView() {
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();

        ll.addView(getRosterListView());
        refreshView = (XRefreshView) view.findViewById(R.id.roster_refreshview);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getRosterList();
                refreshView.stopRefresh();
            }
        });
        recyclerView = (RecyclerView) ll.findViewById(R.id.roster_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        if (null == rosterRecyclerAdapter) {
            rosterRecyclerAdapter = new RosterRecyclerAdapter(mActivity, mJobs, this);
        }
        recyclerView.setAdapter(rosterRecyclerAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);

        panelLeft.setVisibility(View.GONE);
        panelRight.setVisibility(View.GONE);
        panelMid.setVisibility(View.GONE);
        mActivity.showLefuMenuSlide();
    }

    ViewPager mViewPager;

    void loadRosterInfoView() {
        mActivity.hidePanel();
        mActivity.hideLeftMenu();
        mActivity.setTv_title(R.string.alerts_detail);
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();
        ll.addView(getRosterInfoView());
        ButterKnife.bind(this, ll);
        mViewPager = (ViewPager) ll.findViewById(R.id.roster_viewpager);
        mMLlPoint = (LinearLayout) ll.findViewById(point);
        addPoints(10);          //对应的点数量
        panelLeft.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelRight.setVisibility(View.INVISIBLE);
        panelMid.setImageResource(R.drawable.start);
    }

    private void addPoints(int num) {
        views.clear();
        mMLlPoint.removeAllViews();
        for (int i = 0; i < num; i++) {
            int j = i;
            View point = View.inflate(getActivity(), R.layout.item_point, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.leftMargin = 4;
            params.rightMargin = 4;
            views.add(point);
            mMLlPoint.addView(point, params);
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
        rosterInfoScrollAdapter = new RosterInfoScrollAdapter(mActivity, selectedJob, this);
        mViewPager.setAdapter(rosterInfoScrollAdapter);
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

    @OnClick({R.id.panel_left, R.id.panel_mid, R.id.panel_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.panel_mid:
                activeJob(selectedJob.getJobId());
                break;
            case R.id.panel_left:
                mActivity.setTv_title(R.string.position4_fragment);
                mActivity.showPanel();
                loadMainView();
                break;
        }
    }

    public void activeJob(int jobId) {
        Subscription subscription = RetrofitHttp.getInstance()
                .activeJob(jobId, App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(acceptJob -> {
                    Logger.e(acceptJob.getMsg());
                    if ("222".equals(acceptJob.getStatus())) {
                        Toast.makeText(mActivity, acceptJob.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    SharedprefUtil.getInstance(mActivity).saveInt(ParamKey.SP_JOB_ID,jobId);
                    Intent intent = new Intent(mActivity, DispatchActivity.class);
                    intent.putExtra("jobId",acceptJob.getJobId());
                    intent.putExtra("start",1);
                    startActivity(intent);
                }, throwable -> {
                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.e(throwable.getMessage());
                });
    }
}
