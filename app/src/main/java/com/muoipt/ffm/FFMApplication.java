package com.muoipt.ffm;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class FFMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppConfig appConfig = new AppConfig(this);
        SyncUtils syncUtils = new SyncUtils(this);
        ComonUtils comonUtils = new ComonUtils(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

//        // Add your initialization code here
//        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId(getResources().getString(R.string.parse_app_id))
//                .clientKey(getResources().getString(R.string.parse_client_key))
//                .server(getResources().getString(R.string.parse_server))
//                .build()
//        );
        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.parse_app_id))
                .clientKey(getResources().getString(R.string.parse_client_key))
                .server(getResources().getString(R.string.parse_server))
                .build()
        );

        ParseUser.enableRevocableSessionInBackground();
        ParseInstallation.getCurrentInstallation().saveInBackground();

//        PushService.
//        ParsePush.sendMessageInBackground()
//        ParsePush.subscribeInBackground("my_channel_1");

    }

    public void deleteDB(){
        this.deleteDatabase(DatabaseUtils.DATABASE_NAME);
    }
}
