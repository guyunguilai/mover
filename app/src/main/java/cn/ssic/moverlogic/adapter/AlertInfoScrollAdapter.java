package cn.ssic.moverlogic.adapter;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XWebView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.fragment.AlertsFragment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.SplitTimeUtils;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/14.
 */
public class AlertInfoScrollAdapter extends PagerAdapter {
    JobDetail mJobDetail;
    MainActivity mActivity;
    int jobId;
    int layoutIds[] = new int[]{
            R.layout.fragment_alerts_recyleview_item1,
            R.layout.fragment_alerts_recyleview_item2,
            R.layout.fragment_alerts_recyleview_item3,
            R.layout.fragment_alerts_recyleview_item4,
    };

    public AlertInfoScrollAdapter(MainActivity c, Job job, AlertsFragment fragment) {
        mActivity = c;
        loadJobInfo(job.getJobId());
    }

    private void loadJobInfo(int jobid) {
        this.jobId = jobid;
        Subscription subscription = RetrofitHttp.getInstance()
                .getAlertJob(jobid, App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe( getAlertJobRespBean-> {
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
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mActivity).inflate(layoutIds[position], container, false);
        if (null == mJobDetail) {
            return view;
        }
        AlertsInfoItem0Holder holder = new AlertsInfoItem0Holder(view, false);
//        holder.item_refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//            public void onRefresh() {
//                refreshItsself();
//                holder.item_refreshview.stopRefresh();
//            }
//        });

        switch (position) {
            case 0:
                if (null != mJobDetail.getTimedate()) {
                    holder.date.setText(SplitTimeUtils.splitDate(mJobDetail.getTimedate().getDate()) + "(" + mJobDetail.getTimedate().getWeek() + ")");
                }
//                holder.starttime.setText(SplitTimeUtils.splitTime(mJobDetail.getTimedate().getStarttime()));
                holder.starttime.setText(mJobDetail.getTimedate().getStarttime()==null ? "" : mJobDetail.getTimedate().getStarttime());
                holder.type.setText(mJobDetail.getJobtype().getType());
                holder.job_movesize.setText(mJobDetail.getJobtype().getMovesize() + " CuM");
                holder.estTime.setText(TextUtils.isEmpty(mJobDetail.getJobtype().getEstime()) ? "" : mJobDetail.getJobtype().getEstime() + " Hrs");
                holder.truckType.setText(TextUtils.isEmpty(mJobDetail.getVehicleDetails().getVehicleType()) ? "" : mJobDetail.getVehicleDetails().getVehicleType());
                holder.capacity.setText(TextUtils.isEmpty(mJobDetail.getVehicleDetails().getCapacity()) ? "" : mJobDetail.getVehicleDetails().getCapacity() + " CuM");
                holder.gvm.setText(TextUtils.isEmpty(mJobDetail.getVehicleDetails().getGvm()) ? "" : mJobDetail.getVehicleDetails().getGvm() + " Tonne");
                holder.tm.setText(TextUtils.isEmpty(mJobDetail.getVehicleDetails().getTm()) ? "" : mJobDetail.getVehicleDetails().getTm() + " Tonne");
                holder.men.setText(mJobDetail.getVehicleDetails().getMen() + "");
                if (null != mJobDetail.getEquipment() && mJobDetail.getEquipment().size() > 0) {
                    String no = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getEquipment().size(); i++) {
                        no +=  mJobDetail.getEquipment().get(i).getEquipmentQuantity() + "\n";
                        value += mJobDetail.getEquipment().get(i).getEquipment() + "\n";
                    }
                    holder.equipment_no.setText(value);
                    holder.equipment_value.setText(no);
                } else {
                    holder.equipment_no.setText("");
                    holder.equipment_value.setText("");
                }
                break;
            case 1:
                if (null == mJobDetail.getAddressDetails()) {
                    return view;
                }

                for (int i = 0; i < mJobDetail.getAddressDetails().size(); i++) {
                    LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_inner_items, null);
                    LinearLayout llAddress = (LinearLayout) ll.findViewById(R.id.ll_address);
                    LinearLayout llNotes = (LinearLayout) ll.findViewById(R.id.ll_address_notes);
                    String title = "Address " + (i + 1) + ":" + (mJobDetail.getAddressDetails().get(i).getAddressType() == 1 ? "PICK-UP" : "DROP-OFF");
                    TextView address1_text_clickable = (TextView) ll.findViewById(R.id.address1_text_clickable);

                    TextView property = (TextView) ll.findViewById(R.id.address_property);
                    TextView levels = (TextView) ll.findViewById(R.id.address_levels);
                    TextView floor = (TextView) ll.findViewById(R.id.address_floor);
                    TextView access = (TextView) ll.findViewById(R.id.address_access);

                    property.setText(mJobDetail.getAddressDetails().get(i).getProperty()==null ? "" : mJobDetail.getAddressDetails().get(i).getProperty());
                    levels.setText(mJobDetail.getAddressDetails().get(i).getLevels()==null ? "" : mJobDetail.getAddressDetails().get(i).getLevels());
                    floor.setText(mJobDetail.getAddressDetails().get(i).getFloor()+"");
                    access.setText(mJobDetail.getAddressDetails().get(i).getAccess()==null ? "" : mJobDetail.getAddressDetails().get(i).getAccess());

                    address1_text_clickable.setText(title);
                    TextView address = (TextView) ll.findViewById(R.id.address);

                    address1_text_clickable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llAddress.setVisibility(View.GONE == llAddress.getVisibility() ? View.VISIBLE : View.GONE);
                        }
                    });
                    if (null != mJobDetail.getAddressDetails().get(i).getJobAddressNotes()){
                        for (int j = 0; j <mJobDetail.getAddressDetails().get(i).getJobAddressNotes().size(); j++) {
                            LinearLayout ll1 = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_notes, null);
                            TextView tvNotes = (TextView) ll1.findViewById(R.id.tv_notes_key);
                            TextView tvNotesValue = (TextView) ll1.findViewById(R.id.tv_notes_value);
                            tvNotes.setText("Notes "+(j+1));
                            tvNotesValue.setText(mJobDetail.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes()==null ? ""
                                    : mJobDetail.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes());
                            llNotes.addView(ll1);
                        }
                    }

                    address.setText(mJobDetail.getAddressDetails().get(i).getAddress() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getStreet() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getSuburb() + " " +
                            mJobDetail.getAddressDetails().get(i).getState() + "\n" +
                            mJobDetail.getAddressDetails().get(i).getPostCode() + "," +
                            mJobDetail.getAddressDetails().get(i).getCountry() + "");

                    holder.layout1.addView(ll);
                }
                break;
            case 2:
                holder.basepay_pay_type.setText(TextUtils.isEmpty(mJobDetail.getClientCharges().getVehiclecharges().getRatetype()) ? ""
                        : mJobDetail.getClientCharges().getVehiclecharges().getRatetype());
                holder.basepay_pay_amount.setText("$" + mJobDetail.getClientCharges().getVehiclecharges().getRate() + "");

                if (null != mJobDetail.getClientCharges().getExtraCharges() && mJobDetail.getClientCharges().getExtraCharges().size() > 0) {
                    holder.llCharges.setVisibility(View.VISIBLE);
                    String no = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getClientCharges().getExtraCharges().size(); i++) {

                        no += TextUtils.isEmpty(mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeName()) ? ""
                                + "\n" : mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeName() + "\n";
                        String extraValue = mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeValue() ==null ? "" :
                                mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeValue();
                        String extraQuality = mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeQuantity()==null ? "" :
                                mJobDetail.getClientCharges().getExtraCharges().get(i).getExtraChargeQuantity();
                        value +=(extraValue+extraQuality)+ "\n";
                    }
                    holder.extra_key0.setText(no);
                    holder.extra_value0.setText(value);




                } else {
                    holder.llCharges.setVisibility(View.GONE);
                }
                if (null == mJobDetail.getClientCharges() || null == mJobDetail.getClientCharges().getTimeCalculation()) {
                    return view;
                } else {
                    holder.minimum_time.setText(TextUtils.isEmpty(mJobDetail.getClientCharges().getTimeCalculation().getMinimumTime()) ? "" :
                            mJobDetail.getClientCharges().getTimeCalculation().getMinimumTime());
                    holder.time_increments.setText(TextUtils.isEmpty(mJobDetail.getClientCharges().getTimeCalculation().getTimeincrements()) ? "" :
                            mJobDetail.getClientCharges().getTimeCalculation().getTimeincrements());
                }
                break;
            case 3:
