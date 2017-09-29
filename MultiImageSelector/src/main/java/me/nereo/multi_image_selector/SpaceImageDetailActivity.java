package me.nereo.multi_image_selector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import me.nereo.multi_image_selector.utils.SmoothImageView;

/**
 * @Author: he.zhao
 * @Date:on 2017/2/9.
 * @E-mail:377855879@qq.com
 */

public class SpaceImageDetailActivity extends AppCompatActivity{
    private ImageView iv_smooth;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private SmoothImageView imageView;
    private String mDatas;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MIS_NO_ACTIONBAR);
//        setContentView(R.layout.mis_space);
//        iv_smooth= (ImageView) findViewById(R.id.iv_smooth);
        showImage();
    }

    private void showImage() {
        mDatas = getIntent().getStringExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        File imageFile = new File(mDatas);
        Glide.with(this).load(mDatas).into(imageView);

    }
}
