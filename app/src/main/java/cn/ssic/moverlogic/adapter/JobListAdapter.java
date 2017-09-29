package cn.ssic.moverlogic.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.bean.ActiveJobRespBean;
import cn.ssic.moverlogic.fragment.JobFragment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.DialogUtil;
import cn.ssic.moverlogic.utils.SplitTimeUtils;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/14.
 */
public class JobListAdapter extends RecyclerView.Adapter {
    MainActivity mContext;
    List<Job> mJobs = new ArrayList<>();
    OnItemClickListener onItemClickListener;
    JobFragment mJobFragment;
    ActiveJobRespBean activeJobRespBean;
    public JobListAdapter(Context c, JobFragment fragment) {
        mContext = (MainActivity) c;
        loadJobInfo();
        mJobFragment = fragment;
        onItemClickListener = (OnItemClickListener)fragment;
    }

    private void loadJobInfo(){
        mContext.getDialog().showLoading();
        Subscription subscription = RetrofitHttp.getInstance()
                .getActiveJob(App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getActiveJobRespBean -> {
                    mContext.getDialog().dismiss();
                    activeJobRespBean = getActiveJobRespBean;
                    if (getActiveJobRespBean.getJob() != null && getActiveJobRespBean.getJob().getStartTime() != null){
                        mJobs.add(getActiveJobRespBean.getJob());
                        mJobFragment.mJob = getActiveJobRespBean.getJob();
                        notifyDataSetChanged();
                        String time = getActiveJobRespBean.getPage().getPauseStratTime();

                        if (null != time){
                            String temp = time.substring(0,time.indexOf(":"));
//                            if (temp.indexOf("0") == 0){
//                                temp = temp.substring(1,2);
//                            }
                            int hour = Integer.parseInt(temp);
                            temp = time.substring(time.indexOf(":") + 1,time.lastIndexOf(":"));
//                            if (temp.indexOf("0") == 0){
//                                temp = temp.substring(1,2);
//                            }
                            int minute = Integer.parseInt(temp);
                            temp = time.substring(time.lastIndexOf(":") + 1);
//                            if (temp.indexOf("0") == 0){
//                                temp = temp.substring(1,2);
//                            }
                            int second = Integer.parseInt(temp);
                            mJobFragment.timeCount = hour * 3600 + minute * 60 + second;
                            if (!mJobFragment.handler.hasMessages(JobFragment.START_TIMER)){
                                mJobFragment.handler.sendEmptyMessage(JobFragment.START_TIMER);
                            }
                        }
                        mJobFragment.mActivity.showBadgeView(MainActivity.FRAGMENT_JOB,getActiveJobRespBean.getUnreadNumber());
                    }else{
                        mJobFragment.fragmentStatus = mJobFragment.JOB_NO_ACTIVE;
                        mJobFragment.loadView();
                    }
                },throwable -> {
                    mJobFragment.fragmentStatus = mJobFragment.JOB_NO_ACTIVE;
                    mJobFragment.loadView();
                    mContext.getDialog().changeDialog("Load failed", "Load the data failure", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
                    Logger.e(throwable.getMessage());
                });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_job, parent, false);
        return new JobListViewHolder(view, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mJobs == null || mJobs.size() == 0)return;
        JobListViewHolder jobListViewHolder = (JobListViewHolder)holder;
        jobListViewHolder.jobAddress.setText(TextUtils.isEmpty(mJobs.get(position).getAddress()) ? "" : mJobs.get(position).getAddress());
        jobListViewHolder.jobEstablishtime.setText(TextUtils.isEmpty(mJobs.get(position).getEstime()) ?"" : mJobs.get(position).getEstime()+"Hours");
        jobListViewHolder.jobMovesize.setText(mJobs.get(position).getMovesize()+"CuM");
        jobListViewHolder.jobDate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        String s = mJobs.get(position).getWeek()+"\n"
                + SplitTimeUtils.splitDate(mJobs.get(position).getMoveDate())+"\n"
                +SplitTimeUtils.splitTime(mJobs.get(position).getStartTime());
        SpannableString startTime = new SpannableString(s);
        startTime.setSpan(new AbsoluteSizeSpan(15,true),s.lastIndexOf("\n"),s.lastIndexOf(" "),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        jobListViewHolder.jobTime.setText(startTime);
        if (mJobs.get(position).getReadMark() == 1) {
            jobListViewHolder.check.setVisibility(View.VISIBLE);
        } else {
            jobListViewHolder.check.setVisibility(View.INVISIBLE);
        }
        jobListViewHolder.rosterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position,activeJobRespBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobs.size();
    }
    class JobListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.job_new)
        TextView jobDate;
        @BindView(R.id.job_address)
        TextView jobAddress;
        @BindView(R.id.job_movesize)
        TextView jobMovesize;
        @BindView(R.id.job_establishtime)
        TextView jobEstablishtime;
        @BindView(R.id.job_time)
        TextView jobTime;
        @BindView(R.id.roster_item)
        LinearLayout rosterItem;
        @BindView(R.id.check)
        ImageView check;

        public JobListViewHolder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            if (!hasLoaded) {

            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, ActiveJobRespBean activeJobRespBean);
    }
}
