package cn.ssic.moverlogic.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.adapter.JobListAdapter;
import cn.ssic.moverlogic.adapter.JobSheetInfoScrollAdapter;
import cn.ssic.moverlogic.bean.ActiveJobRespBean;
import cn.ssic.moverlogic.bean.GetPayModeRespBean;
import cn.ssic.moverlogic.bean.PayCreditCardReqBean;
import cn.ssic.moverlogic.customviews.DialogCreater;
import cn.ssic.moverlogic.customviews.GravitySnapHelper;
import cn.ssic.moverlogic.customviews.spinner.NiceSpinner;
import cn.ssic.moverlogic.customviews.wheelView.LoopView;
import cn.ssic.moverlogic.customviews.wheelView.OnItemSelectedListener;
import cn.ssic.moverlogic.net2request.CardType;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.Jobextracharge;
import cn.ssic.moverlogic.net2request.PayMode;
import cn.ssic.moverlogic.net2request.TransactionFee;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.CustomDialog;
import cn.ssic.moverlogic.utils.DialogUtil;
import cn.ssic.moverlogic.utils.MultipartUtil;
import cn.ssic.moverlogic.utils.MyEditText;
import cn.ssic.moverlogic.utils.ViewsUtils;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;

import static cn.ssic.moverlogic.R.id.gesture_view;
import static cn.ssic.moverlogic.R.id.point;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by yangjinyu on 2016/9/18.
 */
public class JobFragment extends Fragment implements JobListAdapter.OnItemClickListener,
        GestureOverlayView.OnGesturePerformedListener, GestureOverlayView.OnGestureListener, View.OnClickListener {
    public MainActivity mActivity;
    private int MIN_MARK = 0;
    private int MAX_MARK = 10;
    public static final int PAUSE_REASON_BREAK = 0;
    public static final int PAUSE_REASON_FINISH = 1;
    public static final int PAUSE_REASON_CANCEL = 2;
    public static final int PAY_BY_CASH = 1;
    public static final int PAY_BY_CARD = 2;
    public static final int PAY_BY_LATER = 3;

    public static final int JOB_NO_ACTIVE = 10000;
    public static final int JOB_LIST = 11;
    public static final int JOB_DISPATCH_1 = 0;
    public static final int JOB_DISPATCH_2 = 1;
    public static final int JOB_DISPATCH_3 = 2;
    public static final int JOB_INSPECTION = 4;
    public static final int JOB_INSPECTION_1 = 41;
    public static final int JOB_PRE_1 = 5;
    public static final int JOB_PRE_2 = 6;
    public static final int JOB_PRE_21 = 61;
    public static final int JOB_IN_PLAY = 7;
    public static final int JOB_UPLOAD_PIC = 71;
    public static final int JOB_PAUSE_REASON = 8;
    public static final int JOB_PAUSE_TIME = 81;
    public static final int JOB_PAUSE_TOADD = 82;
    public static final int JOB_PAUSE_SIGN = 83;
    public static final int JOB_SHEET = 100;
    public static final int JOB_FINISH_COLLECT = 200;
    public static final int JOB_FINISH_PAYMENT_CASH = 201;
    public static final int JOB_FINISH_COLLECT_REST = 202;
    public static final int JOB_FINISH_COLLECT_FINISH = 205;
    public static final int JOB_FINISH_PAYMENT_CARD = 203;
    public static final int JOB_FINISH_CHANGE_CARD = 204;
    public static final int JOB_FINISH_INVOICE = 206;
    public static final int JOB_FINISH_COMPLETE = 207;

    public static final int START_TIMER = 300;
    public static final int PAUSE_TIMER = 301;
    public static final int STOP_TIME = 302;
    public static final int DISMISS_DIALOG = 200;

    @BindView(R.id.job_view_container)
    RelativeLayout jobViewContainer;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;
    @Nullable
    @BindView(R.id.btn_back)
    ImageView back;
    @Nullable
    @BindView(R.id.next)
    ImageView next;
    @Nullable
    @BindView(R.id.money)
    TextView money_tv;
    @Nullable
    @BindView(R.id.job_dispatch_tv)
    TextView job_dispatch_tv;
    @Nullable
    @BindView(R.id.timer)
    TextView job_timer_tv;
    @Nullable
    @BindView(R.id.pause_timer)
    TextView job_pause_timer_tv;
    @Nullable
    @BindView(R.id.attach_pic_iv)
    ImageView attach_pic_iv;
    @Nullable
    @BindView(R.id.job_min_time)
    TextView minTimeSp;
    String minTimeStr = "Min Time";
    int minTimeIndex = 0;
    @Nullable
    @BindView(R.id.job_max_time)
    TextView maxTimeSp;
    String maxTimeStr = "Max Time";
    int maxTimeIndex = 0;
    @Nullable
    @BindView(gesture_view)
    GestureOverlayView gestureOverlayView;

    ViewPager mViewPager;
    JobSheetInfoScrollAdapter jobSheetInfoScrollAdapter=null;
    JobListAdapter jobListAdapter;
    RecyclerView jobListRecyclerView;

    @Nullable
    @BindView(R.id.finish_cash_sp)
    TextView finish_cash_sp;
    @Nullable
    @BindView(R.id.month_sp)
    TextView month_sp;
    @Nullable
    @BindView(R.id.ll_change_month)
    LinearLayout ll_change_month;
    @Nullable
    @BindView(R.id.month_right_sp)
    TextView month_right_sp;

    @Nullable
    @BindView(R.id.year_sp)
    EditText job_cvv;
    @Nullable
    @BindView(R.id.card_sp)
    TextView card_sp;
    int card_sp_index;
    @Nullable
    @BindView(R.id.sp_terms)
    NiceSpinner termSp;
    @Nullable
    @BindView(R.id.sp_charge)
    NiceSpinner chargeSp;
    @Nullable
    @BindView(R.id.break_reason_sp)
    TextView breakSp;
    @Nullable
    @BindView(R.id.add_extra_name_sp)
    TextView extra_name_sp;
    @Nullable
    @BindView(R.id.add_extra_cost_sp)
    EditText add_extra_cost_sp;
    @Nullable
    @BindView(R.id.add_extra_quantity_sp)
    TextView extra_quantity_sp;
    @Nullable
    @BindView(R.id.sp_sign_terms)
    NiceSpinner sp_sign_terms;
    @Nullable
    @BindView(R.id.sp_sign_charge)
    NiceSpinner sp_sign_charge;
    @Nullable
    @BindView(R.id.cash_no_et)
    EditText cash_no_et;
    @Nullable
    @BindView(R.id.creadit_card_et)
    EditText creadit_card_et;
    @Nullable
    @BindView(R.id.card_tips)
    TextView card_tips;
    @Nullable
    @BindView(R.id.pay_credit_amount)
    TextView pay_credit_amount;
    @Nullable
    @BindView(R.id.pay_credit_surcharge)
    TextView pay_credit_surcharge;
    @Nullable
    @BindView(R.id.pay_credit_surcharge_amount)
    TextView pay_credit_surcharge_amount;
    @Nullable
    @BindView(R.id.pay_credit_total)
    TextView pay_credit_total;

    Dialog mSpinnerDialog;
    List<String> spinnerList = new ArrayList<>();
    List<Jobextracharge> extraChargeList = new ArrayList<>();
    int extraChargeIndex;
    List<String> attachmentPicPath;
    List<String> cardType = new ArrayList<>();
    List<String> yearList = new ArrayList<>();
    public Job mJob;
    public int fragmentStatus = 0;
    public int tempFragmentStatus = 11;
    public int tempSheetStatus = 110;
    public int pauseReason = 0;
    public int payType = 1;
    public int timeCount = 0;
    public String timerStr;
    double totalMoney;
    double cashMoney;
    double cardMoney;
    double surchargePercentage = 5.0;
    boolean isPreSigned;
    boolean isInspectSigned;
    int extraQuantity;
    boolean isSignature;

    double altitude;
    DialogUtil mDialogUtilog;
    List<View> views = new ArrayList<>();
    CustomDialog mDialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_TIMER:
                    timeCount++;
                    timerStr = (((timeCount / 3600) > 9) ? (timeCount / 3600 + "") : ("0" + timeCount / 3600))
                            + ":" + (((timeCount % 3600 / 60) > 9) ? (timeCount % 3600 / 60 + "") : ("0" + timeCount % 3600 / 60))
                            + ":" + (((timeCount % 60) > 9) ? (timeCount % 60 + "") : ("0" + timeCount % 60));
                    if (null != job_timer_tv) {
                        job_timer_tv.setText(timerStr);
                    }
                    handler.sendEmptyMessageDelayed(START_TIMER, 1000);
                    break;
                case PAUSE_TIMER:

                    break;
                case STOP_TIME:
                    break;
                case DISMISS_DIALOG:
                    spinnerList.clear();
                    break;
            }
        }
    };
    private LinearLayout mLlPoint;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(mActivity).inflate(R.layout.fragment_job, null);
