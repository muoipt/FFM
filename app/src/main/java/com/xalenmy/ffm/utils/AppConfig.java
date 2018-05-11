package com.xalenmy.ffm.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.xalenmy.ffm.FFMApplication;
import com.xalenmy.ffm.R;
import com.xalenmy.ffm.control.CategoryDetailControl;
import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.GroupDetail;
import com.xalenmy.ffm.model.UserDetail;

import java.io.File;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class AppConfig {
    public static final String SAMPLE_DATA_APP = "SAMPLE_DATA_APP";
    public static final Uri CONTENT_URI = Uri.parse("content://com.xalenmy.ffm.ffmcontentprovider/ffmitems");
    public static final String URI = "com.xalenmy.ffm.ffmcontentprovider";
    public static final String FFM_ITEM = "ffmitems";
    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;

    public static final String KEY_ID = "FFM_ID";
    public static final String KEY_TASK = "FFM_TASK";
    public static final String KEY_CREATION_DATE = "FFM_CREATION_DATE";

    public static final String COLOR_THEME = "COLOR_THEME_SAVED";
    public static final String GALLERY3D = "com.sec.android.gallery3d";
    public static final String INDEX_SORT_BY_VALUE = "INDEX_SORT_BY";
    public static final String CURRENT_USER_ID = "USER_ID";
    public static final String CURRENT_USER_EMAIL = "USER_EMAIL";
    public static final String CURRENT_USER_PASSWORD = "USER_PASSWORD";
    public static final String CURRENT_USER_GROUP_ID = "USER_GROUP_ID";
    public static final String CURRENT_USER_AVATAR = "USER_AVATAR";
    public static final String CURRENT_USER_STATUS = "CURRENT_USER_STATUS";
    public static final String CURRENT_USER_ROLE = "CURRENT_USER_ROLE";

    public static final String CURRENT_GROUP_ID = "CURRENT_GROUP_ID";
    public static final String CURRENT_GROUP_NAME = "CURRENT_GROUP_NAME";
    public static final String CURRENT_GROUP_AVATAR_IMG_PATH = "CURRENT_GROUP_AVATAR_IMG_PATH";

    public static Context mContext;
    public static DatabaseUtils databaseUtils;
    public static SharedPreferences mSharedPreferences;

    public static final String CURRENT_SETTING_THEME_COLOR = "CURRENT_SETTING_THEME_COLOR";
    public static final String CURRENT_SETTING_THEME_COLOR_ALPHA1 = "CURRENT_SETTING_THEME_COLOR_ALPHA1";
    public static final String CURRENT_SETTING_THEME_COLOR_ALPHA2 = "CURRENT_SETTING_THEME_COLOR_ALPHA2";
    public static int current_theme_setting_color = R.color.color_setting_1;
    public static int current_theme_setting_color_alpha1 = R.color.color_setting_1_alpha1;
    public static int current_theme_setting_color_alpha2 = R.color.color_setting_1_alpha2;
    public static String current_category_avatar_img_filepath = "";

    public final static String ACTION_UPDATE_AVATAR = "ACTION_UPDATE_AVATAR";

    public AppConfig(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.databaseUtils = new DatabaseUtils(context);

        //just run 1 time to add sample data
        int sampleDataAppExisted = getSampleAppData();
        if (sampleDataAppExisted == 0) {
            initDataDB();
            setSampleAppData(1);
        }

        int color = getIntValueToSharePref(CURRENT_SETTING_THEME_COLOR, mContext);
        int color1 = getIntValueToSharePref(CURRENT_SETTING_THEME_COLOR_ALPHA1, mContext);
        int color2 = getIntValueToSharePref(CURRENT_SETTING_THEME_COLOR_ALPHA2, mContext);
        if (color == -1) {
            current_theme_setting_color = R.color.color_setting_1;
            current_theme_setting_color_alpha1 = R.color.color_setting_1_alpha1;
            current_theme_setting_color_alpha2 = R.color.color_setting_1_alpha2;
        } else {
            current_theme_setting_color = color;
            current_theme_setting_color_alpha1 = color1;
            current_theme_setting_color_alpha2 = color2;
        }
    }

    public static int getBackgroundColorSetting() {
        return mContext.getColor(current_theme_setting_color);
    }

    public static int getAlpha1BackgroundColorSetting() {
        return mContext.getColor(current_theme_setting_color_alpha1);
    }

    public static int getAlpha2BackgroundColorSetting() {
        return mContext.getColor(current_theme_setting_color_alpha2);
    }


    public static int getSampleAppData() {
        int sampleDataAppExisted = mSharedPreferences.getInt(AppConfig.SAMPLE_DATA_APP, 0);

        return sampleDataAppExisted;
    }

    public static void setSampleAppData(int i) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(AppConfig.SAMPLE_DATA_APP, 1);
        editor.apply();
    }

    public static UserDetail getUserLogInInfor() {
        int id = mSharedPreferences.getInt(AppConfig.CURRENT_USER_ID, -1);
        String email = mSharedPreferences.getString(AppConfig.CURRENT_USER_EMAIL, null);
        String pass = mSharedPreferences.getString(AppConfig.CURRENT_USER_PASSWORD, null);
        int groupid = mSharedPreferences.getInt(AppConfig.CURRENT_USER_GROUP_ID, -1);
        int avatar = mSharedPreferences.getInt(AppConfig.CURRENT_USER_AVATAR, -1);
        int status = mSharedPreferences.getInt(AppConfig.CURRENT_USER_STATUS, -1);
        int role = mSharedPreferences.getInt(AppConfig.CURRENT_USER_ROLE, -1);

        return new UserDetail(id, email, pass, groupid, avatar, status, role);
    }

    public static GroupDetail getCurrentGroup() {
        int id = mSharedPreferences.getInt(AppConfig.CURRENT_GROUP_ID, -1);
        String name = mSharedPreferences.getString(AppConfig.CURRENT_GROUP_NAME, null);
        String path = mSharedPreferences.getString(AppConfig.CURRENT_GROUP_AVATAR_IMG_PATH, null);

        return new GroupDetail(id, name, path);
    }

    public static void saveUserInfoToSharePreference(UserDetail user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (user != null) {
            editor.putInt(AppConfig.CURRENT_USER_ID, user.getUserId());
            editor.putString(AppConfig.CURRENT_USER_EMAIL, user.getUserEmail());
            editor.putString(AppConfig.CURRENT_USER_PASSWORD, user.getUserPassword());
            editor.putInt(AppConfig.CURRENT_USER_GROUP_ID, user.getUserGroupId());
            editor.putInt(AppConfig.CURRENT_USER_AVATAR, user.getUserAvatar());
            editor.putInt(AppConfig.CURRENT_USER_STATUS, user.getUserStatus());
            editor.putInt(AppConfig.CURRENT_USER_ROLE, user.getUserRole());

        } else {
            editor.putInt(AppConfig.CURRENT_USER_ID, -1);
            editor.putString(AppConfig.CURRENT_USER_EMAIL, null);
            editor.putString(AppConfig.CURRENT_USER_PASSWORD, null);
            editor.putInt(AppConfig.CURRENT_USER_GROUP_ID, -1);
            editor.putInt(AppConfig.CURRENT_USER_AVATAR, -1);
            editor.putInt(AppConfig.CURRENT_USER_STATUS, -1);
            editor.putInt(AppConfig.CURRENT_USER_ROLE, -1);
        }

        editor.commit();
    }

    public static void saveGroupInfoToSharePreference(GroupDetail group) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (group != null) {
            editor.putInt(AppConfig.CURRENT_GROUP_ID, group.getGroupId());
            editor.putString(AppConfig.CURRENT_GROUP_NAME, group.getGroupName());
            editor.putString(AppConfig.CURRENT_GROUP_AVATAR_IMG_PATH, group.getGroupAvatarImgPath());
        } else {
            editor.putInt(AppConfig.CURRENT_GROUP_ID, -1);
            editor.putString(AppConfig.CURRENT_GROUP_NAME, null);
            editor.putString(AppConfig.CURRENT_GROUP_AVATAR_IMG_PATH, null);
        }

        editor.commit();
    }

    public static void setIntValueToSharePref(int value, String key, Context context) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValueToSharePref(String key, Context context) {
        int value = mSharedPreferences.getInt(key, -1);
        return value;
    }

    public static void setStringValueToSharePref(String value, String key, Context context) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringValueToSharePref(String key, Context context) {
        String value = mSharedPreferences.getString(key, "");
        return value;
    }

    public static void setBooleanValueToSharePref(boolean value, String key, Context context) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanValueToSharePref(String key, Context context) {
        boolean value = mSharedPreferences.getBoolean(key, false);
        return value;
    }

    public static boolean checkUserLoggedIn() {
        String email = mSharedPreferences.getString(AppConfig.CURRENT_USER_EMAIL, null);
        if (email == null) {
            return false;
        }

        return true;
    }

    public void initDataDB() {
        databaseUtils.open();
        databaseUtils.createTables();
        databaseUtils.close();
    }

    public static void deleteDB() {
        FFMApplication ffmApplication = new FFMApplication();
        ffmApplication.deleteDB();
    }

    public static int getThemeColor() {
        return mContext.getColor(current_theme_setting_color);
    }

    public static void changeRoundViewColor(View v) {
        StateListDrawable drawable1 = (StateListDrawable) v.getBackground();
        DrawableContainer.DrawableContainerState dcs1 = (DrawableContainer.DrawableContainerState) drawable1.getConstantState();
        Drawable[] drawableItems1 = dcs1.getChildren();
        GradientDrawable gradientDrawableChecked1 = (GradientDrawable) drawableItems1[0];
        gradientDrawableChecked1.setStroke(1, AppConfig.getThemeColor());
    }

    public static void changeRoundViewColor(View v, boolean includeState) {
        StateListDrawable drawable1 = (StateListDrawable) v.getBackground();
        DrawableContainer.DrawableContainerState dcs1 = (DrawableContainer.DrawableContainerState) drawable1.getConstantState();
        Drawable[] drawableItems1 = dcs1.getChildren();
        GradientDrawable gradientDrawableChecked1 = (GradientDrawable) drawableItems1[0];
        GradientDrawable gradientDrawableUnChecked1 = (GradientDrawable) drawableItems1[1];
        gradientDrawableChecked1.setStroke(1, AppConfig.getThemeColor());
        gradientDrawableUnChecked1.setStroke(1, AppConfig.getThemeColor());
    }

    public static int getCountReportEachCat(CategoryDetail r) {
        CategoryDetailControl control = new CategoryDetailControl(mContext);
        return control.getCountReportEachCat(r.getCatId());
    }

    public static String getCacheDirExternal() {
        File dir = mContext.getCacheDir();
        String dirPath = dir.getAbsolutePath();
        ;

//        if (dir == null) {
//            Log.e("PathInfo", "External cache directory is not available now");
//        } else if (dir.exists()) {
//            dirPath = dir.getAbsolutePath();
//        } else {
//            if (dir.mkdir()) {
//                Log.e("PathInfo", "Failed to create external cache directory");
//            } else {
//                dirPath = dir.getAbsolutePath();
//            }
//        }

        return dirPath;
    }

    public static boolean isGooglePhotosAvailable() {
        boolean isValid = false;

        PackageManager pm = mContext.getPackageManager();
        try {
            if (pm != null) {
                String versionName = pm.getPackageInfo("com.google.android.apps.photos", PackageManager.GET_SERVICES).versionName;

                if (versionName.compareTo("1.20") > 0) {
                    isValid = true;
                }
            } else {
                isValid = false;
            }

        } catch (PackageManager.NameNotFoundException e) {
            isValid = false;
        }
        return isValid;
    }

    public static Bitmap getBitmap(int resId, BitmapFactory.Options opts) {
        return BitmapFactory.decodeResource(mContext.getResources(), resId, opts);
    }
}