//                holder.term_text.setText(R.string.term_content);
                setWeb(holder.mWebview, holder,mJobDetail.getTerms());
                break;
        }
        container.addView(view, 0);
        return view;
    }

    private void setWeb(XWebView mWebview, AlertsInfoItem0Holder holder,String terms) {
//        String url = "http://news.sina.com.cn/";
//        String url = "http://www.baidu.com";

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF -8");
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
        mWebview.loadUrl(terms);

        mWebview.setWebViewClient(new WebViewClient(){
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO 自动生成的方法存根
                view.loadUrl(url);
                return true;
            }
        });
        mWebview.setWebChromeClient(new NewWebViewClient(holder));
    }

    private class NewWebViewClient extends WebChromeClient {
        AlertsInfoItem0Holder infoItem0Holder;

        public NewWebViewClient(AlertsInfoItem0Holder holder) {
            infoItem0Holder = holder;
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                infoItem0Holder.progressBar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == infoItem0Holder.progressBar.getVisibility()) {
                    infoItem0Holder.progressBar.setVisibility(View.VISIBLE);
                }
                infoItem0Holder.progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class AlertsInfoItem0Holder extends RecyclerView.ViewHolder {
        //        @Nullable
//        @BindView(R.id.item_refreshview)
//        XRefreshView item_refreshview;
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
        @Nullable
        @BindView(R.id.ll_extra_charges)
        LinearLayout llCharges;

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
        XWebView mWebview;
        @Nullable
        @BindView(R.id.web_progress)
        NumberProgressBar progressBar;


        public AlertsInfoItem0Holder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