//        fragmentStatus = JOB_LIST;
        ButterKnife.bind(this, view);
        jobViewContainer.addView(getFragmentView());
        mDialogUtilog = new DialogUtil(mActivity);
        return view;
    }

    View getFragmentView() {
        View view = null;
        switch (fragmentStatus) {
            case JOB_LIST:
                gonePanel();
                jobViewContainer.setGravity(Gravity.TOP);
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_list, null);
                jobListAdapter = new JobListAdapter(mActivity, this);
                jobListRecyclerView = (RecyclerView) view.findViewById(R.id.job_list_recyclerview);
                jobListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                jobListRecyclerView.setAdapter(jobListAdapter);
                new GravitySnapHelper(Gravity.START).attachToRecyclerView(jobListRecyclerView);
                break;
            case JOB_NO_ACTIVE:
                jobViewContainer.setGravity(Gravity.CENTER);
                gonePanel();
                mActivity.showPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_noactive, null);
                break;
            case JOB_DISPATCH_1:
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setTv_title(R.string.job_dispatch);
                visiablePanel();
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_dispatch, null);
                break;
            case JOB_DISPATCH_2:
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setTv_title(R.string.job_dispatch);
                visiablePanel();
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_dispatch, null);
                break;
            case JOB_DISPATCH_3:
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setTv_title(R.string.job_dispatch);
                visiablePanel();
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_dispatch, null);
                break;
            case JOB_INSPECTION:
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setTv_title(R.string.job_inspection);
                mActivity.setLeftMenuBack();
                mActivity.showRightMenu();
                inspectionPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_inspection1, null);
                break;
            case JOB_INSPECTION_1:
                mActivity.hideLeftMenu();
                pauseToaddPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_pause_toadd, null);
                getExtraCharge();
                break;
            case JOB_PRE_1://jiedian
                mActivity.setTv_title(R.string.job_prejob);
                mActivity.showRightMenu();
                leftBackPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_inspection2, null);
                break;
            case JOB_PRE_2://jiedian
                mActivity.showRightMenu();
                visiablePanel();
                startPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_inspection3, null);
                break;
            case JOB_PRE_21:
                mActivity.showRightMenu();
                gonePanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_inspection4, null);
                gestureOverlayView = (GestureOverlayView) view.findViewById(R.id.gesture_view);
                gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                isSignature = true;
                                break;
                        }
                        return false;
                    }
                });
                //第一次签名
                break;
            case JOB_IN_PLAY://jiedian
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setLeftMenuBack();
                mActivity.hidePanel();
                mActivity.setTv_title(R.string.job_in_play);
                mActivity.showRightMenu();
                visiablePanel();
                inPlayPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_inplay, null);
                break;
            case JOB_UPLOAD_PIC:
                mActivity.hideLeftMenu();
                mActivity.showRightMenu();
                uploadPicPanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_uploadpic, null);
                break;
            case JOB_PAUSE_REASON:
                mActivity.hideLeftMenu();
                mActivity.showRightMenu();
                mActivity.setTv_title(R.string.job_pause);
                visiablePanel();
                pausePanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_pause, null);
                break;
            case JOB_PAUSE_TIME:
                mActivity.showRightMenu();
                pauseTimePanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_pause_time, null);
                break;
            case JOB_PAUSE_TOADD:
                getExtraCharge();
                mActivity.hideLeftMenu();
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_pause_toadd, null);
                pauseToaddPanel();
                break;
            case JOB_PAUSE_SIGN:
                mActivity.hideRightMenu();
                gonePanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_pause_tofinish, null);
                gestureOverlayView = (GestureOverlayView) view.findViewById(R.id.gesture_view);
                gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                isSignature = true;
                                break;
                        }
                        return false;
                    }
                });
                break;
            case JOB_SHEET:
                gonePanel();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_jobsheet, null);
                mViewPager = (ViewPager) view.findViewById(R.id.job_sheet_viewpager);
                mLlPoint = (LinearLayout) view.findViewById(point);
                addPoints(13);      //对应的点数量
                break;
            case JOB_FINISH_COLLECT://jiedian
                jobViewContainer.setGravity(Gravity.CENTER);
                mActivity.setLeftMenuBack();
                mActivity.showRightMenu();
                mActivity.setTv_title(R.string.job_payment);
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_collect, null);
                endPanel();
                getPaymentAmt();
                break;
            case JOB_FINISH_PAYMENT_CASH:
                mActivity.hideLeftMenu();
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_cash, null);
                addPaymentPanel();
                getPayCardType();
                getPayMode();
                break;
            case JOB_FINISH_COLLECT_REST:
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_collect_rest, null);
                endPanel();
                break;
            case JOB_FINISH_PAYMENT_CARD:
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_card, null);
                break;
            case JOB_FINISH_CHANGE_CARD:
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_change, null);
                panelLeft.setVisibility(View.VISIBLE);
                panelLeft.setImageResource(R.drawable.panel_back);
                panelRight.setImageResource(R.drawable.percentage);
                panelMid.setImageResource(R.drawable.add);
                panelMid.setVisibility(View.VISIBLE);
                month_sp = (TextView) view.findViewById(R.id.month_sp);
                month_right_sp = (TextView) view.findViewById(R.id.month_right_sp);
                card_sp = (TextView) view.findViewById(R.id.card_sp);
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                month_sp.setText(month + "");
                month_right_sp.setText("/" + year + "");
                card_sp.setText(cardType.get(0));
                panelMid.setOnClickListener(this);
                panelRight.setVisibility(View.VISIBLE);
                break;
            case JOB_FINISH_COLLECT_FINISH:
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_collect_finish, null);
                break;
            case JOB_FINISH_INVOICE:
                mActivity.showRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_collect_invoice, null);
                invoicePanel();
                break;
            case JOB_FINISH_COMPLETE:
                mActivity.hideRightMenu();
                view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_job_finish_collect_complete, null);
                finishPanel();
                break;
        }
        return view;
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

