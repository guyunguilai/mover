package cn.ssic.moverlogic.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.fragment.AlertsFragment;
import cn.ssic.moverlogic.net2request.Equipment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.SplitTimeUtils;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/14.
 */
public class AlertInfoAdapter extends BaseRecyclerAdapter {
    JobDetail mJobDetail;
    MainActivity mActivity;
    int jobId;

    public AlertInfoAdapter(MainActivity c, Job job, AlertsFragment fragment) {
        mActivity = c;
        loadJobInfo(job.getJobId());
    }

    private void loadJobInfo(int jobid) {
        this.jobId = jobid;
        Subscription subscription = RetrofitHttp.getInstance()
                .getAlertJob(jobid, App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getAlertJobRespBean -> {
                    Logger.e(getAlertJobRespBean.getMsg());
//                    Toast.makeText(mActivity, "getAlertJob success", Toast.LENGTH_SHORT).show();
                    refreshList(getAlertJobRespBean.getJobdetail());
                }, throwable -> {
//                    Toast.makeText(mActivity, "getAlertJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e(throwable.getMessage());
                });
    }

    public void refreshList(JobDetail job) {
        mJobDetail = job;
        notifyDataSetChanged();
    }

    public void refreshItsself() {
        loadJobInfo(jobId);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new AlertsInfoItem0Holder(view, false);
    }

    @Override
    public int getAdapterItemCount() {
        return 4;
    }

    public int getAdapterItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview_item1, parent, false);;
        if (viewType == 0) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview_item1, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview_item2, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview_item3, parent, false);
        } else if (viewType == 3) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_alerts_recyleview_item4, parent, false);
        }
        return new AlertsInfoItem0Holder(view, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {
        if (null == mJobDetail) {
            return;
        }
        AlertsInfoItem0Holder holder0 = (AlertsInfoItem0Holder) holder;
        switch (position) {
            case 0:
                if (null != mJobDetail.getTimedate()) {
                    holder0.date.setText(SplitTimeUtils.splitDate(mJobDetail.getTimedate().getDate()));
                }
                holder0.starttime.setText(SplitTimeUtils.splitTime(mJobDetail.getTimedate().getStarttime()));
                holder0.type.setText(mJobDetail.getJobtype().getType());
                holder0.job_movesize.setText(mJobDetail.getJobtype().getMovesize() + " CuM");
                holder0.estTime.setText(mJobDetail.getJobtype().getEstime()+" Hrs");
                holder0.truckType.setText(mJobDetail.getVehicleDetails().getVehicleType());
                holder0.capacity.setText(mJobDetail.getVehicleDetails().getCapacity() + " CuM");
                holder0.gvm.setText(mJobDetail.getVehicleDetails().getGvm() + " Tonne");
                holder0.tm.setText(mJobDetail.getVehicleDetails().getTm() + " Tonne");
                holder0.men.setText(mJobDetail.getVehicleDetails().getMen() + "");
                if (null != mJobDetail.getEquipment() && mJobDetail.getEquipment().size() > 0) {
                    String no = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getEquipment().size();i++){
                        no += (i + 1) + ".\n";
                        value += mJobDetail.getEquipment().get(i).getEquipment()+"\n";
                    }
                    holder0.equipment_no.setText(no);
                    holder0.equipment_value.setText(value);
                } else {
                    holder0.equipment_no.setText("");
                    holder0.equipment_value.setText("");
                }
                break;
            case 1:
                if (null==mJobDetail.getAddressDetails()) {
                    return;
                }
                for (int i = 0; i < mJobDetail.getAddressDetails().size(); i++) {
                    LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_inner_items, null);
                    String title = "Address " + (i + 1) + ":" + ("1".equals(mJobDetail.getAddressDetails().get(i).getAddressType()) ? "PICK-UP" : "DROP-OFF");
                    TextView address1_text_clickable = (TextView) ll.findViewById(R.id.address1_text_clickable);
                    address1_text_clickable.setText(title);
                    TextView address = (TextView) ll.findViewById(R.id.address);
                    address.setText(mJobDetail.getAddressDetails().get(i).getAddress() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getStreet() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getSuburb() + " " +
                            mJobDetail.getAddressDetails().get(i).getState() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getPostCode() + "," +
                            mJobDetail.getAddressDetails().get(i).getCountry() + "");
                    address1_text_clickable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            address.setVisibility(View.INVISIBLE == address.getVisibility() ? View.VISIBLE : View.INVISIBLE);
                        }
                    });

                    holder0.layout1.addView(ll);
                }
                break;
            case 2:
                holder0.basepay_pay_type.setText(mJobDetail.getClientCharges().getVehiclecharges().getRatetype());
                holder0.basepay_pay_amount.setText("$"+mJobDetail.getClientCharges().getVehiclecharges().getRate() + "");
              /*  holder0.callOutFee.setText(mJobDetail.getClientCharges().getVehiclecharges().getCalloutfee());
                holder0.basepay_return_fee.setText(mJobDetail.getClientCharges().getVehiclecharges().getReturnfee());*/
                if (null != mJobDetail.getClientCharges().getExtraCharges() && mJobDetail.getClientCharges().getExtraCharges().size() > 0) {
                    String no = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getClientCharges().getExtraCharges().size(); i++) {
                        no +=  mJobDetail.getClientCharges().getExtraCharges().get(i).getChargeName()+ "\n";
                        value += mJobDetail.getClientCharges().getExtraCharges().get(i).getChargeValue() + "\n";
                    }
                    holder0.extra_key0.setText(no);
                    holder0.extra_value0.setText(value);
                }else{
                    holder0.extra_key0.setText("");
                    holder0.extra_value0.setText("");
                }
                if(null == mJobDetail.getClientCharges()  ||  null == mJobDetail.getClientCharges().getTimeCalculation()){
                    return;
                }else{
                    holder0.minimum_time.setText(mJobDetail.getClientCharges().getTimeCalculation().getMinimumTime()+"");
                    holder0.time_increments.setText(mJobDetail.getClientCharges().getTimeCalculation().getTimeincrements()+"");
                }
                break;
            case 3:
