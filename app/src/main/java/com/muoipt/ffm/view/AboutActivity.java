package com.muoipt.ffm.view;

import android.drm.DrmStore;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.muoipt.ffm.R;
import com.muoipt.ffm.utils.AppConfig;

public class AboutActivity extends AppCompatActivity {

    private TextView txtVersion;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        toolbar = (Toolbar)findViewById(R.id.about_toolbar);
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
        toolbar.setTitle(getString(R.string.title_activity_about));

        txtVersion = (TextView)findViewById(R.id.txtVersion);

        txtVersion.setText(Html.fromHtml("<a href=\"mailto:muoipta@gmail.com\">Version 2.0 by Muoipt</a>"));
        txtVersion.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void handleOnBackPress() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(AppConfig.getThemeColor());
    }
}