//        jobSheetInfoScrollAdapter = new JobSheetInfoScrollAdapter(mActivity, mJob.getJobId());
        mViewPager.setAdapter(jobSheetInfoScrollAdapter);
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

    void showTimer() {
        if (!handler.hasMessages(START_TIMER)) {
            handler.sendEmptyMessage(START_TIMER);
        }
    }

    public void loadView() {
        if(jobViewContainer!=null){
            jobViewContainer.removeAllViews();
        }
        jobViewContainer.addView(getFragmentView());
        ButterKnife.bind(this, getView());
        loadViewAfterBind();
    }

    public void loadViewAfterBind() {
        switch (fragmentStatus) {
            case JOB_LIST:
                mActivity.showPanel();
                break;
            case JOB_DISPATCH_1:
                mActivity.hidePanel();
                job_dispatch_tv.setText(R.string.job_dispatch_1);
                break;
            case JOB_DISPATCH_2:
                mActivity.hidePanel();
                job_dispatch_tv.setText(R.string.job_dispatch_2);
                break;
            case JOB_DISPATCH_3:
                mActivity.hidePanel();
                job_dispatch_tv.setText(R.string.job_dispatch_3);
                break;
            case JOB_PRE_1:
                mActivity.hideLeftMenu();
                minTimeSp.setText(minTimeStr);
                maxTimeSp.setText(maxTimeStr);
                break;
            case JOB_PRE_21:
                loadChooseSpinner();
                loadGestureView();
                break;
            case JOB_PAUSE_SIGN:
                loadSignSpinner();
                loadGestureView();
                break;
            case JOB_FINISH_PAYMENT_CASH:
                money_tv.setText(totalMoney + "");
                break;
            case JOB_FINISH_COLLECT_REST:
                money_tv.setText((totalMoney) + "");
                break;
            case JOB_FINISH_CHANGE_CARD:
                creadit_card_et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                money_tv.setText(cardMoney + "");
                double temp = cardMoney * surchargePercentage / 100;
                BigDecimal   b   =   new BigDecimal(temp);
                temp = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                pay_credit_amount.setText("$" + cardMoney);
                pay_credit_surcharge.setText(surchargePercentage + "%");
                pay_credit_surcharge_amount.setText("$" + temp);
                pay_credit_total.setText("$" + (cardMoney + temp));
                break;
            case JOB_IN_PLAY:
                showTimer();
                break;
           /* case JOB_PAUSE_REASON:
                job_pause_timer_tv.setText(timerStr);
                break;*/
            case JOB_PAUSE_TIME:
                pauseTimePanel();
                job_pause_timer_tv.setText(timerStr);
                if (handler.hasMessages(START_TIMER)) {
                    handler.removeMessages(START_TIMER);
                }
                break;
        }
    }

    void gonePanel() {
        panelLeft.setVisibility(View.GONE);
        panelMid.setVisibility(View.GONE);
        panelRight.setVisibility(View.GONE);
    }

    void endPanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.refresh);
        panelMid.setVisibility(View.VISIBLE);
        panelMid.setImageResource(R.drawable.end);
        panelRight.setVisibility(View.VISIBLE);
        panelRight.setImageResource(R.drawable.money);
    }

    void addPaymentPanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelMid.setVisibility(View.VISIBLE);
        panelMid.setImageResource(R.drawable.add);
        panelRight.setVisibility(View.VISIBLE);
        panelRight.setImageResource(R.drawable.auto_add);
    }

    void visiablePanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelMid.setVisibility(View.VISIBLE);
        panelRight.setVisibility(View.VISIBLE);
    }

    void uploadPicPanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelMid.setVisibility(View.VISIBLE);
        panelRight.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelMid.setImageResource(R.drawable.shoot);
        panelRight.setImageResource(R.drawable.upload_sign);
    }

    void inPlayPanel() {
        panelLeft.setImageResource(R.drawable.camera);
        panelMid.setImageResource(R.drawable.stop);
        panelRight.setImageResource(R.drawable.add_sub);
    }

    void invoicePanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelMid.setImageResource(R.drawable.no);
        panelMid.setVisibility(View.VISIBLE);
        panelRight.setVisibility(View.VISIBLE);
        panelRight.setImageResource(R.drawable.invoice_check);
    }

    void finishPanel() {
        panelLeft.setVisibility(View.GONE);
        panelMid.setImageResource(R.drawable.close);
        panelRight.setVisibility(View.GONE);
    }

    void pausePanel() {
        panelLeft.setImageResource(R.drawable.camera);
        panelMid.setImageResource(R.drawable.next);
        panelRight.setImageResource(R.drawable.add_sub);
    }

    void startPanel() {
        panelMid.setImageResource(R.drawable.start);
        panelRight.setImageResource(R.drawable.signed);
        panelRight.setVisibility(View.VISIBLE);
    }

    void inspectionPanel() {
        panelMid.setImageResource(R.drawable.next);
        panelMid.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.camera);
        panelLeft.setVisibility(View.VISIBLE);
        panelRight.setImageResource(R.drawable.add_sub);
        panelRight.setVisibility(View.VISIBLE);
    }

    void leftBackPanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelMid.setImageResource(R.drawable.next);
        panelRight.setVisibility(View.INVISIBLE);
        panelMid.setVisibility(View.VISIBLE);
    }

    void pauseTimePanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelMid.setVisibility(View.VISIBLE);
        panelRight.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.camera);
        panelMid.setImageResource(R.drawable.goon);
        panelRight.setImageResource(R.drawable.add_sub);
    }

    void pauseToaddPanel() {
        panelLeft.setVisibility(View.VISIBLE);
        panelMid.setVisibility(View.VISIBLE);
        panelRight.setVisibility(View.VISIBLE);
        panelLeft.setImageResource(R.drawable.panel_back);
        panelRight.setImageResource(R.drawable.subtract);
        panelMid.setImageResource(R.drawable.add);
    }

    int isTermAccept = 0;
    int isChargeAccept = 0;

    void loadChooseSpinner() {
        spinnerList.clear();
        spinnerList.add("Agree");
        spinnerList.add("Disagree");
        chargeSp.attachDataSource(spinnerList);
        chargeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isChargeAccept = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        termSp.attachDataSource(spinnerList);
        termSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isTermAccept = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void loadGestureView() {
        //设置手势可多笔画绘制，默认情况为单笔画绘制
        gestureOverlayView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        gestureOverlayView.setGestureColor(Color.BLUE);
//        Logger.e("IIIIIIIIIIIIload"+gestureOverlayView.getGestureColor()+"");
        //设置还没未能形成手势绘制是的颜色(红色)
//        gestureOverlayView.setUncertainGestureColor(Color.RED);
        //设置手势的粗细
        gestureOverlayView.setGestureStrokeWidth(10);
        /*手势绘制完成后淡出屏幕的时间间隔，即绘制完手指离开屏幕后相隔多长时间手势从屏幕上消失；
         * 可以理解为手势绘制完成手指离开屏幕后到调用onGesturePerformed的时间间隔
         * 默认值为420毫秒，这里设置为2秒
         */
        gestureOverlayView.setFadeOffset(200000);
        gestureOverlayView.addOnGestureListener(this);
        gestureOverlayView.addOnGesturePerformedListener(this);
    }

    int isSignCharge = 0;
    int isSignTerm = 0;

    void loadSignSpinner() {
        spinnerList.clear();
        spinnerList.add("Agree");
        spinnerList.add("Disagree");
        sp_sign_charge.attachDataSource(spinnerList);
        sp_sign_charge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSignCharge = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_sign_terms.attachDataSource(spinnerList);
        sp_sign_terms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSignTerm = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position, ActiveJobRespBean respBean) {
        mJob = respBean.getJob();
        mActivity.hidePanel();
        switch (respBean.getPage().getPageNumber()) {
            case 1:
                fragmentStatus = JOB_DISPATCH_1;
                break;
            case 2:
                fragmentStatus = JOB_INSPECTION;
                break;
            case 3:
                fragmentStatus = JOB_IN_PLAY;
                break;
            case 4:
                fragmentStatus = JOB_FINISH_COLLECT;
                break;
        }
        mActivity.showRightMenu();
        loadView();
    }

    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {

    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {

    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

    }

    class SpinnerSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(int arg2, LoopView arg0) {
            switch (arg0.getId()) {
                case R.id.job_min_time:
                    if (spinnerList.size() == 0) return;//滑动过程中把dialog dismiss掉，spinnerlist就被置空了。
                    minTimeStr = spinnerList.get(arg2);
                    minTimeSp.setText(minTimeStr);
                    minTimeIndex = arg2;
                    break;
                case R.id.job_max_time:
                    if (spinnerList.size() == 0) return;
                    maxTimeStr = spinnerList.get(arg2);
                    maxTimeSp.setText(maxTimeStr);
                    maxTimeIndex = arg2;
                    break;
                case R.id.sp_terms:
                    Logger.e(spinnerList.get(arg2));
                    break;
                case R.id.sp_charge:
                    Logger.e(spinnerList.get(arg2));
                    break;
                case R.id.break_reason_sp:
                    switch (arg2) {
                        case PAUSE_REASON_BREAK:
                            pauseReason = PAUSE_REASON_BREAK;
                            break;
                        case PAUSE_REASON_FINISH:
                            pauseReason = PAUSE_REASON_FINISH;
                            break;
                        case PAUSE_REASON_CANCEL:
                            pauseReason = PAUSE_REASON_CANCEL;
                            break;
                    }
                    breakSp.setText(spinnerList.get(arg2));
                    break;
                case R.id.add_extra_name_sp:
                    extra_name_sp.setText(spinnerList.get(arg2));
                    if (null != extraChargeList && extraChargeList.size() > 0) {
                        extraChargeIndex = extraChargeList.get(arg2).getId();
                        add_extra_cost_sp.setText(extraChargeList.get(arg2).getCharge() + "");
                    }
                    break;
                case R.id.add_extra_quantity_sp:
                    extra_quantity_sp.setText(spinnerList.get(arg2));
                    extraQuantity = Integer.parseInt(spinnerList.get(arg2));
                    break;
                case R.id.sp_sign_charge:
                    Logger.e(spinnerList.get(arg2));
                    break;
                case R.id.sp_sign_terms:
                    Logger.e(spinnerList.get(arg2));
                    break;
                case R.id.finish_cash_sp:
                    payType = payMode.get(arg2).getPayModeId();
                    if (spinnerList.size() > 0){
                        finish_cash_sp.setText(spinnerList.get(arg2));
                    }
                    if (payType == PAY_BY_CARD) {
                        card_tips.setVisibility(View.VISIBLE);
                    } else {
                        card_tips.setVisibility(View.GONE);
                    }
                    break;

                case R.id.spinner_month:
                    if (spinnerList.size() != 0) {
                        month_sp.setText(spinnerList.get(arg2));
                    }
                    break;
                case R.id.spinner_year:
                    spinnerList.clear();
                    Calendar c = Calendar.getInstance();
                    int month = c.get(Calendar.MONTH) + 1;
                    if (yearList.size() != 0) {
                        month_right_sp.setText("/" + yearList.get(arg2));

                        if ((c.get(Calendar.YEAR)+"").equals(yearList.get(arg2))) {

                                for (int j = 0; j < 12; j++) {
                                    if ((month + j) < 13) {
                                        spinnerList.add(j, month + j + "");
                                    }
                                }

                        }else{
                            for (int j = 0; j < 12; j++) {
                                spinnerList.add(j, j +1+ "");
                            }
                        }
                    }
                    break;
                case R.id.card_sp:
                    card_sp_index = arg2;
                    card_sp.setText(spinnerList.get(arg2));
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (fragmentStatus) {
            case JOB_PRE_21:
                mActivity.hideTitle();
                loadView();
                break;
            case JOB_PAUSE_SIGN:
                mActivity.hideTitle();
                loadView();
                break;
        }
    }

    @Optional
    @OnClick({R.id.panel_left, R.id.panel_mid, R.id.panel_right,
            R.id.btn_back, R.id.next, R.id.job_min_time,
            R.id.job_max_time, R.id.break_reason_sp,
            R.id.add_extra_cost_sp, R.id.add_extra_name_sp, R.id.add_extra_quantity_sp,
            R.id.card_sp, R.id.ll_change_month, R.id.finish_cash_sp})
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.job_min_time:
                showMinTimeSpinnerDialog(view.getId());
                break;
            case R.id.job_max_time:
                showMaxTimeSpinnerDialog(view.getId());
                break;
            case R.id.break_reason_sp:
                showBreakReasonSpinnerDialog(view.getId());
                break;
            case R.id.add_extra_name_sp:
                showInspectionAddNameSpinnerDialog(view.getId());
                break;
            case R.id.add_extra_cost_sp:
                break;
            case R.id.add_extra_quantity_sp:
                showInspectionAddQulitySpinnerDialog(view.getId());
                break;
            case R.id.ll_change_month:
                showMonthSpinnerDialog(view.getId());
//                showYearSpinnerDialog(view.getId());
                break;
            case R.id.card_sp:
                showCardSpinnerDialog(view.getId());
                break;
            case R.id.finish_cash_sp:
                showFinishCardSpinnerDialog(view.getId());
                break;
            case R.id.next:
                gestureOverlayView.clear(false);
                isSignature = false;
                break;
            case R.id.btn_back:
                switch (fragmentStatus) {
                    case JOB_PRE_21:
                        if (!isSignature) {
                            Toast.makeText(mActivity, "You have not yet signed, please sign your name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        String path = ViewsUtils.captureScreen(view);
                        if (isTermAccept == 0 && isChargeAccept == 0) {
                            String path = ViewsUtils.captureScreen(view);
                            inspectionJob(path);
                        } else {
                            CustomDialog.Builder builder = new CustomDialog.Builder(mActivity, false)
                                    .setMessage("You haven't signed for the job select options, will you continue?")
                                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDialog.dismiss();
                                            termSp.setSelectedIndex(0);
                                            chargeSp.setSelectedIndex(0);
                                            termSp.setSelected(true);
                                            chargeSp.setSelected(true);
                                            String path = ViewsUtils.captureScreen(view);
                                            inspectionJob(path);
                                        }
                                    })
                                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDialog.dismiss();

                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.show();
                        }
                        break;
                    case JOB_PAUSE_SIGN:
                        if (!isSignature) {
                            Toast.makeText(mActivity, "You have not yet signed, please sign your name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (isSignCharge == 0 && isSignTerm == 0) {
                            String path1 = ViewsUtils.captureScreen(view);
                            inspectAndSign(path1);
                        } else {
                            CustomDialog.Builder builder = new CustomDialog.Builder(mActivity, false)
                                    .setMessage("You haven't signed for the job select options, will you continue?")
                                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDialog.dismiss();
                                            sp_sign_charge.setSelectedIndex(0);
                                            sp_sign_terms.setSelectedIndex(0);
                                            sp_sign_charge.setSelected(true);
                                            sp_sign_terms.setSelected(true);
                                            String path1 = ViewsUtils.captureScreen(view);
                                            inspectAndSign(path1);
                                        }
                                    })
                                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDialog.dismiss();
                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.show();
                        }
                        break;
                }
                break;
            case R.id.panel_left:
                onClickLeft();
                break;
            case R.id.panel_mid:
                onClickMid();
                break;
            case R.id.panel_right:
                onClickRight();
                break;
        }
    }

    void onClickLeft() {
        switch (fragmentStatus) {
            case JOB_DISPATCH_1:
                fragmentStatus = JOB_LIST;
                mActivity.setTv_title(R.string.job);
                mActivity.hideRightMenu();
                loadView();
                break;
            case JOB_DISPATCH_2:
                fragmentStatus = JOB_DISPATCH_1;
                job_dispatch_tv.setText(R.string.job_dispatch_1);
                break;
            case JOB_DISPATCH_3:
                fragmentStatus = JOB_DISPATCH_2;
                job_dispatch_tv.setText(R.string.job_dispatch_2);
                break;
            case JOB_INSPECTION:
                fragmentStatus = JOB_UPLOAD_PIC;
                tempFragmentStatus = JOB_INSPECTION;
                loadView();
                break;
            case JOB_INSPECTION_1:
                fragmentStatus = JOB_INSPECTION;
                loadView();
                break;
            case JOB_IN_PLAY:
                fragmentStatus = JOB_UPLOAD_PIC;
                tempFragmentStatus = JOB_IN_PLAY;
                loadView();
                break;
            case JOB_PAUSE_REASON:
                fragmentStatus = JOB_UPLOAD_PIC;
                tempFragmentStatus = JOB_PAUSE_REASON;
                loadView();
                break;
            case JOB_PAUSE_TIME:
                fragmentStatus = JOB_UPLOAD_PIC;
                tempFragmentStatus = JOB_PAUSE_TIME;
                loadView();
                break;
            case JOB_PAUSE_TOADD:
                fragmentStatus = tempFragmentStatus;
                loadView();
                break;
            case JOB_UPLOAD_PIC:
                fragmentStatus = tempFragmentStatus;
                loadView();
                break;
            case JOB_PRE_1:
                fragmentStatus = JOB_INSPECTION;
                loadView();
                break;
            case JOB_PRE_2:
                fragmentStatus = JOB_PRE_1;
                loadView();
                break;
            case JOB_FINISH_COLLECT:
                getPaymentAmt();
                break;
            case JOB_FINISH_PAYMENT_CASH:
                fragmentStatus = JOB_FINISH_COLLECT;
                loadView();
                break;
            case JOB_FINISH_INVOICE:
                fragmentStatus = JOB_FINISH_COLLECT;
                loadView();
                break;
            case JOB_FINISH_COLLECT_REST:
                fragmentStatus = JOB_FINISH_PAYMENT_CASH;
                loadView();
                break;
            case JOB_FINISH_CHANGE_CARD:
                fragmentStatus = JOB_FINISH_PAYMENT_CASH;
                loadView();
                break;
        }
    }

    void onClickMid() {
        switch (fragmentStatus) {
            case JOB_NO_ACTIVE:
                fragmentStatus = JOB_DISPATCH_1;
                loadView();
                break;
            case JOB_DISPATCH_1:
                fragmentStatus = JOB_DISPATCH_2;
                loadView();
                job_dispatch_tv.setText(R.string.job_dispatch_2);
                dispatchJob(1);
                break;
            case JOB_DISPATCH_2:
                fragmentStatus = JOB_DISPATCH_3;
                loadView();
                job_dispatch_tv.setText(R.string.job_dispatch_3);
                dispatchJob(2);
                break;
            case JOB_DISPATCH_3:
                fragmentStatus = JOB_INSPECTION;
                loadView();
                dispatchJob(3);
                break;
            case JOB_INSPECTION:
                fragmentStatus = JOB_PRE_1;
                loadView();
                break;
            case JOB_INSPECTION_1:
                addExtraCharges();
                break;
            case JOB_PRE_1:
                if (maxTimeIndex <= minTimeIndex) {
                    Toast.makeText(mActivity, "Wrong time selection", Toast.LENGTH_SHORT).show();
                    return;
                }
                fragmentStatus = JOB_PRE_2;
                loadView();
                break;
            case JOB_PRE_2:
                if (isPreSigned) {
                    startJob();
                } else {
                    CustomDialog.Builder builder = new CustomDialog.Builder(mActivity, false)
                            .setTitle("Notice")
                            .setMessage("You haven't signed for the job options,will you continue")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDialog.dismiss();
                                    fragmentStatus = JOB_PRE_21;
                                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                }
                            })
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDialog.dismiss();
                                }
                            });
                    mDialog = builder.create();
                    mDialog.show();
                }
                break;
            case JOB_IN_PLAY://job正在运行
                fragmentStatus = JOB_PAUSE_REASON;
                loadView();
                break;
            case JOB_PAUSE_TOADD://添加extracharge
                addExtraCharges();
                break;
            case JOB_UPLOAD_PIC://上传图片或者照片
                Intent intent = new Intent(mActivity, MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

                // 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

                startActivityForResult(intent, mActivity.REQUEST_IMAGE);
                break;
            case JOB_PAUSE_REASON://暂停job
                switch (pauseReason) {
                    case PAUSE_REASON_BREAK:
                        pauseJob();
                        break;
                    case PAUSE_REASON_FINISH:
                        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity, false)
                                .setMessage("It will going to payment. Do you want to sign your name first")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fragmentStatus = JOB_PAUSE_SIGN;
                                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                        mDialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        inspectAndSign();
                                        mDialog.dismiss();
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.show();
                        break;
                    case PAUSE_REASON_CANCEL:
                        fragmentStatus = JOB_IN_PLAY;
                        loadView();
                        break;
                }
                break;
            case JOB_PAUSE_TIME://重新继续job
                durationJob();
                break;
            case JOB_PRE_21:
                break;
            case JOB_FINISH_COLLECT://显示剩余未支付款项
