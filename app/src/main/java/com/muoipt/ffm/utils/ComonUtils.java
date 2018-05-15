package com.muoipt.ffm.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.muoipt.ffm.R;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.ReportDetail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ComonUtils {
    public static Context mContext;
    public static final String NEW_CATEGORY_BUNDLE = "NEW_CATEGORY_BUNDLE";
    public static final String NEW_CATEGORY_INTENT = "NEW_CATEGORY_INTENT";
    public static final String NEW_GROUP_BUNDLE = "NEW_GROUP_BUNDLE";
    public static final String NEW_GROUP_INTENT = "NEW_GROUP_INTENT";

    public static final String UPDATE_GROUP_BUNDLE = "UPDATE_GROUP_BUNDLE";
    public static final String UPDATE_GROUP_INTENT = "UPDATE_GROUP_INTENT";
    public static final String UPDATE_GROUP_BUNDLE_RESULT = "UPDATE_GROUP_BUNDLE_RESULT";
    public static final String UPDATE_GROUP_INTENT_RESULT = "UPDATE_GROUP_INTENT_RESULT";

    public static final String NEW_REPORT_BUNDLE = "NEW_REPORT_BUNDLE";
    public static final String NEW_REPORT_INTENT = "NEW_REPORT_INTENT";

    public static final String LOG_IN_BUNDLE = "LOG_IN_BUNDLE";
    public static final String LOG_IN_INTENT = "LOG_IN_INTENT";

    public static final String SIGN_UP_BUNDLE = "SIGN_UP_BUNDLE";
    public static final String SIGN_UP_INTENT = "SIGN_UP_INTENT";

    public static final String UPDATE_REPORT_BUNDLE = "UPDATE_REPORT_BUNDLE";
    public static final String UPDATE_REPORT_INTENT = "UPDATE_REPORT_INTENT";

    public static final String SIGN_UP_ADMIN_BUNDLE = "SIGN_UP_ADMIN_BUNDLE";
    public static final String SIGN_UP_ADMIN_INTENT = "SIGN_UP_ADMIN_INTENT";

    public static final String SIGN_UP_USER_FROM_ADMIN_BUNDLE = "SIGN_UP_USER_FROM_ADMIN_BUNDLE";
    public static final String SIGN_UP_USER_FROM_ADMIN_INTENT = "SIGN_UP_USER_FROM_ADMIN_INTENT";

    public static final String OPEN_USER_PROFILE_BUNDLE = "OPEN_USER_PROFILE_BUNDLE";
    public static final String OPEN_USER_PROFILE_INTENT = "OPEN_USER_PROFILE_INTENT";

    public static final String OPEN_CAT_SEARCH_BUNDLE = "OPEN_CAT_SEARCH_BUNDLE";
    public static final String OPEN_CAT_SEARCH_INTENT = "OPEN_CAT_SEARCH_INTENT";
    public static final String OPEN_CAT_SEARCH_BUNDLE_RESULT = "OPEN_CAT_SEARCH_BUNDLE_RESULT";
    public static final String OPEN_CAT_SEARCH_INTENT_RESULT = "OPEN_CAT_SEARCH_INTENT_RESULT";

    public static final String OPEN_CAT_MAIN_BUNDLE = "OPEN_CAT_MAIN_BUNDLE";
    public static final String OPEN_CAT_MAIN_INTENT = "OPEN_CAT_MAIN_INTENT";
    public static final String OPEN_CAT_MAIN_BUNDLE_RESULT = "OPEN_CAT_MAIN_BUNDLE_RESULT";
    public static final String OPEN_CAT_MAIN_INTENT_RESULT = "OPEN_CAT_MAIN_INTENT_RESULT";

    public static final String OPEN_REPORT_SEARCH_BUNDLE = "OPEN_REPORT_SEARCH_BUNDLE";
    public static final String OPEN_REPORT_SEARCH_INTENT = "OPEN_REPORT_SEARCH_INTENT";
    public static final String OPEN_REPORT_SEARCH_BUNDLE_RESULT = "OPEN_REPORT_SEARCH_BUNDLE_RESULT";
    public static final String OPEN_REPORT_SEARCH_INTENT_RESULT = "OPEN_REPORT_SEARCH_INTENT_RESULT";

    public static final String AVATAR_BUNDLE_RESULT = "AVATAR_BUNDLE_RESULT";
    public static final String AVATAR_INTENT_RESULT = "AVATAR_INTENT_RESULT";


    public static final String SYNC_ACTION = "SYNC_ACTION";
    public static final String SYNC_RESULT = "SYNC_RESULT";

    public static final String EDIT_CAT_MSG = "EDIT_CAT_MSG";
    public static final String EDIT_USER_MSG = "EDIT_USER_MSG";
    public static final String EDIT_REPORT_MSG = "EDIT_REPORT_MSG";

    public static final String SYNC_ALWAYS_SETTING = "SYNC_ALWAYS_SETTING";

    public static final String ACTION_DELETE_REPORT = "ACTION_DELETE_REPORT";
    public static final String ACTION_DELETE_CATEGORY = "ACTION_DELETE_CATEGORY";
    public static final String ACTION_REFRESH_CAT_LIST = "ACTION_REFRESH_CAT_LIST";
    public static final String ACTION_REFRESH_REPORT_LIST = "ACTION_REFRESH_REPORT_LIST";
    public static final String ACTION_REFRESH_IMPORTANT_CAT = "ACTION_REFRESH_IMPORTANT_CAT";
    public static final String ACTION_REFRESH_ALL = "ACTION_REFRESH_ALL";
    public static final String ACTION_ADD_GROUP_FROM_MAIN = "ACTION_ADD_GROUP_FROM_MAIN";
    public static final String ACTION_ADD_GROUP_FROM_GROUP_LIST = "ACTION_ADD_GROUP_FROM_GROUP_LIST";
    public static final String ACTION_ADD_USER_FROM_SETTING = "ACTION_ADD_USER_FROM_SETTING";
    public static final String ACTION_SHOW_REPORT = "action_show_report";
    public static final String ACTION_OPEN_PROFILE_USER = "ACTION_OPEN_PROFILE_USER";
    public static final String ACTION_APPROVE_USER = "ACTION_APPROVE_USER";
    public static final String ACTION_REJECT_USER = "ACTION_REJECT_USER";
    public static final String ACTION_REMOVE_USER = "ACTION_REMOVE_USER";
    public static final String ACTION_AVATAR_FROM_GROUP = "ACTION_AVATAR_FROM_GROUP";
    public static final String ACTION_AVATAR_FROM_USER = "ACTION_AVATAR_FROM_USER";
    public static final String ACTION_AVATAR_FROM_CATEGORY = "ACTION_AVATAR_FROM_CATEGORY";
    public static final String ACTION_AVATAR_FROM_MAIN = "ACTION_AVATAR_FROM_MAIN";
    public static final String ACTION_AVATAR_FROM_GROUP_LIST = "ACTION_AVATAR_FROM_GROUP_LIST";

    public final static int CODE_SELECT_AVATAR_FROM_GALLERY = 10000;

    public static final int CODE_LOG_IN = 1;
    public static final int CODE_SIGN_UP = 2;
    public static final int CODE_ADD_GROUP = 3;
    public static final int CODE_ADD_CATEGORY = 4;
    public static final int CODE_UPDATE_CATEGORY = 5;
    public static final int CODE_ADD_REPORT = 6;
    public static final int CODE_UPDATE_REPORT = 7;
    public static final int CODE_UPDATE_REPORT_RESULT = 8;
    public static final int CODE_FORGOT_PASSWORD = 9;
    public static final int CODE_ADD_MEMBER_BY_ADMIN = 10;
    public static final int CODE_EDIT_CAT_SEARCH = 11;
    public static final int CODE_EDIT_CAT_MAIN = 12;
    public static final int CODE_EDIT_REPORT_SEARCH = 14;
    public static final int CODE_SELECT_AVATAR_FROM_GROUP = 15;
    public static final int CODE_SELECT_AVATAR_FROM_CATEGORY = 16;
    public static final int CODE_SELECT_AVATAR_FROM_MAIN = 17;
    public static final int CODE_SELECT_AVATAR_FROM_USER = 18;
    public static final int CODE_SELECT_AVATAR_FROM_GROUP_LIST = 19;
    public static final int CODE_UPDATE_GROUP = 20;
    public static final int CODE_ADD_GROUP_FROM_MAIN = 21;
    public static final int CODE_ADD_GROUP_FROM_GROUP_LIST = 22;
    public static final int CODE_ADD_ADMIN = 23;
    public static final int CODE_UPDATE_ADMIN = 24;
    public static final int CODE_VIEW_LIST_MEMBER = 25;

    public static List<Integer> deletedCategories = new ArrayList<Integer>();
    public static List<Integer> deletedReports = new ArrayList<Integer>();

    public static final int USER_STATUS_NEW = 0;
    public static final int USER_STATUS_NORMAL = 1;
    public static final int USER_STATUS_REJECT = 2;
    public static final int USER_STATUS_REMOVED = 3;

    public static final int USER_ROLE_NORMAL = 0;
    public static final int USER_ROLE_ADMIN = 1;

    public static final String DISPLAY_COLOR_DIALOG = "DISPLAY_COLOR_DIALOG";


    public enum SIGN_UP_RESULT {
        SUCCESS,
        FAILED_EMAIL_EXIST,
        FAILED_GROUP_NOT_EXIST,
        FAILED_UPDATE_NOT_CHANGED,
        FAILED_COMMON
    }

    public enum ADD_GROUP_RESULT {
        SUCCESS,
        FAILED_GROUP_EXISTED,
        FAILED_COMMON
    }

    public ComonUtils(Context context) {
        this.mContext = context;
    }

    public static boolean isValidEmail(String email) {
        EmailValidator validator = new EmailValidator();
        return validator.validateEmail(email);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public static int getIndexFromName(ArrayList<String> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(name)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean checkItemExistInList(String item, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkItemExistInList(ReportDetail report, ArrayList<ReportDetail> reportDetailsUpdateList) {
        for (int i = 0; i < reportDetailsUpdateList.size(); i++) {
            if (reportDetailsUpdateList.get(i).getReportId() == report.getReportId()) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkItemExistInList(CategoryDetail cat, ArrayList<CategoryDetail> catDetailsUpdateList) {
        for (int i = 0; i < catDetailsUpdateList.size(); i++) {
            if (catDetailsUpdateList.get(i).getCatId() == cat.getCatId()) {
                return true;
            }
        }

        return false;
    }

    public static boolean alreadyChoosen(Integer integerObj, List<Integer> deletedArr) {
        for (Integer i : deletedArr) {
            if (i.intValue() == integerObj.intValue()) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentStrDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getCurrentStrDatetimeFormatMMMdd() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getDisplayStrDatetimeFormatMMMdd(String dateStr) {
        String date2 = "";

        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            DateFormat df = new SimpleDateFormat("MMM dd");

            date2 = df.format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2;
    }

    public static String getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return "w" + week;
    }

    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return (new SimpleDateFormat("MMM").format(cal.getTime()));
    }

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return (new SimpleDateFormat("yyyy").format(cal.getTime()));
    }

    public static String getFormatDatetime(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(d);
        return formatDate;
    }

    public static int getCurrentMonthInteger() {
        String monthStr = getCurrentMonth();

        Date date = null;
        try {
            date = new SimpleDateFormat("MMMM").parse(monthStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static ArrayList<String> getListMonthStr() {
        ArrayList<String> mMonths = new ArrayList<String>();

//        mMonths.add("");
        mMonths.add(mContext.getString(R.string.jan));
        mMonths.add(mContext.getString(R.string.feb));
        mMonths.add(mContext.getString(R.string.mar));
        mMonths.add(mContext.getString(R.string.apr));
        mMonths.add(mContext.getString(R.string.may));
        mMonths.add(mContext.getString(R.string.jun));
        mMonths.add(mContext.getString(R.string.jul));
        mMonths.add(mContext.getString(R.string.aug));
        mMonths.add(mContext.getString(R.string.sep));
        mMonths.add(mContext.getString(R.string.oct));
        mMonths.add(mContext.getString(R.string.nov));
        mMonths.add(mContext.getString(R.string.dec));
//        mMonths.add("");

        return mMonths;
    }

    public static ArrayList<String> getListMonthString() {
        ArrayList<String> mMonths = new ArrayList<String>();

//        mMonths.add("");
        mMonths.add("01");
        mMonths.add("02");
        mMonths.add("03");
        mMonths.add("04");
        mMonths.add("05");
        mMonths.add("06");
        mMonths.add("07");
        mMonths.add("08");
        mMonths.add("09");
        mMonths.add("10");
        mMonths.add("11");
        mMonths.add("12");
//        mMonths.add("");

        return mMonths;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        float MAX_IMAGE_DIMENSION = context.getResources().getDimension(R.dimen.dp_120);
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public static String createNewCacheFileName() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(date) + "." + Bitmap.CompressFormat.JPEG.toString();
    }

    public static ParseFile saveBitmapToFile(Bitmap b, String path) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);// can use something 70 in case u want to compress the image

        byte[] scaledData = bos.toByteArray();

        String[] strs = path.split("/");
        int len = strs.length;
        String fileName = strs[len - 1];

        // Save the scaled image to Parse
        final ParseFile photoFile = new ParseFile(fileName, scaledData);
        photoFile.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                if (e != null) {
                    Log.i("Save image file", "failed");
                } else {
                    Log.i("Save image file", "success");
                }
            }
        });

        return photoFile;
    }
}
