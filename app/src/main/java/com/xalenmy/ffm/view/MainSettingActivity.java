package com.xalenmy.ffm.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.utils.AppConfig;

public class MainSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout layoutSettingSync, layoutSettingAccount, layoutSettingTheme, layoutSettingText;
    private CheckBox cbSyncAllways;
    private ImageView imgEditSettingAccount;
    private ImageButton btnColor1, btnColor2, btnColor3, btnColor4, btnColor5, btnColor6, btnColor7, btnColor8, btnColor9, btnColor10, btnColor11, btnColor12, btnColor13, btnColor14, btnColor15;
    private int selectedColor = 0;
    private int selectedColorAlpha1 = 0;
    private int selectedColorAlpha2 = 0;
    private View div1, div2, div3, div4;
    private Button buttonCancel;
//    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getControlWidget();
        initControl();

//        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));
    }

    private void getControlWidget() {
        layoutSettingSync = (RelativeLayout) findViewById(R.id.layout_setting_sync);
        layoutSettingAccount = (RelativeLayout) findViewById(R.id.layout_setting_account);
        layoutSettingTheme = (RelativeLayout) findViewById(R.id.layout_setting_theme);
        layoutSettingText = (RelativeLayout) findViewById(R.id.layout_setting_text);
        cbSyncAllways = (CheckBox) findViewById(R.id.cb_sync_allways);
        imgEditSettingAccount = (ImageView) findViewById(R.id.img_edit_setting_account);
        div1 = findViewById(R.id.div1);
        div2 = findViewById(R.id.div2);
        div3 = findViewById(R.id.div3);
        div4 = findViewById(R.id.div4);

    }

    private void initControl() {
        layoutSettingSync.setOnClickListener(this);
        layoutSettingAccount.setOnClickListener(this);
        layoutSettingTheme.setOnClickListener(this);
        layoutSettingText.setOnClickListener(this);
        cbSyncAllways.setOnClickListener(this);
        imgEditSettingAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_setting_sync:
                break;
            case R.id.layout_setting_account:
                break;
            case R.id.layout_setting_theme:
                processSettingTheme();
                break;
            case R.id.layout_setting_text:
                break;
            case R.id.cb_sync_allways:
                break;
            case R.id.img_edit_setting_account:
                break;
        }
    }

    private void processSettingTheme() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.content_setting_theme_layout);
        dialog.show();

        buttonCancel = dialog.findViewById(R.id.btn_select_color_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnColor1 = dialog.findViewById(R.id.color_1);
        btnColor2 = dialog.findViewById(R.id.color_2);
        btnColor3 = dialog.findViewById(R.id.color_3);
        btnColor4 = dialog.findViewById(R.id.color_4);
        btnColor5 = dialog.findViewById(R.id.color_5);
        btnColor6 = dialog.findViewById(R.id.color_6);
        btnColor7 = dialog.findViewById(R.id.color_7);
        btnColor8 = dialog.findViewById(R.id.color_8);
        btnColor9 = dialog.findViewById(R.id.color_9);
        btnColor10 = dialog.findViewById(R.id.color_10);
        btnColor11 = dialog.findViewById(R.id.color_11);
        btnColor12 = dialog.findViewById(R.id.color_12);
        btnColor13 = dialog.findViewById(R.id.color_13);
        btnColor14 = dialog.findViewById(R.id.color_14);
        btnColor15 = dialog.findViewById(R.id.color_15);

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_1;
                selectedColorAlpha1 = R.color.color_setting_1_alpha1;
                selectedColorAlpha2 = R.color.color_setting_1_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_2;
                selectedColorAlpha1 = R.color.color_setting_2_alpha1;
                selectedColorAlpha2 = R.color.color_setting_2_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_3;
                selectedColorAlpha1 = R.color.color_setting_3_alpha1;
                selectedColorAlpha2 = R.color.color_setting_3_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_4;
                selectedColorAlpha1 = R.color.color_setting_4_alpha1;
                selectedColorAlpha2 = R.color.color_setting_4_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_5;
                selectedColorAlpha1 = R.color.color_setting_5_alpha1;
                selectedColorAlpha2 = R.color.color_setting_5_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_6;
                selectedColorAlpha1 = R.color.color_setting_6_alpha1;
                selectedColorAlpha2 = R.color.color_setting_6_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_7;
                selectedColorAlpha1 = R.color.color_setting_7_alpha1;
                selectedColorAlpha2 = R.color.color_setting_7_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_8;
                selectedColorAlpha1 = R.color.color_setting_8_alpha1;
                selectedColorAlpha2 = R.color.color_setting_8_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_9;
                selectedColorAlpha1 = R.color.color_setting_9_alpha1;
                selectedColorAlpha2 = R.color.color_setting_9_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_10;
                selectedColorAlpha1 = R.color.color_setting_10_alpha1;
                selectedColorAlpha2 = R.color.color_setting_10_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_11;
                selectedColorAlpha1 = R.color.color_setting_11_alpha1;
                selectedColorAlpha2 = R.color.color_setting_11_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_12;
                selectedColorAlpha1 = R.color.color_setting_12_alpha1;
                selectedColorAlpha2 = R.color.color_setting_12_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_13;
                selectedColorAlpha1 = R.color.color_setting_13_alpha1;
                selectedColorAlpha2 = R.color.color_setting_13_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_14;
                selectedColorAlpha1 = R.color.color_setting_14_alpha1;
                selectedColorAlpha2 = R.color.color_setting_14_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });

        btnColor15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = R.color.color_setting_15;
                selectedColorAlpha1 = R.color.color_setting_15_alpha1;
                selectedColorAlpha2 = R.color.color_setting_15_alpha2;
                setThemeColor();
                dialog.dismiss();
            }
        });
    }

    private void setThemeColor() {
        if (selectedColor == 0) return;

        AppConfig.current_theme_setting_color = selectedColor;
        AppConfig.current_theme_setting_color_alpha1 = selectedColorAlpha1;
        AppConfig.current_theme_setting_color_alpha2 = selectedColorAlpha2;

        AppConfig.setIntValueToSharePref(selectedColor, AppConfig.CURRENT_SETTING_THEME_COLOR, this);
        AppConfig.setIntValueToSharePref(selectedColorAlpha1, AppConfig.CURRENT_SETTING_THEME_COLOR_ALPHA1, this);
        AppConfig.setIntValueToSharePref(selectedColorAlpha2, AppConfig.CURRENT_SETTING_THEME_COLOR_ALPHA2, this);

        refreshBGColor();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshBGColor();

    }

    private void refreshBGColor() {
        div1.setBackgroundColor(AppConfig.getThemeColor());
        div2.setBackgroundColor(AppConfig.getThemeColor());
        div3.setBackgroundColor(AppConfig.getThemeColor());
        div4.setBackgroundColor(AppConfig.getThemeColor());
//        actionBar.setBackgroundDrawable(new ColorDrawable(AppConfig.getThemeColor()));

        cbSyncAllways.setCompoundDrawableTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        cbSyncAllways.setBackgroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        cbSyncAllways.setLinkTextColor(AppConfig.getThemeColor());
        cbSyncAllways.setHighlightColor(AppConfig.getThemeColor());
        cbSyncAllways.setHintTextColor(AppConfig.getThemeColor());
        cbSyncAllways.setTextColor(AppConfig.getThemeColor());
//        cbSyncAllways.setDrawingCacheBackgroundColor(getColor(AppConfig.getThemeColor()));
        cbSyncAllways.setButtonTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        cbSyncAllways.setForegroundTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
        cbSyncAllways.setCompoundDrawableTintList(ColorStateList.valueOf(AppConfig.getThemeColor()));
    }
}
