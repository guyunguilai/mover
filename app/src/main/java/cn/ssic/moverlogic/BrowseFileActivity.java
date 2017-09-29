package cn.ssic.moverlogic;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.barteksc.pdfviewer.PDFView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.download.DbDownUtil;
import cn.ssic.moverlogic.download.DownloadItemInfo;
import cn.ssic.moverlogic.download.DownloadManager;
import cn.ssic.moverlogic.download.HttpProgressOnNextListener;
import cn.ssic.moverlogic.mvppresenter.IPresenter;
import cn.ssic.moverlogic.net2request.RosterAttachItem;
import cn.ssic.moverlogic.net2request.Staff;
import cn.ssic.moverlogic.netokhttp2.HttpConstants;
import cn.ssic.moverlogic.utils.FR;
import cn.ssic.moverlogic.utils.FileUtils;
import cn.ssic.moverlogic.utils.ViewsUtils;
import me.nereo.multi_image_selector.bean.Image;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
/**
 * Created by Administrator on 2017/3/4.
 */

public class BrowseFileActivity extends BaseActivity implements OnLoadCompleteListener,OnPageChangeListener{
    @BindView(R.id.file_name)
    TextView tv_fileName;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_left)
    TextView title_left;
    @BindView(R.id.file_icon)
    ImageView iv_fileIcon;
    @BindView(R.id.open_file_button)
    ImageView iv_open_button;
    @BindView(R.id.webview_wv)
    WebView webView;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar progressBar;
    @BindView(R.id.browse_file_container)
    RelativeLayout open_file_rl;
    @BindView(R.id.browse_image)
    ImageView imageView;
    @BindView(R.id.pdf_viewer)
    PDFView mPdfView;

    RosterAttachItem attachItem;
    DownloadItemInfo downloadItemInfo;
    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.browse_file_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setSupportZoom(true);
    }

    @SuppressWarnings("ResourceType")
    @OnClick({R.id.file_name, R.id.file_icon, R.id.open_file_button,R.id.title_left})
    public void onClick(View view) {
        ViewsUtils.preventViewMultipleClick(view, 1000);
        switch (view.getId()) {
            case R.id.open_file_button:
                switch (attachItem.getFileType()){
                    case FileUtils.WPS:
                    case FileUtils.DPS:
                    case FileUtils.ET:
                    case FileUtils.ZIP:
                    case FileUtils.RAR:
                    case FileUtils.TRA:
                    case FileUtils.ARJ:
                    case FileUtils.FLV:
                    case FileUtils.AVI:
                    case FileUtils.MP4:
                    case FileUtils.RMVB:
                    case FileUtils.WMV:
                    case FileUtils.HTM:
                    case FileUtils.HTML:
                    case FileUtils.TMP:
                    case FileUtils.SWF:
                    case FileUtils.TXT:
                        File file = new File(downloadItemInfo.getSavePath());
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromFile(file);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.title_left:
                DownloadManager.getInstance().stopAllDownload();
                finish();
                break;
            case R.id.title_right:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventLogin(RosterAttachItem item) {
        attachItem = item;
        downloadItemInfo=new DownloadItemInfo();
        downloadItemInfo.setUrl(HttpConstants.TEST_URL+item.getFileUrl());
        downloadItemInfo.setId(System.currentTimeMillis());
        downloadItemInfo.setSDownload_Status(DownloadItemInfo.DOWNLOAD_START);
        downloadItemInfo.setSavePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+item.getFileName());
        downloadItemInfo.setListener(httpProgressOnNextListener);
        DownloadManager.getInstance().startDownload(downloadItemInfo);
        title.setText(attachItem.getFileName());
        tv_fileName.setText(attachItem.getFileName());

//        DbDownUtil.getInstance().save(apkApi);
    }
    private TextView tvMsg;
    /*下载回调*/
    HttpProgressOnNextListener<DownloadItemInfo> httpProgressOnNextListener=new HttpProgressOnNextListener<DownloadItemInfo>() {
        @Override
        public void onNext(DownloadItemInfo baseDownEntity) {
//            tvMsg.setText("提示：下载完成");
            Toast.makeText(BrowseFileActivity.this,baseDownEntity.getSavePath(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
//            tvMsg.setText("提示:开始下载");
        }

        @Override
        public void onComplete() {
            FR fr;
            switch (attachItem.getFileType()){
                case FileUtils.PIC:
                    open_file_rl.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageURI(Uri.parse(downloadItemInfo.getSavePath()));
                    imageView.setOnTouchListener(new ImageViewTouchListener());
                    break;
                case FileUtils.DOC:
                    open_file_rl.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
//                    fr = new FR(Environment.getExternalStorageDirectory()+"/aa.docx");
                    fr = new FR(downloadItemInfo.getSavePath());
                    webView.loadUrl(fr.returnPath);
                    break;
                case FileUtils.EXCEL:
                    open_file_rl.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
//                    fr = new FR(Environment.getExternalStorageDirectory()+"/aa.xlsx");
                    fr = new FR(downloadItemInfo.getSavePath());
                    webView.loadUrl(fr.returnPath);
                    break;
                case FileUtils.PDF:
                    open_file_rl.setVisibility(View.GONE);
                    mPdfView.setVisibility(View.VISIBLE);
                    mPdfView.fromFile(new File(downloadItemInfo.getSavePath()))
                            .defaultPage(0)
                            .onPageChange(BrowseFileActivity.this)
                            .enableAnnotationRendering(true)
                            .onLoad(BrowseFileActivity.this)
                            .load();
                    break;
//                default:
//                    open_file_rl.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(Throwable e) {
            System.out.print(e.getStackTrace());
        }
        @Override
        public void onPause() {
//            tvMsg.setText("提示:暂停");
        }

        @Override
        public void onStop() {

        }

        @Override
        public void updateProgress(long readLength, long countLength) {
//            tvMsg.setText("提示:下载中");
            progressBar.setMax((int) countLength);
            progressBar.setProgress((int) readLength);
        }
    };

    @Override
    protected void onDestroy() {
        DownloadManager.getInstance().stopAllDownload();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    private final class ImageViewTouchListener implements View.OnTouchListener {

        /** 记录是拖拉照片模式还是放大缩小照片模式 */
        private int mode = 0;// 初始状态
        /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;

        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        /** 用于记录拖拉图片移动的坐标位置 */
        private Matrix matrix = new Matrix();
        /** 用于记录图片要进行拖拉时候的坐标位置 */
        private Matrix currentMatrix = new Matrix();

        /** 两个手指的开始距离 */
        private float startDis;
        /** 两个手指的中间点 */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }

        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float)Math.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }
    }
}