//                if (totalMoney > 0) {
//                    CustomDialog.Builder builder = new CustomDialog.Builder(mActivity, false)
//                            .setTitle("Notice")
//                            .setMessage("Add Payment First")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mDialog.dismiss();
//                                }
//                            });
//                    mDialog = builder.create();
//                    mDialog.show();
//                } else {
                    fragmentStatus = JOB_FINISH_INVOICE;
                    loadView();
//                }
                break;
            case JOB_FINISH_PAYMENT_CASH://现金支付接口
                if (payType == PAY_BY_CASH) {
                    payCash();
                } else if (payType == PAY_BY_CARD) {
                    if (TextUtils.isEmpty(cash_no_et.getText().toString().trim()))
                        return;
                    cardMoney = Double.parseDouble(cash_no_et.getText().toString().trim());
                    fragmentStatus = JOB_FINISH_CHANGE_CARD;
                    loadView();
                } else if (payType == PAY_BY_LATER) {
                    fragmentStatus = JOB_FINISH_COLLECT_REST;
                    totalMoney = 0;
                    loadView();
                }
                payType = PAY_BY_CASH;
                break;
            case JOB_FINISH_INVOICE://是否要发票的接口
                isInvoice(0);
                break;
            case JOB_FINISH_COLLECT_REST://显示剩余未支付款项
