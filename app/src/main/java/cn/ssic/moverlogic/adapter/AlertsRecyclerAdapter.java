package cn.ssic.moverlogic.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.SparseArray;
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
import cn.ssic.moverlogic.fragment.AlertsFragment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.utils.SplitTimeUtils;

/**
 * Created by Administrator on 2016/9/14.
 */
public class AlertsRecyclerAdapter extends BaseRecyclerAdapter {
    List<Job> mList;
    SparseArray<Integer> mTitleIndex;
    /**
     * JOB_CHANGE的五种状态
     */
    private int CHAMPIONSTATUS1=1;
    private int CHAMPIONSTATUS2=2;
    private int CHAMPIONSTATUS3=3;
    private int CHAMPIONSTATUS4=4;
    private int CHAMPIONSTATUS5=5;
    int statusIndex0;
    int statusIndex1;
    int statusIndex3;
    int statusIndex4;
    public static final int JOB_TYPE_NEW = 0;
    public static final int JOB_TYPE_AVIALBLE = 4;
    public static final int JOB_TYPE_CANCEL = 3;
    public static final int JOB_TYPE_CHANGES = 1;
    MainActivity mActivity;
    OnItemClickListener onItemClickListener;

    public AlertsRecyclerAdapter(MainActivity c, List<Job> list, AlertsFragment a) {
        mActivity = c;
        mList = list;
        onItemClickListener = (OnItemClickListener) a;
    }

    public void refreshList(List<Job> list) {
        mList = list;
        for (int i = mList.size() - 1; i >= 0; i--) {//倒序的方式检索各种status第一次出现的索引位置
            switch (mList.get(i).getStatus()) {
                case JOB_TYPE_NEW:
                    statusIndex0 = i;
                    break;
                case JOB_TYPE_CHANGES:
                    statusIndex1 = i;
                    break;
                case JOB_TYPE_CANCEL:
                    statusIndex3 = i;
                    break;
                case JOB_TYPE_AVIALBLE:
                    statusIndex4 = i;
                    break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemViewType(int position) {
//        if (position == 5) {
//            return 1;
//        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new AlertsViewHolder(view, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_alerts, parent, false);
            AlertsViewHolder holder = new AlertsViewHolder(view, false);
            view.setTag(holder);
            return holder;
        } else if (viewType == 1) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_alerts_cancellations_divider, parent, false);
            AlertsCancelDividerViewHolder holder = new AlertsCancelDividerViewHolder(view, false);
            view.setTag(holder);
            return holder;
        }

        return new AlertsViewHolder(view, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {
        switch (getItemViewType(position)) {
            case 0:
                AlertsViewHolder alertsViewHolder = (AlertsViewHolder) holder;
                if (position == statusIndex0) {
                    alertsViewHolder.jobDescripe.setVisibility(View.VISIBLE);
                } else if (position == statusIndex1) {
                    alertsViewHolder.jobDescripe.setVisibility(View.VISIBLE);
                } else if (position == statusIndex3) {
                    alertsViewHolder.jobDescripe.setVisibility(View.VISIBLE);
                } else if (position == statusIndex4) {
                    alertsViewHolder.jobDescripe.setVisibility(View.VISIBLE);
                } else {
                    alertsViewHolder.jobDescripe.setVisibility(View.GONE);
                }
                if (mList.get(position).getStatus() == JOB_TYPE_NEW) {
                    alertsViewHolder.jobDescripe.setText(R.string.alert_listTitle1);
                    alertsViewHolder.job_info_layout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_status0));
                } else if (mList.get(position).getStatus() == JOB_TYPE_CHANGES) {
                    alertsViewHolder.jobDescripe.setText(R.string.alert_listTitle2);
                    alertsViewHolder.job_info_layout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_status1));
                    if (CHAMPIONSTATUS1 == mList.get(position).getChangeStatus()) {
                        alertsViewHolder.jobNew.setText("DATE CHANGE");
                    }
                    if (CHAMPIONSTATUS2 == mList.get(position).getChangeStatus()) {
                        alertsViewHolder.jobNew.setText("TIME CHANGE");
                    }
                    if (CHAMPIONSTATUS3 == mList.get(position).getChangeStatus()) {
                        alertsViewHolder.jobNew.setText("VEHICLE SIZE CHANGE");
                    }
                    if (CHAMPIONSTATUS4 == mList.get(position).getChangeStatus()) {
                        alertsViewHolder.jobNew.setText("MEN CHANGE");
                    }
                    if (CHAMPIONSTATUS5 == mList.get(position).getChangeStatus()) {
                        alertsViewHolder.jobNew.setText("CHANGES");
                    }
                } else if (mList.get(position).getStatus() == JOB_TYPE_CANCEL) {
                    alertsViewHolder.jobDescripe.setText(R.string.alert_listTitle3);
                    alertsViewHolder.job_info_layout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_status3));
                } else if (mList.get(position).getStatus() == JOB_TYPE_AVIALBLE) {
                    alertsViewHolder.jobDescripe.setText(R.string.alert_listTitle4);
                    alertsViewHolder.job_info_layout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_status4));
                }
                alertsViewHolder.jobAddress.setText(TextUtils.isEmpty(mList.get(position).getAddress())?"":
                        mList.get(position).getAddress());
                alertsViewHolder.jobEstablishtime.setText(TextUtils.isEmpty(mList.get(position).getEstime())?"":mList.get(position).getEstime() + "Hours");
                alertsViewHolder.jobMovesize.setText(mList.get(position).getMovesize() + "CuM");
                if (mList.get(position).getReadMark() == 1) {
                    alertsViewHolder.check.setVisibility(View.VISIBLE);
                } else {
                    alertsViewHolder.check.setVisibility(View.INVISIBLE);
                }
                String s = mList.get(position).getWeek() + "\n"
                        + SplitTimeUtils.splitDate(mList.get(position).getMoveDate()) + "\n"
                        + SplitTimeUtils.splitTime(mList.get(position).getStartTime());
                SpannableString startTime = new SpannableString(s);
                startTime.setSpan(new AbsoluteSizeSpan(15, true), s.lastIndexOf("\n"), s.lastIndexOf(" "), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                alertsViewHolder.jobTime.setText(startTime);
                alertsViewHolder.jobNew.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
                alertsViewHolder.job_info_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onItemClickListener.onItemClick(v, position, mList.get(position));
                    }
                });
                break;
            case 1:
//                AlertsCancelDividerViewHolder alertsCancelDividerViewHolder = (AlertsCancelDividerViewHolder) holder;
//                alertsCancelDividerViewHolder.jobDescripe.setText("CANCELLATIONS");
                break;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    class AlertsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.job_descripe)
        TextView jobDescripe;
        @BindView(R.id.job_new)
        TextView jobNew;
        @BindView(R.id.job_address)
        TextView jobAddress;
        @BindView(R.id.job_movesize)
        TextView jobMovesize;
        @BindView(R.id.job_establishtime)
        TextView jobEstablishtime;
        @BindView(R.id.job_infos)
        TextView jobInfos;
        @BindView(R.id.job_time)
        TextView jobTime;
        @BindView(R.id.job_info_layout)
        RelativeLayout job_info_layout;
        @BindView(R.id.check)
        ImageView check;

        public AlertsViewHolder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (!hasLoaded) {

            }
        }
    }

    class AlertsCancelDividerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.job_descripe)
        TextView jobDescripe;

        public AlertsCancelDividerViewHolder(View itemView, boolean hasLoaded) {
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
