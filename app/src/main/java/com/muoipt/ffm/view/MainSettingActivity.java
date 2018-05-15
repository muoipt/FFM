package com.muoipt.ffm.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muoipt.ffm.MainActivity;
import com.muoipt.ffm.R;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;

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
    private Toolbar toolbar;
    private TextView txt_sync_setting_email, txt_acount_setting_email;
    private boolean isColorDisplay = false;

    private BroadcastReceiver internetConnectionReceiver = null;

    private boolean internetConnected = false;

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
        txt_sync_setting_email = (TextView) findViewById(R.id.txt_sync_setting_email);
        txt_acount_setting_email = (TextView) findViewById(R.id.txt_acount_setting_email);

    }

    public boolean isInternetConnected() {
        return internetConnected;
    }

    public void setInternetConnected(boolean internetConnected) {
        this.internetConnected = internetConnected;
    }

    private void initControl() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getDrawable(R.drawable.back_icon_36dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        toolbar.setTitle(getString(R.string.title_activity_settings));

        layoutSettingSync.setOnClickListener(this);
        layoutSettingAccount.setOnClickListener(this);
        layoutSettingTheme.setOnClickListener(this);
        layoutSettingText.setOnClickListener(this);
        cbSyncAllways.setOnClickListener(this);
        imgEditSettingAccount.setOnClickListener(this);

        internetConnectionReceiver = new InternetConnectionReceiver();
        IntentFilter intentFilterNetworkConnection = new IntentFilter();
        intentFilterNetworkConnection.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetConnectionReceiver, intentFilterNetworkConnection);


        cbSyncAllways.setChecked(AppConfig.getSyncAlwaysOption());

        boolean isDisplayingColorDlg = AppConfig.getBooleanValueToSharePref(ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());

        if (isDisplayingColorDlg) {
            processSettingTheme();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_setting_theme:
                processSettingTheme();
                break;
            case R.id.layout_setting_text:
                break;
            case R.id.cb_sync_allways:
            case R.id.layout_setting_sync:
                processSyncAlways();
                break;
            case R.id.img_edit_setting_account:
            case R.id.layout_setting_account:
                processSettingAccount();
                break;
        }
    }

    private void processSettingTheme() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.content_setting_theme_layout);
        dialog.show();

        AppConfig.setBooleanValueToSharePref(true, ComonUtils.DISPLAY_COLOR_DIALOG, this);

        buttonCancel = dialog.findViewById(R.id.btn_select_color_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppConfig.setBooleanValueToSharePref(false, ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AppConfig.setBooleanValueToSharePref(false, ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());
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
                AppConfig.setBooleanValueToSharePref(false, ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());
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

        AppConfig.setBooleanValueToSharePref(false, ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshBGColor();

        toolbar.setBackgroundColor(AppConfig.getThemeColor());

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        if (currentUser.getUserEmail() != null) {
            txt_sync_setting_email.setText(currentUser.getUserEmail());
            txt_acount_setting_email.setText(currentUser.getUserEmail());
        }

    }

    @Override
    protected void onDestroy() {
        if (internetConnectionReceiver != null) {
            unregisterReceiver(internetConnectionReceiver);
        }
        super.onDestroy();
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

        toolbar.setBackgroundColor(AppConfig.getThemeColor());
    }

    private void handleOnBackPress() {
        this.finish();
        AppConfig.setBooleanValueToSharePref(false, ComonUtils.DISPLAY_COLOR_DIALOG, getApplicationContext());
    }

    private void processSyncAlways() {
        boolean isChecked = cbSyncAllways.isChecked();

        AppConfig.setSyncAlwaysOption(isChecked);

        //check internet connection
        if (isChecked && !internetConnected) {
            displayAlertDlg(2);

            cbSyncAllways.setChecked(false);
        }
    }

    private class InternetConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle extras = intent.getExtras();

                NetworkInfo info = (NetworkInfo) extras
                        .getParcelable("networkInfo");

                NetworkInfo.State state = info.getState();
                Log.d("TEST Internet", info.toString() + " "
                        + state.toString());

                if (state == NetworkInfo.State.CONNECTED) {
                    internetConnected = true;
                } else {
                    internetConnected = false;
                }
            }
        }
    }

    private void processSettingAccount() {
        Intent intent = new Intent(this, SignUpActivity.class);

        UserDetail currentUser = AppConfig.getUserLogInInfor();

        if (currentUser.getUserEmail() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ComonUtils.OPEN_USER_PROFILE_BUNDLE, currentUser);
            intent.putExtra(ComonUtils.OPEN_USER_PROFILE_INTENT, bundle);
        } else {
            intent.setAction(ComonUtils.ACTION_ADD_USER_FROM_SETTING);
        }

        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    private void displayAlertDlg(int item) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sync_layout);
        dialog.show();

        ImageView img_sync = dialog.findViewById(R.id.img_sync);
        img_sync.setImageResource(R.drawable.info_icon);
        TextView txt_message = dialog.findViewById(R.id.txt_message);

        if (item == 0) {
            txt_message.setText(getString(R.string.alert_login_info));
        } else if (item == 1) {
            txt_message.setText(getString(R.string.alert_internet_info));
        } else if(item == 2){
            txt_message.setText(getString(R.string.alert_user_status_info));
        }

        Button buttonOK = dialog.findViewById(R.id.btn_sync_ok);
        AppConfig.changeRoundViewColor(buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