//                if (totalMoney > 0) {
//                    AlertDialog dialog0 = new AlertDialog.Builder(mActivity)
//                            .setTitle("Notice")
//                            .setMessage("Add Payment Second")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .create();
//                    dialog0.show();
//                } else {
                    fragmentStatus = JOB_FINISH_INVOICE;
                    loadView();
//                }
                break;
            case JOB_FINISH_CHANGE_CARD://信用卡支付
                payCreditCard();
                break;
            case JOB_FINISH_COMPLETE://结束job
                finishJob();
                break;
        }
    }

    void onClickRight() {
        switch (fragmentStatus) {
            case JOB_PRE_2:
                if (isPreSigned) {
                    Toast.makeText(mActivity, "Already signed", Toast.LENGTH_SHORT).show();
                } else {
                    fragmentStatus = JOB_PRE_21;
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mActivity.showTitle();
                }
                break;
            case JOB_IN_PLAY:
                fragmentStatus = JOB_PAUSE_TOADD;
                tempFragmentStatus = JOB_IN_PLAY;
                loadView();
                break;
            case JOB_PAUSE_TIME:
                fragmentStatus = JOB_PAUSE_TOADD;
                tempFragmentStatus = JOB_PAUSE_TIME;
                loadView();
                break;
            case JOB_PAUSE_REASON:
                fragmentStatus = JOB_PAUSE_TOADD;
                tempFragmentStatus = JOB_PAUSE_REASON;
                loadView();
                break;
            case JOB_PAUSE_TOADD:
                subExtraCharges();
                break;
            case JOB_INSPECTION:
                fragmentStatus = JOB_INSPECTION_1;
                loadView();
                break;
            case JOB_INSPECTION_1:
                subExtraCharges();
                break;
            case JOB_UPLOAD_PIC:
                uploadAttachment();
                break;
            case JOB_FINISH_COLLECT:
                if (totalMoney != 0) {
                    fragmentStatus = JOB_FINISH_PAYMENT_CASH;
                    loadView();
                } else {
                    Toast.makeText(mActivity, "No need to pay anymore", Toast.LENGTH_SHORT).show();
                }
                break;
            case JOB_FINISH_INVOICE:
                isInvoice(1);
                break;
            case JOB_FINISH_PAYMENT_CASH:
                cash_no_et.setText(totalMoney + "");
                break;
            case JOB_FINISH_COLLECT_REST:
                Toast.makeText(mActivity, "No need to pay anymore", Toast.LENGTH_SHORT).show();
                break;
            case JOB_FINISH_CHANGE_CARD:
                EditText inputServer = new EditText(mActivity);
                inputServer.setFocusable(true);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                setRegion(inputServer);//设置输入范围
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Please Input the Percent of CreditCard Fee").setView(inputServer).setNegativeButton("No", null);
                builder.setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(inputServer.getText().toString())){
                                    surchargePercentage = Double.parseDouble(inputServer.getText().toString());
                                }
                                double temp = cardMoney * surchargePercentage / 100;
                                BigDecimal   b   =   new BigDecimal(temp);
                                temp = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                                pay_credit_amount.setText("$" + cardMoney);
                                pay_credit_surcharge.setText(surchargePercentage + "%");
                                pay_credit_surcharge_amount.setText("$" + temp);
                                pay_credit_total.setText("$" + (cardMoney + temp));
                            }
                        });
                builder.show();
                break;

        }
    }

    private void setRegion(EditText inputServer) {
        inputServer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        inputServer.setText(s);
                        inputServer.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    inputServer.setText(s);
                    inputServer.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        inputServer.setText(s.subSequence(0, 1));
                        inputServer.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal >MAX_MARK)
                        {
                            Toast.makeText(mActivity, "limit to 10", Toast.LENGTH_SHORT).show();
                            inputServer.setText(String.valueOf(MAX_MARK));
                        }
                        else if (markVal < MIN_MARK)
                        {
                            inputServer.setText(String.valueOf(MIN_MARK));
                        }
                        //将光标移至尾部
                        CharSequence text = inputServer.getText();
                        if (text instanceof Spannable)
                        {
                            Spannable spanText = (Spannable) text;
                            Selection.setSelection(spanText, text.length());
                        }
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mActivity.REQUEST_IMAGE) {
            if (data != null) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                attachmentPicPath = path;
                Glide.with(mActivity).load(new File(path.get(0))).into(attach_pic_iv);
            }
        }
    }

    public void showJobSheet() {
        fragmentStatus = JOB_SHEET;
        loadView();
    }

    void dispatchJob(int item) {
        RetrofitHttp.getInstance()
                .dispatchJob(mJob.getJobId(), App.app.getLoginUser().getUid(), item)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(respBean -> {
//                    Toast.makeText(mActivity, "dispatchJob success", Toast.LENGTH_SHORT).show();
                    if (respBean.getSkipMark() == 555) {
                        jumpStatus(respBean.getCurrentPage(), respBean.getRunDate(), respBean.getRunStatus());
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "dispatchJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("dispatchJob:" + throwable.getMessage());
                });
    }

    void startJob() {
        Calendar c = Calendar.getInstance();
        String s = c.get(Calendar.YEAR) + "-"
                + ((c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + "-"
                + (c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)) + " "
                + (c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)) + ":"
                + (c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)) + ":"
                + (c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : c.get(Calendar.SECOND));
        Subscription subscription = RetrofitHttp.getInstance()
                .startJob(mJob.getJobId())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(startJobRespbean -> {
//                    Toast.makeText(mActivity, "startJob success", Toast.LENGTH_SHORT).show();
                    if (startJobRespbean.getSkipMark() == 555) {
                        jumpStatus(startJobRespbean.getCurrentPage(), startJobRespbean.getRunDate(), startJobRespbean.getRunStatus());
                    } else {
                        fragmentStatus = JOB_IN_PLAY;
                        loadView();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "startJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("startJob:" + throwable.getMessage());
                });
    }

    void inspectionJob(String filePath) {
        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "signImage");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + "");
        RequestBody requestBody0 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), minTimeIndex + 1 + "");
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), maxTimeIndex + 1 + "");
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
        map.put("jobId", requestBody);
        map.put("estimateTimeFrom", requestBody0);
        map.put("estimateTimeTo", requestBody1);
        map.put("termsOfService", requestBody2);
        map.put("chargesAndCosts", requestBody3);
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectionJob(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectionJob -> {
//                    Toast.makeText(mActivity, "inspectionJob success", Toast.LENGTH_SHORT).show();
                    isPreSigned = true;
                    if (inspectionJob.getSkipMark() == 555) {
                        jumpStatus(inspectionJob.getCurrentPage(), inspectionJob.getRunDate(), inspectionJob.getRunStatus());
                    } else {
                        fragmentStatus = JOB_PRE_2;
                        loadView();
                    }
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.showTitle();
                    spinnerList.clear();
                }, throwable -> {
                    Toast.makeText(mActivity, "Upload Faild,please try again later! ", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectionJob:" + throwable.getMessage());
                });
    }

    int returnJobId;

    void pauseJob() {
        Subscription subscription = RetrofitHttp.getInstance()
                .pauseJob(mJob.getJobId(), 1)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(pauseJobRespbean -> {
//                    Toast.makeText(mActivity, "pauseJob success", Toast.LENGTH_SHORT).show();
                    if (pauseJobRespbean.getSkipMark() == 555) {
                        jumpStatus(pauseJobRespbean.getCurrentPage(), pauseJobRespbean.getRunDate(), pauseJobRespbean.getRunStatus());
                    } else {
                        returnJobId = pauseJobRespbean.getId();
                        fragmentStatus = JOB_PAUSE_TIME;
                        loadView();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "pauseJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("pauseJob:" + throwable.getMessage());
                });
    }

    void durationJob() {
        Subscription subscription = RetrofitHttp.getInstance()
                .durationJob(returnJobId, mJob.getJobId())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(durationJobRespbean -> {
//                    Toast.makeText(mActivity, "durationJob success", Toast.LENGTH_SHORT).show();
                    if (durationJobRespbean.getSkipMark() == 555) {
                        jumpStatus(durationJobRespbean.getCurrentPage(), durationJobRespbean.getRunDate(), durationJobRespbean.getRunStatus());
                    } else {
                        fragmentStatus = JOB_IN_PLAY;
                        loadView();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "durationJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("durationJob:" + throwable.getMessage());
                });
    }

    public BDLocationListener myListener = new MyLocationListener();

    void uploadAttachment() {
        if (null == attachmentPicPath || attachmentPicPath.size() == 0) {
            Toast.makeText(mActivity, "Please Add Image", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
            dialog.setMessage("Please Open GPS");
            dialog.setPositiveButton("OK",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                        }
                    });
            dialog.setNeutralButton("Cancle", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            mDialogUtilog.showLoading();
            ViewsUtils.preventViewMultipleClick(panelRight, 3000);
            App.app.mBaiduLocationClient.registerLocationListener(myListener);
            App.app.mBaiduLocationClient.start();

        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            App.app.mBaiduLocationClient.unRegisterLocationListener(myListener);
            App.app.mBaiduLocationClient.stop();
            double baiDuLongitude = location.getLongitude();
            double baiDuLatitude = location.getLatitude();
            if (baiDuLongitude != 0 && baiDuLatitude != 0) {
                new Thread("uploadAttachment") {
                    @Override
                    public void run() {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                                "{\"jobId\":" + mJob.getJobId() + ",\"jobType\":1,\"description\":\"photo\",\"location\":\""
                                        + baiDuLatitude + " " + baiDuLongitude+"\"}");
                        Map<String, RequestBody> requestFiles = MultipartUtil.getFilesBody(attachmentPicPath, "files");
                        Subscription subscription = RetrofitHttp.getInstance()
                                .uploadAttachment(requestBody, requestFiles)
                                .compose(RxResultHelper.handleResult())
                                .compose(RxResultHelper.applyIoSchedulers())
                                .subscribe(uploadAttachmentRespbean -> {
                                    Toast.makeText(mActivity, "Upload success", Toast.LENGTH_SHORT).show();
                                    mDialogUtilog.dismiss();
                                    attachmentPicPath.clear();
                                    fragmentStatus = tempFragmentStatus;
                                    loadView();
                                    Logger.e("uploadAttachment:" + uploadAttachmentRespbean.getMsg());
                                }, throwable -> {
                                    mDialogUtilog.dismiss();
                                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    Logger.e("uploadAttachment:" + throwable.getMessage());
                                });
                    }
                }.start();
            } else {
                mDialogUtilog.dismiss();
                Toast.makeText(getActivity(), "Failed to get information, please click on the upload button again!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    void getExtraCharge() {
        Subscription subscription = RetrofitHttp.getInstance()
                .getJobExtraCharge(mJob.getJobId())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getJobExtraChargeRespBean -> {
//                    Toast.makeText(mActivity, "getJobExtraChargeRespBean success", Toast.LENGTH_SHORT).show();
                    Logger.e("getJobExtraChargeRespBean:" + getJobExtraChargeRespBean.getMsg());
                    extraChargeList.clear();
                    for (int i = 0; i < getJobExtraChargeRespBean.getJobextrachargeDict().size(); i++) {
                        extraChargeList.add(getJobExtraChargeRespBean.getJobextrachargeDict().get(i));
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "getJobExtraChargeRespBean fail", Toast.LENGTH_SHORT).show();
                    Logger.e("getJobExtraChargeRespBean:" + throwable.getMessage());
                });
    }

    void addExtraCharges() {
        if (TextUtils.isEmpty(add_extra_cost_sp.getText().toString())) {
//            Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
            return;
        }
        //修改Quantity的默认值为1
        if ("x1".equals(extra_quantity_sp.getText().toString().trim())) {
            extraQuantity = 1;
        }
        Subscription subscription = RetrofitHttp.getInstance()
                .addExtraCharges(mJob.getJobId(), extraChargeIndex, Double.parseDouble(add_extra_cost_sp.getText().toString()), extraQuantity)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(addExtraChargeRespbean -> {
                    extraQuantity = 1;
                    Toast.makeText(mActivity, "Add success", Toast.LENGTH_SHORT).show();
                    Logger.e("addExtraChargeRespbean:" + addExtraChargeRespbean.getMsg());
                }, throwable -> {
                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.e("addExtraChargeRespbean:" + throwable.getMessage());
                });
    }

    void subExtraCharges() {
        if (TextUtils.isEmpty(add_extra_cost_sp.getText().toString())) {
            return;
        }
        Subscription subscription = RetrofitHttp.getInstance()
                .subExtraCharges(mJob.getJobId(), extraChargeIndex,Double.parseDouble(add_extra_cost_sp.getText().toString()) , extraQuantity)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(subExtraCharges -> {
                    Logger.e("subExtraCharges:" + subExtraCharges.getMsg());
                    extraQuantity = 1;
                    Toast.makeText(mActivity, "Sub success", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.e("subExtraCharges:" + throwable.getMessage());
                });
    }

    void inspectAndSign() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("jobId ",  RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + ""));
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectAndSign(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectAndSign -> {
//                    Toast.makeText(mActivity, "inspectAndSign success", Toast.LENGTH_SHORT).show();
                    if (inspectAndSign.getSkipMark() == 555) {
                        jumpStatus(inspectAndSign.getCurrentPage(), inspectAndSign.getRunDate(), inspectAndSign.getRunStatus());
                    } else {
//                        Toast.makeText(mActivity, "inspectAndSign fail", Toast.LENGTH_SHORT).show();
                        enterPaymentResetData();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "inspectAndSign fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectAndSign:" + throwable.getMessage());
                });
    }

    void inspectAndSign(String filePath) {
        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "signImage");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + "");
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (isSignTerm == 0 ? true:false) +"");
        RequestBody requestBody4= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (isSignCharge == 0 ? true:false) +"");
        map.put("jobId",requestBody);
        map.put("sustomerSigned",requestBody2);
        map.put("npDamages", requestBody3);
        map.put("chargesAndCosts", requestBody4);
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectAndSign(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectAndSign -> {
//                    Toast.makeText(mActivity, "inspectAndSign success", Toast.LENGTH_SHORT).show();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.showTitle();
                    if (inspectAndSign.getSkipMark() == 555) {
                        jumpStatus(inspectAndSign.getCurrentPage(), inspectAndSign.getRunDate(), inspectAndSign.getRunStatus());
                    } else {
                        enterPaymentResetData();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "inspectAndSign fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectAndSign:" + throwable.getMessage());
                });
    }

    void enterPaymentResetData() {
        timeCount = 0;
        minTimeStr = "";
        maxTimeStr = "";
        minTimeIndex = 0;
        maxTimeIndex = 0;
        isPreSigned = false;
        isInspectSigned = false;
        spinnerList.clear();
        handler.removeMessages(START_TIMER);
        fragmentStatus = JOB_FINISH_COLLECT;
        loadView();
    }

    void getPaymentAmt() {
        Subscription subscription = RetrofitHttp.getInstance()
                .getPayment(mJob.getJobId())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getPaymentAmt -> {
                    Logger.e("inspectAndSign:" + getPaymentAmt.getAmount());
                    totalMoney = getPaymentAmt.getAmount();
                    money_tv.setText("$" + totalMoney + "");
//                    Toast.makeText(mActivity, "getPaymentAmt success", Toast.LENGTH_SHORT).show();
                }, throwable -> {
//                    Toast.makeText(mActivity, "getPaymentAmt fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectAndSign:" + throwable.getMessage());
                });
    }

    void getPayCardType() {
        Subscription subscription = RetrofitHttp.getInstance()
                .getCreditCardType()
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getCreditCardType -> {
                    Logger.e("getCreditCardType:" + getCreditCardType);
                    cardType.clear();
                    for (CardType card : getCreditCardType.getCreditCardType()) {
                        cardType.add(card.getCardType());
                    }
//                    Toast.makeText(mActivity, "getCreditCardType success", Toast.LENGTH_SHORT).show();
                }, throwable -> {
//                    Toast.makeText(mActivity, "getCreditCardType fail", Toast.LENGTH_SHORT).show();
                    Logger.e("getCreditCardType:" + throwable.getMessage());
                });
    }

    GetPayModeRespBean payMode = new GetPayModeRespBean();

    void getPayMode() {
        Subscription subscription = RetrofitHttp.getInstance()
                .getPayMode()
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getPayMode -> {
                    Logger.e("getPayMode:" + getPayMode);
                    if (null != getPayMode){
                        payMode = getPayMode;
                    }
//                    Toast.makeText(mActivity, "getPayMode success", Toast.LENGTH_SHORT).show();
                }, throwable -> {
//                    Toast.makeText(mActivity, "getPayMode fail", Toast.LENGTH_SHORT).show();
                    Logger.e("getPayMode:" + throwable.getMessage());
                });
    }

    void payCash() {
        if (TextUtils.isEmpty(cash_no_et.getText().toString().trim())) {
            Toast.makeText(mActivity, "Please input cash number", Toast.LENGTH_SHORT).show();
            return;
        }
        String s = cash_no_et.getText().toString();
        cashMoney = Double.parseDouble(cash_no_et.getText().toString().trim());
        Subscription subscription = RetrofitHttp.getInstance()
                .payCash(mJob.getJobId(), 1, Double.parseDouble(s), totalMoney)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(payCash -> {
//                    Toast.makeText(mActivity, "payCash success", Toast.LENGTH_SHORT).show();
                    if (payCash.getSkipMark() == 555) {
                        jumpStatus(payCash.getCurrentPage(), payCash.getRunDate(), payCash.getRunStatus());
                    } else {
                        totalMoney = payCash.getPayment();
                        money_tv.setText(totalMoney + "");
                        if (totalMoney <= 0) {
                            fragmentStatus = JOB_FINISH_COLLECT;
                            loadView();
                        }
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "payCash fail", Toast.LENGTH_SHORT).show();
                });
    }

    void payCreditCard() {
        if ((creadit_card_et.getText().toString().trim().isEmpty())) {
            Toast.makeText(mActivity, "Please input the Card No.!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((creadit_card_et.getText().toString().trim().length() < 15) || (creadit_card_et.getText().toString().trim().length() > 25)) {
            Toast.makeText(mActivity, "Please Check the CreditCard No. is Available!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (job_cvv.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please input the CVV Code!", Toast.LENGTH_SHORT).show();
            return;
        }
        int length = job_cvv.getText().toString().trim().length();
        if (length <= 2) {
            Toast.makeText(mActivity, "Please Check the CVV Code is Available!", Toast.LENGTH_SHORT).show();
            return;
        }
        String year = month_right_sp.getText().toString().trim().substring(1);
        PayCreditCardReqBean reqBean = new PayCreditCardReqBean();
        reqBean.setJobId(mJob.getJobId());
        reqBean.setCardMonth(Integer.parseInt(month_sp.getText().toString().trim()));
        reqBean.setCardNo(creadit_card_et.getText().toString());
        reqBean.setCardType(card_sp_index);
        reqBean.setCardYear(Integer.parseInt(year));
        reqBean.setCardCvv(job_cvv.getText().toString().trim());
        reqBean.setPayment(cardMoney);
        reqBean.setPayType(2);
        TransactionFee fee = new TransactionFee();
        fee.setRate(surchargePercentage);
        reqBean.setTransactionFee(fee);
        Gson gson = new Gson();
        String s = gson.toJson(reqBean);
        Subscription subscription = RetrofitHttp.getInstance()
                .payCreditCard(s, totalMoney)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(payCreditCard -> {
//                    Toast.makeText(mActivity, "payCreditCard success", Toast.LENGTH_SHORT).show();
                    if (payCreditCard.getSkipMark() == 555) {
                        jumpStatus(payCreditCard.getCurrentPage(), payCreditCard.getRunDate(), payCreditCard.getRunStatus());
                    } else {
                        totalMoney = payCreditCard.getPayment();
                        fragmentStatus = JOB_FINISH_COLLECT;
                        loadView();
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "payCreditCard fail", Toast.LENGTH_SHORT).show();
                    Logger.e("payCreditCard:" + throwable.getMessage());
                });
    }

    void finishJob() {
        Subscription subscription = RetrofitHttp.getInstance()
                .finishJob(mJob.getJobId(), App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(finishJob -> {
//                    Toast.makeText(mActivity, "finishJob success", Toast.LENGTH_SHORT).show();
                    if (finishJob.getSkipMark() == 555) {
                        jumpStatus(finishJob.getCurrentPage(), finishJob.getRunDate(), finishJob.getRunStatus());
                    } else {
                        fragmentStatus = JOB_NO_ACTIVE;
                        loadView();
                        mActivity.hideRightMenu();
                        mActivity.hideLeftMenu();
                        mActivity.showPanel();
                        mActivity.setTv_title(R.string.job);
                        mActivity.showBadgeView(MainActivity.FRAGMENT_JOB, 0);
                    }
                }, throwable -> {
//                    Toast.makeText(mActivity, "finishJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("finishJob:" + throwable.getMessage());
                });
    }

    void isInvoice(int status) {
        Subscription subscription = RetrofitHttp.getInstance()
                .isInvoice(mJob.getJobId(), App.app.getLoginUser().getUid(), status)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(isInvoice -> {
//                    Logger.e("isInvoice:" + isInvoice.getMsg());
//                    Toast.makeText(mActivity, "isInvoice success", Toast.LENGTH_SHORT).show();
//                    if (isInvoice.getStatus().equals("114")) {
//                        Toast.makeText(mActivity, "You have not registered your mailbox!", Toast.LENGTH_SHORT).show();
//                    }
                    fragmentStatus = JOB_FINISH_COMPLETE;
                    loadView();
                }, throwable -> {
                    Logger.e("isInvoice:" + throwable.getMessage());
//                    Toast.makeText(mActivity, "isInvoice fail", Toast.LENGTH_SHORT).show();
                });
    }

    void jumpStatus(int page, String jobTime, int runStatus) {
        switch (page) {
            case 1:
                break;
            case 2:
                fragmentStatus = JOB_PRE_2;
                loadView();
                break;
            case 3:
                String time = jobTime;
                if (null != time) {
                    String temp = time.substring(0, time.indexOf(":"));
                    int hour = Integer.parseInt(temp);
                    temp = time.substring(time.indexOf(":") + 1, time.lastIndexOf(":"));
                    int minute = Integer.parseInt(temp);
                    temp = time.substring(time.lastIndexOf(":") + 1);
                    int second = Integer.parseInt(temp);
                    timeCount = hour * 3600 + minute * 60 + second;
                    if (runStatus == 0) {
                        if (!handler.hasMessages(JobFragment.START_TIMER)) {
                            handler.sendEmptyMessage(JobFragment.START_TIMER);
                        }
                        fragmentStatus = JOB_IN_PLAY;
                        loadView();
                    } else {
                        fragmentStatus = JOB_PAUSE_TIME;
                        loadView();
                    }
                }
                break;
            case 4:
                enterPaymentResetData();
                break;
            case 5:
                fragmentStatus = JOB_NO_ACTIVE;
                loadView();
                mActivity.hideRightMenu();
                mActivity.hideLeftMenu();
                mActivity.showPanel();
                mActivity.setTv_title(R.string.job);
                mActivity.showBadgeView(MainActivity.FRAGMENT_JOB, 0);
                break;
        }
    }

    class DismissListener implements DialogInterface.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            spinnerList.clear();
        }
    }

    void showFinishCardSpinnerDialog(int id) {
        spinnerList.clear();
        for (int i = 0; i < payMode.size(); i++) {
            spinnerList.add(payMode.get(i).getPayMode());
        }
        showSpinnerDialog(id);
    }

    void showMinTimeSpinnerDialog(int id) {
        for (int i = 0; i < 24; i++) {
            spinnerList.add(i, i + 1 + " Hours");
        }
        showSpinnerDialog(id);
    }

    void showMaxTimeSpinnerDialog(int id) {
        for (int i = 0; i < 24; i++) {
            spinnerList.add(i, i + 1 + " Hours");
        }
        showSpinnerDialog(id);
    }

    void showMonthSpinnerDialog(int id) {
        spinnerList.clear();
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        for (int j = 0; j < 12; j++) {
            if ((month + j) < 13) {
                spinnerList.add(j, month + j + "");
            }
        }
        for (int i = c.get(Calendar.YEAR); i < 2050; i++) {
            yearList.add(i + "");
        }
        showSpinnerDateDialog(id);
    }

