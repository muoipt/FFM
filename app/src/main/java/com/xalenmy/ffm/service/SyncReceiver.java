package com.xalenmy.ffm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xalenmy.ffm.utils.ComonUtils;

/**
 * Created by XalenMy on 3/2/2018.
 */

public class SyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent != null) {
            boolean syncResult = intent.getBooleanExtra(ComonUtils.SYNC_RESULT, false);
        }
    }
}
