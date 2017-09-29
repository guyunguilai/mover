package cn.ssic.moverlogic.netokhttp3;

import java.util.Map;

import cn.ssic.moverlogic.bean.ActiveJobRespBean;
import cn.ssic.moverlogic.bean.AddExtraChargeRespBean;
import cn.ssic.moverlogic.bean.CheckVerisonRespBean;
import cn.ssic.moverlogic.bean.ConfirmJobRespBean;
import cn.ssic.moverlogic.bean.DispatchJobRespBean;
import cn.ssic.moverlogic.bean.DurationJobRespBean;
import cn.ssic.moverlogic.bean.EditUserImgRespBean;
import cn.ssic.moverlogic.bean.EditUserInfoRespBean;
import cn.ssic.moverlogic.bean.GetAlertJobRespBean;
import cn.ssic.moverlogic.bean.GetAlertsRespBean;
import cn.ssic.moverlogic.bean.GetCreditCardTypeRespBean;
import cn.ssic.moverlogic.bean.GetJobExtraChargeRespBean;
import cn.ssic.moverlogic.bean.GetJobSheetRespBean;
import cn.ssic.moverlogic.bean.GetPayModeRespBean;
import cn.ssic.moverlogic.bean.GetRosterJobRespBean;
import cn.ssic.moverlogic.bean.GetRosterRespBean;
import cn.ssic.moverlogic.bean.GetUserInfoRespBean;
import cn.ssic.moverlogic.bean.InspectionJobRespBean;
import cn.ssic.moverlogic.bean.JumpStatusRespBean;
import cn.ssic.moverlogic.bean.LoginRespBean;
import cn.ssic.moverlogic.bean.LogoutRespBean;
import cn.ssic.moverlogic.bean.PauseJobRespBean;
import cn.ssic.moverlogic.bean.PayCashRespBean;
import cn.ssic.moverlogic.bean.PaymentRespBean;
import cn.ssic.moverlogic.bean.RespBean;
import cn.ssic.moverlogic.bean.StartJobRespBean;
import cn.ssic.moverlogic.bean.SubExtraChargeRespBean;
import cn.ssic.moverlogic.bean.UpdateAttachmentRespBean;
import cn.ssic.moverlogic.net2request.InvoiceBean;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface IHttp {
    //@Path 补全url中的{}变量部分 动态获取URL地址：@Path
    //@Field 用于POST请求的表单，Formed过的body //使用@Field时记得添加@FormUrlEncoded
    //@FieldMap 相当于多个@Field，以hashmap的形式提交
    //@Body post请求过去所带的数据body，可以以文本（比如json），图片等形式传输作为一般的Requestbody，也可以用Field作为FormBody提交，FromBody extends RequestBody
    //@Query 跟在url的问号后面进行赋值操作  使用@Query注解即可完成我们的需求，在@Query(“sort”)中，short就好比是URL请求地址中的键，而它说对应的String sort中的sort则是它的值。
    //@QueryMap：相当于多个@Query
    //@Url 用于重新定义接口地址，将地址以参数的形式传入
    //@PartMap 用于post多媒体图片
    //http://blog.csdn.net/guiman/article/details/51480497
    @POST("/mlogic/app/staff/login")
    @FormUrlEncoded
    rx.Observable<RespBean<LoginRespBean>> login(@Field("username") String username, @Field("password") String pwd);

    @POST("/mlogic/app/staff/logout")
    @FormUrlEncoded
    rx.Observable<RespBean<LogoutRespBean>> logout(@Field("uid") String uid);

    @POST("/mlogic/app/staff/editUserInfo")
    @FormUrlEncoded
    rx.Observable<RespBean<EditUserInfoRespBean>> editUserInfo(
            @Field("uid") String uid,
            @Field("username") String username,
            @Field("password") String npassword,
            @Field("firstname") String firstname,
            @Field("surname") String surname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("isAlert") short isAlert
    );

    @POST("/mlogic/app/staff/getUserInfo")
    @FormUrlEncoded
    rx.Observable<RespBean<GetUserInfoRespBean>> getUserInfo(@Field("uid") String uid);

    @POST("/mlogic/app/staff/getAlertsList")
    @FormUrlEncoded
    rx.Observable<RespBean<GetAlertsRespBean>> getAlerts(@Field("uid") String uid);

    @POST("/mlogic/app/staff/confirmJob")
    @FormUrlEncoded
    rx.Observable<RespBean<ConfirmJobRespBean>> confirmJob(@Field("uid") String uid, @Field("jobId") String jobId, @Field("status") int status);

    @POST("/mlogic/app/staff/getRosterList")
    @FormUrlEncoded
    rx.Observable<RespBean<GetRosterRespBean>> getRosters(@Field("uid") String uid);

    @POST("/mlogic/app/staff/editUserImage")
    @Multipart
    rx.Observable<RespBean<EditUserImgRespBean>> editUserImage(@Part("uid") int uid, @PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/editUserImage")
    @Multipart
    rx.Observable<RespBean<EditUserImgRespBean>> uploadImages(@QueryMap Map<String, Object> map);

    @POST("/mlogic/app/staff/getAlertJob")
    @FormUrlEncoded
    rx.Observable<RespBean<GetAlertJobRespBean>> getAlertJob(@Field("jobId") int jobId, @Field("uid") int uid);

    @POST("/mlogic/app/staff/getRosterJob")
    @FormUrlEncoded
    rx.Observable<RespBean<GetRosterJobRespBean>> getRosterJob(@Field("jobId") int jobId, @Field("uid") int uid);

    @POST("/mlogic/app/staff/activeJob")
    @FormUrlEncoded
    rx.Observable<RespBean<ActiveJobRespBean>> activeJob(@Field("jobId") int jobId, @Field("uid") int uid);

    @POST("/mlogic/app/staff/getActiveJob")
    @FormUrlEncoded
    rx.Observable<RespBean<ActiveJobRespBean>> getActiveJob(@Field("uid") int uid);

    @POST("/mlogic/app/staff/inspectionJob")
    @Multipart
    rx.Observable<RespBean<InspectionJobRespBean>> inspectionJob(@PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/dispatchJob")
    @FormUrlEncoded
    rx.Observable<RespBean<DispatchJobRespBean>> dispatchJob(@Field("jobId") int jobId, @Field("uid") int uid,@Field("item") int item);

    @POST("/mlogic/app/staff/startJob")
    @FormUrlEncoded
    rx.Observable<RespBean<StartJobRespBean>> startJob(@Field("jobId") int jobId);

    @POST("/mlogic/app/staff/pauseJob")//reason 1   是 break ;  reason 2   是 finish;
    @FormUrlEncoded
    rx.Observable<RespBean<PauseJobRespBean>> pauseJob(@Field("jobId") int jobId, @Field("reason") int reason);

    @POST("/mlogic/app/staff/durationJob")
    @FormUrlEncoded
    rx.Observable<RespBean<DurationJobRespBean>> durationJob(@Field("id") int id, @Field("jobId") int jobId);

    @POST("/mlogic/app/staff/uploadAttachment")
    @Multipart
    rx.Observable<RespBean<UpdateAttachmentRespBean>> uploadAttachment(@Part("jobAttachment") RequestBody jobAttachment, @PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/addExtraCharges")
    @FormUrlEncoded
    rx.Observable<RespBean<AddExtraChargeRespBean>> addExtraCharges(@Field("jobId") int jobId, @Field("chargeType") int chargeType, @Field("charge") double charge, @Field("quantity") int quantity);

    @POST("/mlogic/app/staff/subExtraCharges")
    @FormUrlEncoded
    rx.Observable<RespBean<SubExtraChargeRespBean>> subExtraCharges(@Field("jobId") int jobId, @Field("chargeType") int chargeType, @Field("charge") double charge, @Field("quantity") int quantity);

    @POST("/mlogic/app/staff/getJobExtraCharge")
    @FormUrlEncoded
    rx.Observable<RespBean<GetJobExtraChargeRespBean>> getJobExtraCharge(@Field("jobId") int jobId);

    @POST("/mlogic/app/staff/inspectAndSign")
    @Multipart
    rx.Observable<RespBean<InspectionJobRespBean>> inspectAndSign(@PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/getPayment")
    @FormUrlEncoded
    rx.Observable<RespBean<PaymentRespBean>> getPayment(@Field("jobId") int jobId);

    @POST("/mlogic/app/staff/payCash")
    @FormUrlEncoded
//payType:1 cash,2 card
    rx.Observable<RespBean<PayCashRespBean>> payCash(@Field("jobId") int jobId, @Field("payType") int payType, @Field("payment") double payment, @Field("totalPrice") double totalPrice);

    @POST("/mlogic/app/staff/getCreditCardType")
    rx.Observable<RespBean<GetCreditCardTypeRespBean>> getCreditCardType();

    @POST("/mlogic/app/staff/getPayMode")
    rx.Observable<RespBean<GetPayModeRespBean>> getPayMode();

    @POST("/mlogic/app/staff/payCreditCard")
    @FormUrlEncoded
    rx.Observable<RespBean<JumpStatusRespBean>> payCreditCard(@Field("jobId") int jobId, @Field("payType") int payType, @Field("payment") double payment,
                                                              @Field("cardType") int cardType, @Field("cardNo") String cardNo, @Field("cardYear") int cardYear,
                                                              @Field("cardMonth") int cardMonth, @Field("transactionFee") String transactionFee, @Field("totalPrice") int totalPrice);

    @POST("/mlogic/app/staff/payCreditCard")
    @FormUrlEncoded
    rx.Observable<RespBean<PayCashRespBean>> payCreditCard(@Field("payInfoStr") String payInfoStr, @Field("totalPrice") double totalPrice);

    @POST("/mlogic/app/staff/finishJob")
    @FormUrlEncoded
    rx.Observable<RespBean<JumpStatusRespBean>> finishJob(@Field("jobId") int jobId, @Field("uid") int uid);

    @POST("/mlogic/app/staff/isInvoice")
    @FormUrlEncoded
    rx.Observable<RespBean<InvoiceBean>> isInvoice(@Field("jobId") int jobId, @Field("uid") int uid, @Field("status") int status);

    @POST("/mlogic/app/staff/jobSheet")
    @FormUrlEncoded
    rx.Observable<RespBean<GetJobSheetRespBean>> jobSheet(@Field("jobId") int jobId, @Field("uid") int uid);

    @POST("http://13.55.134.228/mlogic/app/staff/upLocation")
    @FormUrlEncoded
    rx.Observable<RespBean<Object>> upGps(@Field("longitude") double longitude, @Field("latitude") double latitude, @Field("devietype") int devietype, @Field("address") String address);

    @POST("/mlogic/app/staff/checkAppVersion")
    @FormUrlEncoded
    rx.Observable<RespBean<CheckVerisonRespBean>> checkVer(@Field("deviceId") int deviceId);

    @Streaming
    @GET
    rx.Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}
