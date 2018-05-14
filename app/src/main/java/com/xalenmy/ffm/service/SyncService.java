package com.xalenmy.ffm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import com.xalenmy.ffm.sync.SyncManagement;
import com.xalenmy.ffm.utils.ComonUtils;

public class SyncService extends Service {
    private Context mContext;

    public SyncService() {
    }

    public SyncService(Context context) {
        mContext = context;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SyncThread syncThread = new SyncThread();
        syncThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    class SyncThread extends Thread{

        @Override
        public void run() {
            SyncManagement syncManagement = new SyncManagement(mContext);
            Intent resultIntent = new Intent();
            resultIntent.setAction(ComonUtils.SYNC_ACTION);

            if (syncManagement.startSync()) {
                resultIntent.putExtra(ComonUtils.SYNC_RESULT, true);
            } else {
                resultIntent.putExtra(ComonUtils.SYNC_RESULT, false);
            }

            sendBroadcast(resultIntent);

            stopSelf();
        }
    }
}
