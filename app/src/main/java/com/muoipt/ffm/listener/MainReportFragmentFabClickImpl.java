package com.muoipt.ffm.listener;

import android.content.Context;
import android.content.Intent;

import com.muoipt.ffm.view.ReportAddNewActivity;

/**
 * Created by XalenMy on 3/22/2018.
 */

public class MainReportFragmentFabClickImpl implements IFabClickListener {
    @Override
    public void onFabClick(Context context) {
        Intent intent = new Intent(context, ReportAddNewActivity.class);
        context.startActivity(intent);
    }
}
