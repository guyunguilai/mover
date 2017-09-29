package cn.ssic.moverlogic.download;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.ssic.moverlogic.R;

/**
 * 下载item
 * Created by WZG on 2016/10/21.
 */

public class DownHolder extends BaseViewHolder<DownloadItemInfo> implements View.OnClickListener{
    private TextView tvMsg;
    private NumberProgressBar progressBar;
    private DownloadItemInfo apkApi;
    private DownloadManager manager;

    public DownHolder(ViewGroup parent) {
        super(parent, R.layout.view_item_holder);
        manager= DownloadManager.getInstance();
        $(R.id.btn_rx_down).setOnClickListener(this);
        $(R.id.btn_rx_pause).setOnClickListener(this);
        progressBar=$(R.id.number_progress_bar);
        tvMsg=$(R.id.tv_msg);
    }

    @Override
    public void setData(DownloadItemInfo data) {
        super.setData(data);
        data.setListener(httpProgressOnNextListener);
        apkApi=data;
        progressBar.setMax((int) apkApi.getCountLength());
        progressBar.setProgress((int) apkApi.getReadLength());
        /*第一次恢复 */
        switch (apkApi.getSDownload_Status()){
            case DownloadItemInfo.DOWNLOAD_START:
                /*起始状态*/
                break;
            case DownloadItemInfo.DOWNLOAD_PAUSE:
                tvMsg.setText("暂停中");
                break;
            case DownloadItemInfo.DOWNLOAD_ING:
                manager.startDownload(apkApi);
                break;
            case DownloadItemInfo.DOWNLOAD_STOP:
                tvMsg.setText("下载停止");
                break;
            case DownloadItemInfo.DOWNLOAD_ERROR:
                tvMsg.setText("下載錯誤");
                break;
            case  DownloadItemInfo.DOWNLOAD_FINISH:
                tvMsg.setText("下载完成");
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_down:
                if(apkApi.getSDownload_Status()!= DownloadItemInfo.DOWNLOAD_FINISH){
                    manager.startDownload(apkApi);
                }
                break;
            case R.id.btn_rx_pause:
                manager.pauseDownload(apkApi);
                break;
        }
    }

    /*下载回调*/
    HttpProgressOnNextListener<DownloadItemInfo> httpProgressOnNextListener=new HttpProgressOnNextListener<DownloadItemInfo>() {
        @Override
        public void onNext(DownloadItemInfo baseDownEntity) {
            tvMsg.setText("提示：下载完成");
            Toast.makeText(getContext(),baseDownEntity.getSavePath(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
            tvMsg.setText("提示:开始下载");
        }

        @Override
        public void onComplete() {
            tvMsg.setText("提示：下载结束");
        }

        @Override
        public void onError(Throwable e) {
            tvMsg.setText("失败:"+e.toString());
        }
        @Override
        public void onPause() {
            tvMsg.setText("提示:暂停");
        }

        @Override
        public void onStop() {

        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            tvMsg.setText("提示:下载中");
            progressBar.setMax((int) countLength);
            progressBar.setProgress((int) readLength);
        }
    };
}