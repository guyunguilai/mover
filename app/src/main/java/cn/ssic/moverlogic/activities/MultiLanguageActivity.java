package cn.ssic.moverlogic.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ssic.moverlogic.BaseActivity;
import cn.ssic.moverlogic.MainActivity;
import cn.ssic.moverlogic.R;
import cn.ssic.moverlogic.mvppresenter.IPresenter;

import static java.security.AccessController.getContext;

/**
 * @Author: he.zhao
 * @Date:on 2017/4/17.
 * @E-mail:377855879@qq.com
 */

public class MultiLanguageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.multi_title_left)
    TextView multiTitleLeft;
    @BindView(R.id.multi_title)
    TextView multiTitle;
    @BindView(R.id.rb_en)
    RadioButton rbEn;
    @BindView(R.id.rb_cn)
    RadioButton rbCn;
    @BindView(R.id.multi_rg)
    RadioGroup multiRg;
    @BindView(R.id.multi_save)
    TextView mSave;

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_multi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        rbEn.setChecked(true);
        multiRg.setOnCheckedChangeListener(this);

    }

    @OnClick({R.id.multi_title_left,R.id.multi_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.multi_title_left:
                finish();
                break;
            case R.id.multi_save:
                if(rbEn.isChecked()){

                }else{

                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.rb_en:
              /*  Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                // 应用用户选择语言
                config.locale = new Locale("","");
                resources.updateConfiguration(config, dm);
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);*/
                break;
            case R.id.rb_cn:
               /* Resources resources1 = getResources();
                DisplayMetrics dm1 = resources1.getDisplayMetrics();
                Configuration config1 = resources1.getConfiguration();
                // 应用用户选择语言
                config1.locale = new Locale("zh","CN");
                resources1.updateConfiguration(config1, dm1);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
                break;
        }

    }
}