//                setWeb(holder0.mWebview);

                break;
        }
    }

    private void setWeb(WebView mWebview) {
        String url = "http://baidu.com";
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF -8") ;
        webSettings.setJavaScriptEnabled(true); // 就可以打开JavaScript.
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);
        mWebview.loadUrl(url);


        mWebview.setWebChromeClient(new WebChromeClient());
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
//              return super.shouldOverrideUrlLoading(view, url);
                mWebview.loadUrl(url);
                return true;
            }
        });
    }

    class AlertsInfoItem0Holder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.date)
        TextView date;
        @Nullable
        @BindView(R.id.equipment_no)
        TextView equipment_no;
        @Nullable
        @BindView(R.id.equipment_value)
        TextView equipment_value;
        @Nullable
        @BindView(R.id.starttime)
        TextView starttime;
        @Nullable
        @BindView(R.id.type)
        TextView type;
        @Nullable
        @BindView(R.id.job_movesize)
        TextView job_movesize;
        @Nullable
        @BindView(R.id.est_time)
        TextView estTime;
        @Nullable
        @BindView(R.id.truck_type)
        TextView truckType;
        @Nullable
        @BindView(R.id.capacity)
        TextView capacity;
        @Nullable
        @BindView(R.id.gvm)
        TextView gvm;
        @Nullable
        @BindView(R.id.tm)
        TextView tm;
        @Nullable
        @BindView(R.id.men)
        TextView men;
        @Nullable
        @BindView(R.id.address_layout1)
        LinearLayout layout1;
        @Nullable
        @BindView(R.id.basepay_pay_type)
        TextView basepay_pay_type;
        @Nullable
        @BindView(R.id.basepay_pay_amount)
        TextView basepay_pay_amount;
     /*   @Nullable
        @BindView(R.id.call_out_fee)
        TextView callOutFee;
        @Nullable
        @BindView(R.id.basepay_return_fee)
        TextView basepay_return_fee;*/
        @Nullable
        @BindView(R.id.extra_key0)
        TextView extra_key0;
        @Nullable
        @BindView(R.id.extra_value0)
        TextView extra_value0;
        @Nullable
        @BindView(R.id.minimum_time)
        TextView minimum_time;
        @Nullable
        @BindView(R.id.time_increments)
        TextView time_increments;

        @Nullable
        @BindView(R.id.alerts_item4_webView)
        WebView mWebview;
        public AlertsInfoItem0Holder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
