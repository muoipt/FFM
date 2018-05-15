package com.muoipt.ffm.listener;

import android.content.Context;
import android.content.Intent;

import com.muoipt.ffm.view.CategoryAddNewActivity;

/**
 * Created by XalenMy on 3/22/2018.
 */

public class MainSummaryFragmentFabClickImpl implements IFabClickListener {
    @Override
    public void onFabClick(Context context) {
        Intent intent = new Intent(context, CategoryAddNewActivity.class);
        context.startActivity(intent);
    }
}
