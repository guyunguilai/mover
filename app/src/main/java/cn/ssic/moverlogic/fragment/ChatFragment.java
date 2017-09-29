package cn.ssic.moverlogic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.io.File;
import java.util.List;

import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.download.DbDownUtil;
import cn.ssic.moverlogic.download.DownAdapter;
import cn.ssic.moverlogic.download.DownloadItemInfo;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ChatFragment extends Fragment {
    Context mContext;
    DbDownUtil dbUtil;
    List<DownloadItemInfo> listData;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        initResource();
    }

    private void initResource(){
        dbUtil= DbDownUtil.getInstance();
        listData=dbUtil.queryDownAll();
        /*第一次模拟服务器返回数据掺入到数据库中*/
        if(listData.isEmpty()){
            String[] downUrl=new String[]{"http://www.apk3.com/uploads/soft/201504/mosh.apk",
                    "http://www.apk3.com/uploads/soft/20160511/wshdyq.hit_1.15_25.apk",
            };
            for (int i = 0; i < downUrl.length; i++) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "test"+i + ".apk");
                DownloadItemInfo apkApi=new DownloadItemInfo();
                apkApi.setUrl(downUrl[i]);
                apkApi.setId(System.currentTimeMillis());
                apkApi.setSDownload_Status(DownloadItemInfo.DOWNLOAD_START);
                apkApi.setSavePath(outputFile.getAbsolutePath());
                dbUtil.save(apkApi);
            }
            listData=dbUtil.queryDownAll();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(mContext).inflate(R.layout.fragment_chat, null);
        EasyRecyclerView recyclerView=(EasyRecyclerView)view.findViewById(R.id.rv);
        DownAdapter adapter=new DownAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listData);
        return view;
    }

    public void onDestroy() {
        /*记录退出时下载任务的状态-复原用*/
        for (DownloadItemInfo downInfo : listData) {
            dbUtil.update(downInfo);
        }
        super.onDestroy();
    }
}