/*    void showYearSpinnerDialog(int id){

        showSpinnerDialog(id);
    }*/

    void showCardSpinnerDialog(int id) {
        spinnerList.clear();
        spinnerList.addAll(cardType);
        showSpinnerDialog(id);
    }

    void showBreakReasonSpinnerDialog(int id) {
        spinnerList.add("Taking Break");
        spinnerList.add("Finish Job");
        spinnerList.add("Cancel");
        showSpinnerDialog(id);
    }

    void showInspectionAddNameSpinnerDialog(int id) {
        for (Jobextracharge s : extraChargeList) {
            spinnerList.add(s.getDescription());
        }
        showSpinnerDialog(id);
    }

    void showInspectionAddQulitySpinnerDialog(int id) {
        for (int i = 1; i < 100; i++) {
            spinnerList.add(i + "");
        }
        showSpinnerDialog(id);
    }

    void showSpinnerDialog(int id) {
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_spinner, null);
        LoopView loopView = (LoopView) dialogView.findViewById(R.id.spinner);
        loopView.setId(id);
        loopView.setNotLoop();
        loopView.setItems(spinnerList);
        loopView.setListener(new SpinnerSelectedListener());

        mSpinnerDialog = DialogCreater.createSpinnerDialog(mActivity, dialogView);
        mSpinnerDialog.setOnDismissListener(new DismissListener());
        mSpinnerDialog.show();
    }

    void showSpinnerDateDialog(int id) {
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_month_spinner, null);
        LoopView loopView = (LoopView) dialogView.findViewById(R.id.spinner_month);
        loopView.setId(R.id.spinner_month);
        loopView.setNotLoop();
        loopView.setItems(spinnerList);
        loopView.setListener(new SpinnerSelectedListener());
        LoopView loopYearView = (LoopView) dialogView.findViewById(R.id.spinner_year);
        loopYearView.setId(R.id.spinner_year);
        loopYearView.setNotLoop();
        loopYearView.setItems(yearList);
        loopYearView.setListener(new SpinnerSelectedListener());
        mSpinnerDialog = DialogCreater.createSpinnerDialog(mActivity, dialogView);
        mSpinnerDialog.setOnDismissListener(new DismissListener());
        mSpinnerDialog.show();
    }

}
