package cn.ssic.moverlogic.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
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
import android.widget.Toast;

import com.andview.refreshview.XWebView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.BrowseFileActivity;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.bean.GetJobSheetRespBean;
import cn.ssic.moverlogic.bean.GetRosterJobRespBean;
import cn.ssic.moverlogic.fragment.JobFragment;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.Pause;
import cn.ssic.moverlogic.net2request.RosterAttachItem;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.AlertDialog;
import cn.ssic.moverlogic.utils.SplitTimeUtils;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/14.
 */
public class JobSheetInfoScrollAdapter extends PagerAdapter {
    MainActivity mActivity;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Job mJob;
    JobFragment mJobFragment;
    int itemIds[] = {
            R.layout.fragment_roster_item0,
            R.layout.fragment_jobsheet_item1,
            R.layout.fragment_jobsheet_item2,
            R.layout.fragment_jobsheet_item3,
            R.layout.fragment_jobsheet_item4,
            R.layout.fragment_jobsheet_item5,
            R.layout.fragment_jobsheet_item6,
            R.layout.fragment_jobsheet_item7,
            R.layout.fragment_jobsheet_item8,
            R.layout.fragment_jobsheet_item9,
            R.layout.fragment_roster_item6,
            R.layout.fragment_jobsheet_item10,
            R.layout.fragment_jobsheet_item11,
    };
    GetJobSheetRespBean mGetJobSheetRespBean;

    public JobSheetInfoScrollAdapter(MainActivity c, Job job, JobFragment fragment) {
        mActivity = c;
        mJob = job;
        mJobFragment = fragment;
        loadJobInfo(mJob.getJobId());
    }

