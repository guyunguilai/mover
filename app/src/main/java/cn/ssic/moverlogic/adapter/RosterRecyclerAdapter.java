package cn.ssic.moverlogic.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.fragment.RosterFragment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.utils.SplitTimeUtils;


/**
 * Created by Administrator on 2016/9/14.
 */
public class RosterRecyclerAdapter extends BaseRecyclerAdapter<RosterRecyclerAdapter.RosterViewHolder> {
    List<Job> mList;
    MainActivity mActivity;
    int dayIndex0 = 999;
    int dayIndex1 = 999;
    int dayIndex2 = 999;
    public static final int TODAY = 1;
    public static final int TOMMOROW = 2;
    public static final int UPCOMING = 3;
    OnItemClickListener onItemClickListener;

    public RosterRecyclerAdapter(MainActivity c, List<Job> list, RosterFragment fragment) {
        mActivity = c;
        mList = list;
        onItemClickListener = (OnItemClickListener) fragment;
    }

    @Override
    public RosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_roster, parent, false);
        return new RosterViewHolder(view, false);
    }

    @Override
    public RosterViewHolder getViewHolder(View view) {
        return new RosterViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(RosterViewHolder holder, int position, boolean isItem) {
        RosterViewHolder rosterRespBean = (RosterViewHolder) holder;
        rosterRespBean.jobAddress.setText(mList.get(position).getAddress());
        rosterRespBean.jobEstablishtime.setText(mList.get(position).getEstime() + " Hours");
        rosterRespBean.jobMovesize.setText(mList.get(position).getMovesize() + " CuM");
        String s = mList.get(position).getWeek() + "\n"
                + SplitTimeUtils.splitDate(mList.get(position).getMoveDate()) + "\n"
                +SplitTimeUtils.splitTime(mList.get(position).getStartTime());
        SpannableString startTime = new SpannableString(s);
        startTime.setSpan(new AbsoluteSizeSpan(15, true), s.lastIndexOf("\n"), s.lastIndexOf(" "), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        rosterRespBean.jobTime.setText(startTime);
        if ((position + 1) > 9) {
            rosterRespBean.jobNo.setText((position + 1) + "");
        } else {
            rosterRespBean.jobNo.setText("0" + (position + 1) + "");
        }
        if (mList.get(position).getStatus() == 1) {
            rosterRespBean.jobStatus.setVisibility(View.VISIBLE);
            rosterRespBean.jobStatus.setText("AWAITING");
            rosterRespBean.jobStatus.setTextColor(mActivity.getResources().getColor(R.color.holo_red_dark));
        } else if (mList.get(position).getStatus() == 2) {
            rosterRespBean.jobStatus.setVisibility(View.VISIBLE);
            rosterRespBean.jobStatus.setText("JOB ACCEPTED");
            rosterRespBean.jobStatus.setTextColor(mActivity.getResources().getColor(R.color.holo_green_dark));
        } else {
            rosterRespBean.jobStatus.setVisibility(View.GONE);
        }

        if (position == dayIndex0) {
            rosterRespBean.job_date.setVisibility(View.VISIBLE);
            rosterRespBean.job_date.setText("TODAY");
        } else if (position == dayIndex1) {
            rosterRespBean.job_date.setVisibility(View.VISIBLE);
            rosterRespBean.job_date.setText("TOMORROW");
        } else if (position == dayIndex2) {
            rosterRespBean.job_date.setVisibility(View.VISIBLE);
            rosterRespBean.job_date.setText("UPCOMING");
        } else {
            rosterRespBean.job_date.setVisibility(View.GONE);
        }
        if (mList.get(position).getReadMark() == 1) {
            rosterRespBean.check.setVisibility(View.VISIBLE);
        } else {
            rosterRespBean.check.setVisibility(View.INVISIBLE);
        }

        if (mList.get(position).getDay() == TODAY) {
            rosterRespBean.rosterItem.setBackgroundColor(mActivity.getResources().getColor(R.color.today_backgroud_color));
            rosterRespBean.jobInfo.setBackgroundColor(mActivity.getResources().getColor(R.color.today_sec_bk_color));
        } else if (mList.get(position).getDay() == TOMMOROW) {
            rosterRespBean.rosterItem.setBackgroundColor(mActivity.getResources().getColor(R.color.tomorrow_backgroud_color));
            rosterRespBean.jobInfo.setBackgroundColor(mActivity.getResources().getColor(R.color.tomorrow_sec_bk_color));
        } else if (mList.get(position).getDay() == UPCOMING) {
            rosterRespBean.rosterItem.setBackgroundColor(mActivity.getResources().getColor(R.color.upcoming_backgroud_color));
            rosterRespBean.jobInfo.setBackgroundColor(mActivity.getResources().getColor(R.color.upcoming_sec_bk_color));
        }
        rosterRespBean.rosterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position, mList.get(position));
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    public void refreshList(List<Job> list) {
        mList = list;
        for (int i = mList.size() - 1; i >= 0; i--) {
            switch (mList.get(i).getDay()) {
                case TODAY:
                    dayIndex0 = i;
                    break;
                case TOMMOROW:
                    dayIndex1 = i;
                    break;
                case UPCOMING:
                    dayIndex2 = i;
                    break;
            }
        }
        notifyDataSetChanged();
    }

    class RosterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.job_no)
        TextView jobNo;
        @BindView(R.id.job_address)
        TextView jobAddress;
        @BindView(R.id.job_movesize)
        TextView jobMovesize;
        @BindView(R.id.job_establishtime)
        TextView jobEstablishtime;
        @BindView(R.id.job_status)
        TextView jobStatus;
        @BindView(R.id.job_time)
        TextView jobTime;
        @BindView(R.id.roster_item)
        RelativeLayout rosterItem;
        @BindView(R.id.rl_item_roster1)
        RelativeLayout jobInfo;
        @BindView(R.id.job_date)
        TextView job_date;
        @BindView(R.id.check)
        ImageView check;



        public RosterViewHolder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (!hasLoaded) {

            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Job job);
    }
}
