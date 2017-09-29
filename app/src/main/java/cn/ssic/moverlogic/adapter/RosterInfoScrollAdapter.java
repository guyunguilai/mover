package cn.ssic.moverlogic.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import cn.ssic.moverlogic.App;
import cn.ssic.moverlogic.BrowseFileActivity;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.bean.GetRosterJobRespBean;
import cn.ssic.moverlogic.fragment.RosterFragment;
import cn.ssic.moverlogic.mvpview.LoginActivity;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.RosterAttachItem;
import cn.ssic.moverlogic.netokhttp3.RetrofitHttp;
import cn.ssic.moverlogic.netokhttp3.RxResultHelper;
import cn.ssic.moverlogic.utils.AlertDialog;
import cn.ssic.moverlogic.utils.SplitTimeUtils;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RosterInfoScrollAdapter extends PagerAdapter {
    GetRosterJobRespBean mJobDetail;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    Job mJob;
    MainActivity mActivity;
    int itemIds[] = {
            R.layout.fragment_roster_item0,
            R.layout.fragment_roster_item1,
            R.layout.fragment_roster_item2,
            R.layout.fragment_roster_item3,
            R.layout.fragment_roster_item4,
            R.layout.fragment_roster_item5,
            R.layout.fragment_roster_item6,
            R.layout.fragment_roster_item7,
            R.layout.fragment_roster_item8,
            R.layout.fragment_roster_item9,
    };

    public RosterInfoScrollAdapter(MainActivity c, Job job, RosterFragment fragment) {
        mActivity = c;
        mJob = job;
        loadJobInfo(job.getJobId());
    }

    private void loadJobInfo(int jobid) {
        Subscription subscription = RetrofitHttp.getInstance()
                .getRosterJob(jobid, App.app.getLoginUser().getUid())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(rosterJobRespBean -> {
//                    Logger.e(rosterJobRespBean.getMsg());
//                    Toast.makeText(mActivity, "getRosterJob success", Toast.LENGTH_SHORT).show();
                    refreshList(rosterJobRespBean);
                }, throwable -> {
//                    Toast.makeText(mActivity, "getRosterJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e(throwable.getMessage());
                });
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mActivity).inflate(itemIds[position], container, false);
        if (null == mJobDetail) {
            return view;
        }
        JobSheetBigHolder rosterHolder = new JobSheetBigHolder(view, false);
//        rosterHolder.item_refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//            public void onRefresh() {
//                refreshItsself();
//                rosterHolder.item_refreshview.stopRefresh();
//            }
//        });

        switch (position) {
            case 0:
                rosterHolder.contact.removeAllViews();
                rosterHolder.ll1.removeAllViews();
                rosterHolder.ll2.removeAllViews();
                GetRosterJobRespBean.ContactDetailsBean details = mJobDetail.getContactDetails();
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
                    String phone = details.getContact().get(j).getPhone1();
                    phone1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog(mActivity).builder().setTitle(phone)
                                    .setPositiveButton("call", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+details.getContact().get(j).getPhone1()));
                                            // 检查是否获得了权限（Android6.0运行时权限）
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
                                                callPhone(phone);
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
                    rosterHolder.contact.addView(inflate);
                }
                for (int i = 0; i < details.getCrew().size(); i++) {
                    View inflate1 = View.inflate(mActivity, R.layout.fragment_item0, null);
                    TextView key = (TextView) inflate1.findViewById(R.id.item0_key);
                    TextView value = (TextView) inflate1.findViewById(R.id.item0_value);
                    key.setText(details.getCrew().get(i).getName() == null ? "" : details.getCrew().get(i).getName());
                    value.setText(details.getCrew().get(i).getPhone() == null ? "" : details.getCrew().get(i).getPhone());
                    rosterHolder.ll1.addView(inflate1);
                }

                View inflate = View.inflate(mActivity, R.layout.fragment_item0, null);
                TextView key = (TextView) inflate.findViewById(R.id.item0_key);
                TextView value1 = (TextView) inflate.findViewById(R.id.item0_value);
                key.setText(details.getCrewVehicle().getVehicleName() == null ? "" : details.getCrewVehicle().getVehicleName());
                value1.setText(details.getCrewVehicle().getVehicleType() == null ? "" : details.getCrewVehicle().getVehicleType());
                rosterHolder.ll2.addView(inflate);

                break;
            case 1:
                if (null == mJobDetail.getClientdetail()) {
                    break;
                }
                rosterHolder.name.setText(mJobDetail.getClientdetail().getName() == null ? "" : mJobDetail.getClientdetail().getName());
                rosterHolder.phone1.setText(mJobDetail.getClientdetail().getPhone1() == null ? "" : mJobDetail.getClientdetail().getPhone1());
                rosterHolder.phone2.setText(mJobDetail.getClientdetail().getPhone2() == null ? "" : mJobDetail.getClientdetail().getPhone2());
                rosterHolder.phone1.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                rosterHolder.phone2.setTextColor(mActivity.getResources().getColor(R.color.phone_color));
                rosterHolder.phone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog(mActivity).builder().setTitle(mJobDetail.getClientdetail().getPhone1())
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
                                            callPhone(mJobDetail.getClientdetail().getPhone1());
                                        }
                                    }
                                }).setNegativeButton("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    }
                });

                rosterHolder.phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog(mActivity).builder().setTitle(mJobDetail.getClientdetail().getPhone2())
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
                                            callPhone(mJobDetail.getClientdetail().getPhone2());
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
                if (null == mJobDetail.getAddressDetails() || mJobDetail.getAddressDetails().size() == 0) {
                }else {
                    for (int i = 0; i < mJobDetail.getAddressDetails().size(); i++) {
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_inner_items, null);
                        LinearLayout llAddress = (LinearLayout) ll.findViewById(R.id.ll_address);
                        LinearLayout llNotes = (LinearLayout) ll.findViewById(R.id.ll_address_notes);
                        String title = "Address " + (i + 1) + ":" + (mJobDetail.getAddressDetails().get(i).getAddressType() ==1  ? "PICK-UP" : "DROP-OFF");
                        TextView address1_text_clickable = (TextView) ll.findViewById(R.id.address1_text_clickable);
                        address1_text_clickable.setText(title);
                        TextView address = (TextView) ll.findViewById(R.id.address);
                        String s = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getAddress())? "" : mJobDetail.getAddressDetails().get(i).getAddress();
                        String street = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getStreet())? "" : mJobDetail.getAddressDetails().get(i).getStreet();
                        String suburb = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getSuburb())? "" : mJobDetail.getAddressDetails().get(i).getSuburb();
                        String state = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getState())? "" : mJobDetail.getAddressDetails().get(i).getState();
                        String postCode = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getPostCode())? "" : mJobDetail.getAddressDetails().get(i).getPostCode();
                        String country = TextUtils.isEmpty(mJobDetail.getAddressDetails().get(i).getCountry())? "" : mJobDetail.getAddressDetails().get(i).getCountry();
                        address.setText(s + "\n" + street + "\n" + suburb + " " + state + "\n" + postCode + "," + country + "");

                        TextView property = (TextView) ll.findViewById(R.id.address_property);
                        TextView levels = (TextView) ll.findViewById(R.id.address_levels);
                        TextView floor = (TextView) ll.findViewById(R.id.address_floor);
                        TextView access = (TextView) ll.findViewById(R.id.address_access);

                        property.setText(mJobDetail.getAddressDetails().get(i).getProperty()==null ? "" : mJobDetail.getAddressDetails().get(i).getProperty());
                        levels.setText(mJobDetail.getAddressDetails().get(i).getLevels()==null ? "" : mJobDetail.getAddressDetails().get(i).getLevels());
                        floor.setText(mJobDetail.getAddressDetails().get(i).getFloor()+"");
                        access.setText(mJobDetail.getAddressDetails().get(i).getAccess()==null ? "" : mJobDetail.getAddressDetails().get(i).getAccess());

                        address1_text_clickable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                llAddress.setVisibility(View.GONE == llAddress.getVisibility() ? View.VISIBLE : View.GONE);
                            }
                        });
                        for (int j = 0; j <mJobDetail.getAddressDetails().get(i).getJobAddressNotes().size(); j++) {
                            LinearLayout ll1 = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.address_notes, null);
                            TextView tvNotes = (TextView) ll1.findViewById(R.id.tv_notes_key);
                            TextView tvNotesValue = (TextView) ll1.findViewById(R.id.tv_notes_value);
                            tvNotes.setText("Notes "+(j+1));
                            tvNotesValue.setText(mJobDetail.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes()==null ? ""
                                    : mJobDetail.getAddressDetails().get(i).getJobAddressNotes().get(j).getNotes());
                            llNotes.addView(ll1);
                        }
                        rosterHolder.address_container.addView(ll);
                    }
                }
                break;
            case 3:
                if (null == mJobDetail.getVehicle() || mJobDetail.getVehicle().getVehicleDetails() == null) {

                }else {
                    rosterHolder.truck_type.setText(mJobDetail.getVehicle().getVehicleDetails().getTruckType() == null ? "" : mJobDetail.getVehicle().getVehicleDetails().getTruckType());
                    rosterHolder.truck_capacity.setText(TextUtils.isEmpty(mJobDetail.getVehicle().getVehicleDetails().getCapacity()) ? "" : mJobDetail.getVehicle().getVehicleDetails().getCapacity() + "CuM");
                    rosterHolder.truck_gvm.setText(TextUtils.isEmpty(mJobDetail.getVehicle().getVehicleDetails().getGvm()) ? "" : mJobDetail.getVehicle().getVehicleDetails().getGvm()+ "Tonne");
                    rosterHolder.truck_tm.setText(TextUtils.isEmpty(mJobDetail.getVehicle().getVehicleDetails().getTm()) ? "" : mJobDetail.getVehicle().getVehicleDetails().getTm() + "Tonne");
                    rosterHolder.truck_men.setText(mJobDetail.getVehicle().getVehicleDetails().getMen() + "");
                    if (null == mJobDetail.getVehicle().getEquipment() ||
                            mJobDetail.getVehicle().getEquipment().size() == 0) {
                        rosterHolder.equipment_no.setText("");
                        rosterHolder.equipment_value.setText("");
                    } else {
                        String no = "";
                        String value = "";
                        for (int i = 0; i < mJobDetail.getVehicle().getEquipment().size(); i++) {
                            no += TextUtils.isEmpty(mJobDetail.getVehicle().getEquipment().get(i).getEquipmentQuantity()) ? ""+ "\n" : mJobDetail.getVehicle().getEquipment().get(i).getEquipmentQuantity() + "\n";
                            value += TextUtils.isEmpty(mJobDetail.getVehicle().getEquipment().get(i).getEquipment()) ? ""+ "\n" : mJobDetail.getVehicle().getEquipment().get(i).getEquipment() + "\n";
                        }
                        rosterHolder.equipment_no.setText(value);
                        rosterHolder.equipment_value.setText(no);
                    }
                }
                break;
            case 4:
                if (null == mJobDetail.getJobdetail()) {
                    break;
                }
                if (null == mJobDetail.getJobdetail().getJobtype()) {
                    break;
                } else {
                    rosterHolder.job_type.setText(mJobDetail.getJobdetail().getJobtype().getType() == null ? "" : mJobDetail.getJobdetail().getJobtype().getType());
                    rosterHolder.job_min_time.setText(mJobDetail.getJobdetail().getJobtype().getMinimumtime() == null ? "" : mJobDetail.getJobdetail().getJobtype().getMinimumtime());
                    rosterHolder.time_increments.setText(mJobDetail.getJobdetail().getJobtype().getTimeincrements() == null ? "" : mJobDetail.getJobdetail().getJobtype().getTimeincrements());
                }
                if (null == mJobDetail.getJobdetail().getTimedate()) {
                    break;
                } else {
                    String day = mJobDetail.getJobdetail().getTimedate().getDate();
                    String time = mJobDetail.getJobdetail().getTimedate().getStarttime();
                    rosterHolder.job_date.setText(SplitTimeUtils.splitDate(day) == null ? "" : SplitTimeUtils.splitDate(day));
//                    rosterHolder.starttime.setText(SplitTimeUtils.splitTime(time) == null ? "" : SplitTimeUtils.splitTime(time));
                    rosterHolder.starttime.setText(time == null ? "" : time);
                }
                if (null == mJobDetail.getJobdetail().getEstimatedtime()) {
                    break;
                } else {
                    rosterHolder.job_est_time.setText(mJobDetail.getJobdetail().getEstimatedtime().getEstimeStart() + "Hrs -" +
                            mJobDetail.getJobdetail().getEstimatedtime().getEstimeEnd() + "Hrs");
                }
                break;
            case 5:
                if (null == mJobDetail.getClientcharge()) {
                    break;
                }
                if (null == mJobDetail.getClientcharge().getVehiclecharges()) {
                    break;
                } else {
                    rosterHolder.job_rate_type.setText(mJobDetail.getClientcharge().getVehiclecharges().getRatetype() == null ? "" : mJobDetail.getClientcharge().getVehiclecharges().getRatetype());
                    rosterHolder.rate.setText("$" + mJobDetail.getClientcharge().getVehiclecharges().getRate());
                    rosterHolder.call_out_fee.setText(mJobDetail.getClientcharge().getVehiclecharges().getCalloutfee() == null ? "" : mJobDetail.getClientcharge().getVehiclecharges().getCalloutfee());
                    rosterHolder.return_fee.setText(mJobDetail.getClientcharge().getVehiclecharges().getReturnfee() == null ? "" : mJobDetail.getClientcharge().getVehiclecharges().getReturnfee());
                }
                if (null != mJobDetail.getClientcharge().getExtraCharges() && mJobDetail.getClientcharge().getExtraCharges().size() > 0) {
                    String name = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getClientcharge().getExtraCharges().size(); i++) {
                        name += TextUtils.isEmpty(mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeName()) ? ""
                                + "\n" : mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeName() + "\n";
                        String extraValue = mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeValue() ==null ? "" : mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeValue();
                        String extraQuality = mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeQuantity()==null ? "" : mJobDetail.getClientcharge().getExtraCharges().get(i).getExtraChargeQuantity();
                        value +=(extraValue+extraQuality)+ "\n";
                    }
                    rosterHolder.extra_charge_name0.setText(name);
                    rosterHolder.extra_charge_value0.setText(value);
                } else {
                    rosterHolder.extra_charge_name0.setText("");
                    rosterHolder.extra_charge_value0.setText("");
                }
                if (null != mJobDetail.getClientcharge().getDiscounts() && mJobDetail.getClientcharge().getDiscounts().size() > 0) {
                    String name = "";
                    String value = "";
                    for (int i = 0; i < mJobDetail.getClientcharge().getDiscounts().size(); i++) {
                        name += TextUtils.isEmpty(mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountName())
                        ? ""+ "\n" : mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountName() + "\n";

                        String extraValue = mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountRate() ==null ? "" : mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountRate();
                        String extraQuality = mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountQuantity()==null ? "" : mJobDetail.getClientcharge().getDiscounts().get(i).getDiscountQuantity();
                        value +=(extraValue+extraQuality)+ "\n";
                    }
                    rosterHolder.dicount_name.setText(name);
                    rosterHolder.dicount_value.setText(value);
                } else {
                    rosterHolder.dicount_name.setText("");
                    rosterHolder.dicount_value.setText("");
                }
                if (null == mJobDetail.getClientcharge().getTransactionFee()) {
                    break;
                } else {
                    String name = "";
                    String value = "";
                    for (int i = 0; i <mJobDetail.getClientcharge().getTransactionFee().size() ; i++) {
                        name += TextUtils.isEmpty(mJobDetail.getClientcharge().getTransactionFee().get(i).getTransactionFeeName()) ? ""
                                + "\n" : mJobDetail.getClientcharge().getTransactionFee().get(i).getTransactionFeeName() + "\n";
                        String extraValue = mJobDetail.getClientcharge().getTransactionFee().get(i).getTransactionFeeValue() ==null ? "" :
                                mJobDetail.getClientcharge().getTransactionFee().get(i).getTransactionFeeValue();
                        value +=extraValue+ "\n";
                    }
                    rosterHolder.action_name.setText(name);
                    rosterHolder.action_value.setText(value);
                }
                break;
            case 6:
                if (null == mJobDetail.getPayment()) {
                    break;
                }

               rosterHolder.llPayment.removeAllViews();
                for (int i = 0; i <mJobDetail.getPayment().size() ; i++) {
                    LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.layout_payment, null);
                    TextView tvMoney = (TextView) ll.findViewById(R.id.tv_payment_money);
                    TextView tvCash = (TextView) ll.findViewById(R.id.tv_payment_cash);
                    TextView tvDate = (TextView) ll.findViewById(R.id.tv_payment_date);
                    tvMoney.setText((i+1)+". $"+mJobDetail.getPayment().get(i).getPayment());
                    tvCash.setText(mJobDetail.getPayment().get(i).getPayType()==null ? "" :  mJobDetail.getPayment().get(i).getPayType());
                    tvDate.setText(mJobDetail.getPayment().get(i).getPayDate() == null ? "" : mJobDetail.getPayment().get(i).getPayDate());

                    rosterHolder.llPayment.addView(ll);
                }

                break;
            case 7:
                rosterHolder.items_container.removeAllViews();
                if (null == mJobDetail.getAttacheditems()) {
                    rosterHolder.attachitems_name.setText("Attached Items" + "(" + 0 + ")");
                    rosterHolder.attachitems_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                    break;
                }
                rosterHolder.attachitems_name.setText("Attached Items" + "(" + mJobDetail.getAttacheditems().size() + ")");
                rosterHolder.attachitems_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                for (int i = 0; i < mJobDetail.getAttacheditems().size(); i++) {
                    LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_roster_attachitems, null);
                    TextView tv = (TextView) ll.findViewById(R.id.attachitems_name);
                        if (mJobDetail.getAttacheditems().get(i).getFileType() == 1) {
                            tv.setText("Photo:");
                        } else {
                            tv.setText("File:");
                        }
                        tv.setTextAppearance(mActivity, R.style.JobTagTextStyle);


                    TextView tv1 = (TextView) ll.findViewById(R.id.file_date);
                    tv1.setText(mJobDetail.getAttacheditems().get(i).getDate());
                    RosterAttachItem item = mJobDetail.getAttacheditems().get(i);
                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().postSticky(item);//post给MainActivity的onEventLogin方法
                            mActivity.startActivity(new Intent(mActivity, BrowseFileActivity.class));
                        }
                    });
                    rosterHolder.items_container.addView(ll);
                }

                break;
            case 8:
                rosterHolder.inventory_container.removeAllViews();
                if (null == mJobDetail.getTotalinventory() || mJobDetail.getTotalinventory().size() == 0) {
                    rosterHolder.tv_inventory.setText("Total Inventory" + "(" + 0 + " CuM)");
                    rosterHolder.tv_inventory.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                }else {
                    double total = 0.00;
                    for (int i = 0; i < mJobDetail.getTotalinventory().size(); i++) {
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_roster_inventorys, null);
                        TextView tv = (TextView) ll.findViewById(R.id.inventory_name);
                        tv.setText(mJobDetail.getTotalinventory().get(i).getFurnitureName() + "(" + mJobDetail.getTotalinventory().get(i).getQuantity() + ")");
                        tv.setTextAppearance(mActivity, R.style.JobTagTextStyle);
                        TextView tv1 = (TextView) ll.findViewById(R.id.inventory_no);
                        tv1.setText((mJobDetail.getTotalinventory().get(i).getFurnitureVolume() == 0) ? "" : mJobDetail.getTotalinventory().get(i).getFurnitureVolume() + "CuM");
//                        tv1.setText(mJobDetail.getTotalinventory().get(i).getFurnitureVolume() + "CuM");
                        rosterHolder.inventory_container.addView(ll);
                        total += mJobDetail.getTotalinventory().get(i).getFurnitureVolume();
                    }
                    BigDecimal b = new BigDecimal(total);//保留两位小数
                    BigDecimal  newTotal = b.setScale(2,BigDecimal.ROUND_DOWN);
                    rosterHolder.tv_inventory.setText("Total Inventory" + "(" + newTotal + " CuM)");
                    rosterHolder.tv_inventory.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
                }
                break;
            case 9:
                if (null == mJobDetail.getNotes()) {
                    break;
                }
                rosterHolder.general_notes.setText(mJobDetail.getNotes().getGeneralnotes() == null ? "" : mJobDetail.getNotes().getGeneralnotes());
                rosterHolder.forman_notes.setText(mJobDetail.getNotes().getForemannotes() == null ? "" : mJobDetail.getNotes().getForemannotes());
                rosterHolder.forman_text_clickable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rosterHolder.forman_notes.setVisibility(View.VISIBLE == rosterHolder.forman_notes.getVisibility() ? View.GONE : View.VISIBLE);
                    }
                });
                break;
        }

        container.addView(view,0);
        return view;
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
       container.removeView((View)object);
    }

    public void refreshList(GetRosterJobRespBean job) {
        mJobDetail = job;
        notifyDataSetChanged();
    }

    public void refreshItsself() {
        loadJobInfo(mJob.getJobId());
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