    private void loadJobInfo(int jobid) {
        Subscription subscription = RetrofitHttp.getInstance()
                .jobSheet(jobid, App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(jobSheet -> {
                    Logger.e(jobSheet.getMsg());
                    mGetJobSheetRespBean = jobSheet;
                    Toast.makeText(mActivity, "jobSheet success", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }, throwable -> {
                    Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.e(throwable.getMessage());
                });
    }

    void refreshItsself() {
        loadJobInfo(mJob.getJobId());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mActivity).inflate(itemIds[position], container, false);
        if (null == mGetJobSheetRespBean) {
            return view;
        }
        JobSheetBigHolder viewHolder = new JobSheetBigHolder(view, false);
//        viewHolder.item_refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//            public void onRefresh() {
//                refreshItsself();
//                viewHolder.item_refreshview.stopRefresh();
//            }
//        });

        switch (position) {
            case 0:
                viewHolder.contact.removeAllViews();
                viewHolder.ll1.removeAllViews();
                viewHolder.ll2.removeAllViews();
                GetRosterJobRespBean.ContactDetailsBean details = mGetJobSheetRespBean.getContactDetails();
                for (int i = 0; i < details.getContact().size(); i++) {
                    View inflate = View.inflate(mActivity, R.layout.contact, null);
                    TextView nameTv = (TextView) inflate.findViewById(R.id.name);
                    TextView mContact = (TextView) inflate.findViewById(R.id.roster_contact);
                    TextView email1Tv = (TextView) inflate.findViewById(R.id.email1);
                    TextView email2Tv = (TextView) inflate.findViewById(R.id.email2);
                    TextView phone1 = (TextView) inflate.findViewById(R.id.item_phone1);
                    TextView phone2 = (TextView) inflate.findViewById(R.id.itme_phone2);
                    mContact.setText("Contact "+(i+1));
                    nameTv.setText(details.getContact().get(i).getName() == null ? "" : details.getContact().get(i).getName());
                    email1Tv.setText(details.getContact().get(i).getEmail1() == null ? "" : details.getContact().get(i).getEmail1());
                    email2Tv.setText(details.getContact().get(i).getEmail2() == null ? "" : details.getContact().get(i).getEmail2());
                    phone1.setText(details.getContact().get(i).getPhone1() == null ? "" : details.getContact().get(i).getPhone1());
                    phone2.setText(details.getContact().get(i).getPhone2() == null ? "" : details.getContact().get(i).getPhone2());
                    phone1.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                    phone2.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                    int j =i;
                    phone1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog(mActivity).builder().setTitle(details.getContact().get(j).getPhone1())
                                    .setPositiveButton("call", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                                                        Manifest.permission.CALL_PHONE)) {
                                                    // 返回值：
                                                    //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                                                    //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                                                    //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                                                    // 弹窗需要解释为何需要该权限，再次请求授权
//                                                    Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                                                    // 帮跳转到该应用的设置界面，让用户手动授权
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                                    intent.setData(uri);
                                                    mActivity.startActivity(intent);
                                                }else{
                                                    // 不需要解释为何需要该权限，直接请求授权
                                                    ActivityCompat.requestPermissions(mActivity,
                                                            new String[]{Manifest.permission.CALL_PHONE},
                                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                                }
                                            }else {
                                                callPhone(details.getContact().get(j).getPhone1());
                                            }
                                        }
                                    }).setNegativeButton("cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        }
                    });
                    phone2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog(mActivity).builder().setTitle(details.getContact().get(j).getPhone2())
                                    .setPositiveButton("call", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                                                        Manifest.permission.CALL_PHONE)) {
                                                    // 返回值：
                                                    //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                                                    //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                                                    //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                                                    // 弹窗需要解释为何需要该权限，再次请求授权
//                                                    Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                                                    // 帮跳转到该应用的设置界面，让用户手动授权
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                                    intent.setData(uri);
                                                    mActivity.startActivity(intent);
                                                }else{
                                                    // 不需要解释为何需要该权限，直接请求授权
                                                    ActivityCompat.requestPermissions(mActivity,
                                                            new String[]{Manifest.permission.CALL_PHONE},
                                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                                }
                                            }else {
                                                callPhone(details.getContact().get(j).getPhone2());
                                            }
                                        }
                                    }).setNegativeButton("cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        }
                    });
                    viewHolder.contact.addView(inflate);
                }
                for (int i = 0; i < details.getCrew().size(); i++) {
                    View inflate1 = View.inflate(mActivity, R.layout.fragment_item0, null);
                    TextView key = (TextView) inflate1.findViewById(R.id.item0_key);
                    TextView value = (TextView) inflate1.findViewById(R.id.item0_value);
                    key.setText(details.getCrew().get(i).getName() == null ? "" : details.getCrew().get(i).getName());
                    value.setText(details.getCrew().get(i).getPhone() == null ? "" : details.getCrew().get(i).getPhone());
                    viewHolder.ll1.addView(inflate1);
                }

                View inflate = View.inflate(mActivity, R.layout.fragment_item0, null);
                TextView key = (TextView) inflate.findViewById(R.id.item0_key);
                TextView value1 = (TextView) inflate.findViewById(R.id.item0_value);
                key.setText(details.getCrewVehicle().getVehicleName() == null ? "" : details.getCrewVehicle().getVehicleName());
                value1.setText(details.getCrewVehicle().getVehicleType() == null ? "" : details.getCrewVehicle().getVehicleType());
                viewHolder.ll2.addView(inflate);
                break;
            case 1:
                viewHolder.name.setText(mGetJobSheetRespBean.getClientDetails().getName());
                viewHolder.phone1.setText(mGetJobSheetRespBean.getClientDetails().getPhone1());
                viewHolder.phone2.setText(mGetJobSheetRespBean.getClientDetails().getPhone2());

                viewHolder.phone1.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                viewHolder.phone2.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                viewHolder.phone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog(mActivity).builder().setTitle(mGetJobSheetRespBean.getClientDetails().getPhone1())
                                .setPositiveButton("call", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                                                    Manifest.permission.CALL_PHONE)) {
                                                // 返回值：
                                                //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                                                //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                                                //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                                                // 弹窗需要解释为何需要该权限，再次请求授权
//                                                    Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                                                // 帮跳转到该应用的设置界面，让用户手动授权
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                                intent.setData(uri);
                                                mActivity.startActivity(intent);
                                            }else{
                                                // 不需要解释为何需要该权限，直接请求授权
                                                ActivityCompat.requestPermissions(mActivity,
                                                        new String[]{Manifest.permission.CALL_PHONE},
                                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                            }
                                        }else {
                                            callPhone(mGetJobSheetRespBean.getClientDetails().getPhone1());
                                        }
                                    }
                                }).setNegativeButton("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                });

                viewHolder.phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog(mActivity).builder().setTitle(mGetJobSheetRespBean.getClientDetails().getPhone2())
                                .setPositiveButton("call", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                                                    Manifest.permission.CALL_PHONE)) {
                                                // 返回值：
                                                //                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                                                //                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                                                //                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                                                // 弹窗需要解释为何需要该权限，再次请求授权
//                                                    Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                                                // 帮跳转到该应用的设置界面，让用户手动授权
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                                intent.setData(uri);
                                                mActivity.startActivity(intent);
                                            }else{
                                                // 不需要解释为何需要该权限，直接请求授权
                                                ActivityCompat.requestPermissions(mActivity,
                                                        new String[]{Manifest.permission.CALL_PHONE},
                                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                            }
                                        }else {
                                            callPhone(mGetJobSheetRespBean.getClientDetails().getPhone2());
                                        }
                                    }
                                }).setNegativeButton("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                });
                break;
            case 2:
                viewHolder.address_container.removeAllViews();
                if (null == mGetJobSheetRespBean.getAddressDetails() || mGetJobSheetRespBean.getAddressDetails().size() == 0) {
                } else {
                    for (int i = 0; i < mGetJobSheetRespBean.getAddressDetails().size(); i++) {
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_inner_items, null);
                        LinearLayout llAddress = (LinearLayout) ll.findViewById(R.id.ll_address);
                        LinearLayout llNotes = (LinearLayout) ll.findViewById(R.id.ll_address_notes);
                        String title = "Address " + (i + 1) + ":" + (1 == mGetJobSheetRespBean.getAddressDetails().get(i).getAddressType() ? "PICK-UP" : "DROP-OFF");
                        TextView address1_text_clickable = (TextView) ll.findViewById(R.id.address1_text_clickable);
                        address1_text_clickable.setText(title);
                        TextView address = (TextView) ll.findViewById(R.id.address);
                        String s = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getAddress())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getAddress();
                        String street = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getStreet())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getStreet();
                        String suburb = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getSuburb())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getSuburb();
                        String state = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getState())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getState();
                        String postCode = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getPostCode())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getPostCode();
                        String country = TextUtils.isEmpty(mGetJobSheetRespBean.getAddressDetails().get(i).getCountry())? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getCountry();
                        address.setText(s + "\n" + street + "\n" + suburb + " " + state + "\n" + postCode + "," + country + "");

                        TextView property = (TextView) ll.findViewById(R.id.address_property);
                        TextView levels = (TextView) ll.findViewById(R.id.address_levels);
                        TextView floor = (TextView) ll.findViewById(R.id.address_floor);
                        TextView access = (TextView) ll.findViewById(R.id.address_access);

                        property.setText(mGetJobSheetRespBean.getAddressDetails().get(i).getProperty()==null ? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getProperty());
                        levels.setText(mGetJobSheetRespBean.getAddressDetails().get(i).getLevels()==null ? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getLevels());
                        floor.setText(mGetJobSheetRespBean.getAddressDetails().get(i).getFloor()+"");
                        access.setText(mGetJobSheetRespBean.getAddressDetails().get(i).getAccess()==null ? "" : mGetJobSheetRespBean.getAddressDetails().get(i).getAccess());

                        for (int j = 0; j <mGetJobSheetRespBean.getAddressDetails().get(i).getJobAddressNotes().size(); j++) {
                            LinearLayout ll1 = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_notes, null);
                            TextView tvNotes = (TextView) ll1.findViewById(R.id.tv_notes_key);
                            TextView tvNotesValue = (TextView) ll1.findViewById(R.id.tv_notes_value);
                            tvNotes.setText("Notes "+(j+1));
                            tvNotesValue.setText(mGetJobSheetRespBean.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes()==null ? ""
                                    : mGetJobSheetRespBean.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes());
                            llNotes.addView(ll1);
                        }
                        address1_text_clickable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                llAddress.setVisibility(View.GONE == llAddress.getVisibility() ? View.VISIBLE : View.GONE);
                            }
                        });
                        viewHolder.address_container.addView(ll);
                    }
                }
                break;
            case 3:
                if (null == mGetJobSheetRespBean.getVehicle()) {

                } else {
                    if (null != mGetJobSheetRespBean.getVehicle().getVehicleDetails()) {
                        viewHolder.truck_type.setText(mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckType());
                        viewHolder.truck_capacity.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckCapacity()) ? "" :
                                mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckCapacity() + "CuM");
                        viewHolder.truck_gvm.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckGvm()) ? "" :
                                mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckGvm() + "Tonne");
                        viewHolder.truck_tm.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckTm()) ? "" :
                                mGetJobSheetRespBean.getVehicle().getVehicleDetails().getTruckTm() + "Tonne");
                        viewHolder.truck_men.setText(mGetJobSheetRespBean.getVehicle().getVehicleDetails().getMen() + "");
                    }
                }

                if (null == mGetJobSheetRespBean.getVehicle().getEquipment()
                        || mGetJobSheetRespBean.getVehicle().getEquipment().size() == 0) {
                } else {
                    String no = "";
                    String value = "";
                    for (int i = 0; i < mGetJobSheetRespBean.getVehicle().getEquipment().size(); i++) {
                        no += TextUtils.isEmpty(mGetJobSheetRespBean.getVehicle().getEquipment().get(i).getEquipmentQuantity()) ? ""+ "\n" :
                                mGetJobSheetRespBean.getVehicle().getEquipment().get(i).getEquipmentQuantity() + "\n";
                        value += TextUtils.isEmpty(mGetJobSheetRespBean.getVehicle().getEquipment().get(i).getEquipment()) ? ""+ "\n" :
                                mGetJobSheetRespBean.getVehicle().getEquipment().get(i).getEquipment() + "\n";
                    }
                    viewHolder.truck_equipment_1.setText(no);
                    viewHolder.truck_equipment_name.setText(value);
                }

                break;
            case 4:
                if (null == mGetJobSheetRespBean.getJobDetails()) {
                } else {
                    if (null != mGetJobSheetRespBean.getJobDetails().getTimedate()) {
                        viewHolder.job_date.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getJobDetails().getTimedate().getDate())? "" :
                                SplitTimeUtils.splitDate(mGetJobSheetRespBean.getJobDetails().getTimedate().getDate()));
                        viewHolder.starttime.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getJobDetails().getTimedate().getStarttime()) ? "" :
                                mGetJobSheetRespBean.getJobDetails().getTimedate().getStarttime());
                    }
                }

                if (null != mGetJobSheetRespBean.getJobDetails().getJobtype()) {
                    viewHolder.job_type.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getJobDetails().getJobtype().getType()) ? "" :
                            mGetJobSheetRespBean.getJobDetails().getJobtype().getType());
                    viewHolder.job_movesize.setText(mGetJobSheetRespBean.getJobDetails().getJobtype().getMovesize() + "CuM");
                    viewHolder.job_est1.setText(TextUtils.isEmpty(mGetJobSheetRespBean.getJobDetails().getJobtype().getEstime().getEstTime1()) ? "" :
                            mGetJobSheetRespBean.getJobDetails().getJobtype().getEstime().getEstTime1());
                    viewHolder.job_est2.setText(mGetJobSheetRespBean.getJobDetails().getJobtype().getEstime().getEstTime2() == null ? "" :
                            mGetJobSheetRespBean.getJobDetails().getJobtype().getEstime().getEstTime2());
                }
                break;
            case 5:
                if (null == mGetJobSheetRespBean.getJobCharges()) {
                } else {
                    if (null != mGetJobSheetRespBean.getJobCharges().getVehicleCharges()) {
                        viewHolder.job_rate_type.setText(mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getRatetype() ==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getRatetype());
                        viewHolder.rate.setText("$" + mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getRate());
                        viewHolder.call_out_fee.setText(mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getCalloutfee() == null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getCalloutfee());
                        viewHolder.return_fee.setText(mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getReturnfee()==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getVehicleCharges().getReturnfee());
                    }
                }

                if (null == mGetJobSheetRespBean.getJobCharges().getExtraCharges()
                        || mGetJobSheetRespBean.getJobCharges().getExtraCharges().size() == 0) {
                } else {
                    String name = "";
                    String value = "";
                    for (int i = 0; i < mGetJobSheetRespBean.getJobCharges().getExtraCharges().size(); i++) {
                        name += mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeName()==null ? ""
                                + "\n" : mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeName()+ "\n";
                        String extraValue = mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeValue() ==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeValue();
                        String extraQuality = mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeQuantity()==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getExtraCharges().get(i).getExtraChargeQuantity();
                        value +=(extraValue+extraQuality)+ "\n";
                    }
                    viewHolder.extra_charge_name0.setText(name);
                    viewHolder.extra_charge_value0.setText(value);

                }

                if (null != mGetJobSheetRespBean.getJobCharges().getTimeCharges()) {
                    viewHolder.job_min_time.setText(mGetJobSheetRespBean.getJobCharges().getTimeCharges().getMinimumTime() ==null ? "" :
                            mGetJobSheetRespBean.getJobCharges().getTimeCharges().getMinimumTime());
                    viewHolder.time_increments.setText(mGetJobSheetRespBean.getJobCharges().getTimeCharges().getTimeincrements()== null ? "" :
                            mGetJobSheetRespBean.getJobCharges().getTimeCharges().getTimeincrements());
                }

                if (null == mGetJobSheetRespBean.getJobCharges().getDiscounts()
                        || mGetJobSheetRespBean.getJobCharges().getDiscounts().size() == 0) {
                } else {
                    String name = "";
                    String value = "";
                    for (int i = 0; i < mGetJobSheetRespBean.getJobCharges().getDiscounts().size(); i++) {
                        name += mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountName()==null ? ""
                                + "\n" : mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountName()+ "\n";

                        String extraValue = mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountRate() ==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountRate();
                        String extraQuality = mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountQuantity()==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getDiscounts().get(i).getDiscountQuantity();
                        value +=(extraValue+extraQuality)+ "\n";
                    }
                    viewHolder.dicount_name.setText(name);
                    viewHolder.dicount_value.setText(value);

                }
               /* if (null != mGetJobSheetRespBean.getJobCharges().getTaxes()) {
                    viewHolder.gst.setText(mGetJobSheetRespBean.getJobCharges().getTaxes().getGst() + "");
                }*/

                if (null == mGetJobSheetRespBean.getJobCharges().getTransactionFee()) {
                    break;
                } else {
                    String name = "";
                    String value = "";
                    for (int i = 0; i <mGetJobSheetRespBean.getJobCharges().getTransactionFee().size() ; i++) {
                        name += TextUtils.isEmpty(mGetJobSheetRespBean.getJobCharges().getTransactionFee().get(i).getTransactionFeeName()) ? ""
                                + "\n" : mGetJobSheetRespBean.getJobCharges().getTransactionFee().get(i).getTransactionFeeName() + "\n";
                        String extraValue = mGetJobSheetRespBean.getJobCharges().getTransactionFee().get(i).getTransactionFeeValue() ==null ? "" :
                                mGetJobSheetRespBean.getJobCharges().getTransactionFee().get(i).getTransactionFeeValue();
                        value +=extraValue+ "\n";
                    }
                    viewHolder.action_name.setText(name);
                    viewHolder.action_value.setText(value);
                }
                break;
            case 6:
                viewHolder.items_container.removeAllViews();
                if (null == mGetJobSheetRespBean.getAttachedItems()
                        || mGetJobSheetRespBean.getAttachedItems().size() == 0) {
                } else {
                    for (int i = 0; i < mGetJobSheetRespBean.getAttachedItems().size(); i++) {
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_roster_attachitems, null);
                        TextView tv = (TextView) ll.findViewById(R.id.attachitems_name);

                        if (mGetJobSheetRespBean.getAttachedItems().get(i).getFileType() == 1) {
                            tv.setText("Photo:");
                        } else {
                            tv.setText("File:");
                        }

                        TextView tv1 = (TextView) ll.findViewById(R.id.file_date);
                        tv1.setText(mGetJobSheetRespBean.getAttachedItems().get(i).getDate());
                        RosterAttachItem item = mGetJobSheetRespBean.getAttachedItems().get(i);
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventBus.getDefault().postSticky(item);//post给MainActivity的onEventLogin方法
                                mActivity.startActivity(new Intent(mActivity, BrowseFileActivity.class));
                            }
                        });
                        viewHolder.items_container.addView(ll);
                    }
                    viewHolder.attachitems_name.setText("Attached Items" + "(" + mGetJobSheetRespBean.getAttachedItems().size() + ")");
                }
                break;
            case 7:
                viewHolder.inventory_container.removeAllViews();
                if (null == mGetJobSheetRespBean.getTotalInventory()
                        || mGetJobSheetRespBean.getTotalInventory().size() == 0) {
                    viewHolder.tv_inventory.setText("Total Inventory" + "(" + 0 + " CuM" + ")");
                    viewHolder.tv_inventory.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                } else {
                    double total = 0;
                    for (int i = 0; i < mGetJobSheetRespBean.getTotalInventory().size(); i++) {
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_roster_inventorys, null);
                        TextView tv = (TextView) ll.findViewById(R.id.inventory_name);
                        tv.setText(mGetJobSheetRespBean.getTotalInventory().get(i).getInventoryName() + "(" + mGetJobSheetRespBean.getTotalInventory().get(i).getQuantity() + ")");
                        tv.setTextAppearance(mActivity, R.style.JobTagTextStyle);
                        TextView tv1 = (TextView) ll.findViewById(R.id.inventory_no);
                        tv1.setText((mGetJobSheetRespBean.getTotalInventory().get(i).getVolume() == 0) ? "" : mGetJobSheetRespBean.getTotalInventory().get(i).getVolume() + "CuM");
//                        tv1.setText(mGetJobSheetRespBean.getTotalInventory().get(i).getVolume() + "CuM");
                        viewHolder.inventory_container.addView(ll);
//                        if (!TextUtils.isEmpty(mGetJobSheetRespBean.getTotalInventory().get(i).getVolume())) {
//                            double item = Double.parseDouble(mGetJobSheetRespBean.getTotalInventory().get(i).getVolume());
//                            total += item;
//                        }
                            total += mGetJobSheetRespBean.getTotalInventory().get(i).getVolume();
                    }
                    BigDecimal b = new BigDecimal(total);//保留两位小数
                    BigDecimal  newTotal = b.setScale(2,BigDecimal.ROUND_DOWN);
                    viewHolder.tv_inventory.setText("Total Inventory" + "(" + newTotal + " CuM" + ")");
                    viewHolder.tv_inventory.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                }
                break;
            case 8:
                if (null == mGetJobSheetRespBean.getTimeSheet()) {

                } else {
                    if (null != mGetJobSheetRespBean.getTimeSheet().getStartTimes()) {
                        viewHolder.dispatch_time.setText(SplitTimeUtils.splitTime(mGetJobSheetRespBean.getTimeSheet().getStartTimes().getDispatchTime()));

                        viewHolder.arrival_time.setText(SplitTimeUtils.splitTime(mGetJobSheetRespBean.getTimeSheet().getStartTimes().getArrivalTime()));
                        viewHolder.timesheet_starttime.setText(SplitTimeUtils.splitTime(mGetJobSheetRespBean.getTimeSheet().getStartTimes().getStartTime()));
                    }
                }

                if (null == mGetJobSheetRespBean.getTimeSheet().getPause()
                        || mGetJobSheetRespBean.getTimeSheet().getPause().size() == 0) {
                } else {
                    String name = "";
                    String value = "";
                    for (Pause pause : mGetJobSheetRespBean.getTimeSheet().getPause()) {
                        String s = pause.getHour()+":"+pause.getMin()+":"+pause.getS();
                        name += "Break:" + "\n";
//                        value += pause.getHour() + "Hr(s)" + pause.getMin() + "Min(s) " + pause.getS() + "Sec(s)\n";
                        value += SplitTimeUtils.splitTotal(s)+"\n";
                    }
                    viewHolder.break_name.setText(name);
                    viewHolder.break_time.setText(value);
                }
                if (null == mGetJobSheetRespBean.getTimeSheet().getFinishTime().getFinishTime()
                        || "null".equals(mGetJobSheetRespBean.getTimeSheet().getFinishTime().getFinishTime())) {
                } else {
                    viewHolder.finish_time.setText(SplitTimeUtils.splitTime(mGetJobSheetRespBean.getTimeSheet().getFinishTime().getFinishTime()));
                }
                if (null == mGetJobSheetRespBean.getTimeSheet().getTotalChargeTime()
                        || "0:0:0".equals(mGetJobSheetRespBean.getTimeSheet().getTotalChargeTime().getTotalChargeTime())) {
                } else {
                    String total= SplitTimeUtils.splitTotal(mGetJobSheetRespBean.getTimeSheet().getTotalChargeTime().getTotalChargeTime());
                    viewHolder.total_time.setText( total+ "");
                }
                break;
            case 9:
                if (null != mGetJobSheetRespBean.getRunningTotal()) {
                    viewHolder.vehicle_charge.setText(mGetJobSheetRespBean.getRunningTotal().getTotalVehicleCharges() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getTotalVehicleCharges());
                    viewHolder.extra_charge.setText(mGetJobSheetRespBean.getRunningTotal().getTotalExtraCharges() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getTotalExtraCharges());
                    viewHolder.total_discount.setText(mGetJobSheetRespBean.getRunningTotal().getTotalDiscounts() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getTotalDiscounts());
                    viewHolder.sub_total.setText(mGetJobSheetRespBean.getRunningTotal().getSubTotal() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getSubTotal());
                    viewHolder.tax.setText(mGetJobSheetRespBean.getRunningTotal().getTax() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getTax());
                    viewHolder.total.setText(mGetJobSheetRespBean.getRunningTotal().getTotal() == null ? "" : mGetJobSheetRespBean.getRunningTotal().getTotal());
                }
                break;
            case 10:
                if (null == mGetJobSheetRespBean.getPayment()) {
                    break;
                }
                viewHolder.llPayment.removeAllViews();
                for (int i = 0; i <mGetJobSheetRespBean.getPayment().size() ; i++) {
                    LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.layout_payment, null);
                    TextView tvMoney = (TextView) ll.findViewById(R.id.tv_payment_money);
                    TextView tvCash = (TextView) ll.findViewById(R.id.tv_payment_cash);
                    TextView tvDate = (TextView) ll.findViewById(R.id.tv_payment_date);
                    tvMoney.setText((i+1)+". $"+mGetJobSheetRespBean.getPayment().get(i).getPayment());
                    tvCash.setText(mGetJobSheetRespBean.getPayment().get(i).getPayType()==null ? "" :  mGetJobSheetRespBean.getPayment().get(i).getPayType());
                    tvDate.setText(mGetJobSheetRespBean.getPayment().get(i).getPayDate() == null ? "" : mGetJobSheetRespBean.getPayment().get(i).getPayDate());

                    viewHolder.llPayment.addView(ll);
                }
                break;
            case 11:
                if (null != mGetJobSheetRespBean.getJobNotes()) {
                    viewHolder.general_notes.setText(mGetJobSheetRespBean.getJobNotes().getGeneralnotes() == null ? "" : mGetJobSheetRespBean.getJobNotes().getGeneralnotes());
                    viewHolder.forman_notes.setText(mGetJobSheetRespBean.getJobNotes().getForemannotes() == null ? "" : mGetJobSheetRespBean.getJobNotes().getForemannotes());
                    viewHolder.forman_notes_title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.forman_notes.setVisibility(viewHolder.forman_notes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                    });
                }
                break;
            case 12:
                setWeb(viewHolder.mWebview, viewHolder,mGetJobSheetRespBean.getTerms());
//                viewHolder.terms.setText(R.string.term_content);
      /*          viewHolder.pdfView.fromAsset("terms_of_services.pdf")
                        .defaultPage(1)
//                        .onPageChange((OnPageChangeListener) mJobFragment.getActivity())
//                        .enableAnnotationRendering(true)
                        .enableSwipe(true)   //是否允许翻页，默认是允许翻
                        .swipeVertical(true)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
//                        .onLoad((OnLoadCompleteListener) mJobFragment.getActivity())
                        .load();*/
                break;
        }
        container.addView(view, 0);
        return view;
    }

    private void setWeb(XWebView mWebview,  JobSheetBigHolder holder, String terms) {
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
        JobSheetBigHolder infoItem0Holder;

        public NewWebViewClient(JobSheetBigHolder holder) {
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
    private void callPhone(String phone) {
    /*    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        mActivity.startActivity(intent);*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" +phone));
        //开启系统拨号器
        mActivity.startActivity(intent);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return itemIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals((View) object);
    }
}
