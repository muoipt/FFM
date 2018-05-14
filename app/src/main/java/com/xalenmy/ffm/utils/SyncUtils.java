package com.xalenmy.ffm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.LogOutCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.xalenmy.ffm.control.CategoryDetailControl;
import com.xalenmy.ffm.control.GroupDetailControl;
import com.xalenmy.ffm.control.ReportDetailControl;
import com.xalenmy.ffm.control.TypeDetaillControl;
import com.xalenmy.ffm.control.UserDetailControl;
import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.UserDetail;

import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class SyncUtils {
    private static Context context;
    public static final String CHANGE_GROUP_ADD_IN_DB_FROM_SERVER = "ADD_GROUP_IN_DB";
    public static final String CHANGE_GROUP_ADD_IN_SERVER_FROM_DB = "ADD_GROUP_IN_SERVER";
    public static final String CHANGE_GROUP_UPDATE_IN_DB_FROM_SERVER = "UPDATE_GROUP_IN_DB";
    public static final String CHANGE_GROUP_UPDATE_IN_SERVER_FROM_DB = "UPDATE_GROUP_IN_SERVER";

    public static final String CHANGE_USER_ADD_IN_DB_FROM_SERVER = "ADD_USER_IN_DB";
    public static final String CHANGE_USER_ADD_IN_SERVER_FROM_DB = "ADD_USER_IN_SERVER";
    public static final String CHANGE_USER_UPDATE_IN_DB_FROM_SERVER = "UPDATE_USER_IN_DB";
    public static final String CHANGE_USER_UPDATE_IN_SERVER_FROM_DB = "UPDATE_USER_IN_SERVER";

    public static final String CHANGE_TYPE_ADD_IN_DB_FROM_SERVER = "ADD_TYPE_IN_DB";
    public static final String CHANGE_TYPE_ADD_IN_SERVER_FROM_DB = "ADD_TYPE_IN_SERVER";
    public static final String CHANGE_TYPE_UPDATE_IN_DB_FROM_SERVER = "UPDATE_TYPE_IN_DB";
    public static final String CHANGE_TYPE_UPDATE_IN_SERVER_FROM_DB = "UPDATE_TYPE_IN_SERVER";

    public static final String CHANGE_CAT_ADD_IN_DB_FROM_SERVER = "ADD_CAT_IN_DB";
    public static final String CHANGE_CAT_ADD_IN_SERVER_FROM_DB = "ADD_CAT_IN_SERVER";
    public static final String CHANGE_CAT_UPDATE_IN_DB_FROM_SERVER = "UPDATE_CAT_IN_DB";
    public static final String CHANGE_CAT_UPDATE_IN_SERVER_FROM_DB = "UPDATE_CAT_IN_SERVER";

    public static final String CHANGE_REPORT_ADD_IN_DB_FROM_SERVER = "ADD_REPORT_IN_DB";
    public static final String CHANGE_REPORT_ADD_IN_SERVER_FROM_DB = "ADD_REPORT_IN_SERVER";
    public static final String CHANGE_REPORT_UPDATE_IN_DB_FROM_SERVER = "UPDATE_REPORT_IN_DB";
    public static final String CHANGE_REPORT_UPDATE_IN_SERVER_FROM_DB = "UPDATE_REPORT_IN_SERVER";

    public SyncUtils(Context context) {
        this.context = context;
    }

    public static boolean userLogInToParseServer() {

        final boolean[] res = {true};

        UserDetail user = AppConfig.getUserLogInInfor();
        try {
            ParseUser.logIn(user.getUserEmail(), user.getUserPassword());
            Log.i("userLogInToParseServer", "sucessfull");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("userLogInToParseServer", "failed");
            res[0] = false;
        }

        return res[0];
    }

    public static boolean userLogInToParseServer(String email, String password) {

        final boolean[] res = {true};
        try {
            ParseUser.logIn(email, password);
            Log.i("userLogInToParseServer", "sucessfull");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("userLogInToParseServer", "failed");
            res[0] = false;
        }

        return res[0];
    }

    public static void logOut() {
        ParseUser.logOutInBackground((new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("logOutInBackground", "sucessful");
                } else {
                    Log.i("logOutInBackground", "failed error : " + e.toString());
                }
            }
        }));
    }

    public void setIdDataTable() {
        //auto log in to server to check value
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            connectAutoUserToParseServer();
        }

        ParseObject idData = new ParseObject(DatabaseUtils.TABLE_ID_DATA);

        idData.put(DatabaseUtils.COLUMN_ID_DATA_ID, 1);
        idData.put(DatabaseUtils.COLUMN_TYPE_ID_DATA, 1);
        idData.put(DatabaseUtils.COLUMN_CATEGORY_ID_DATA, 1);
        idData.put(DatabaseUtils.COLUMN_REPORT_ID_DATA, 1);
        idData.put(DatabaseUtils.COLUMN_GROUP_ID_DATA, 1);
        idData.put(DatabaseUtils.COLUMN_USER_ID_DATA, 1);
        try {
            idData.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIdDataTableExist() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_ID_DATA);
        query.whereEqualTo(DatabaseUtils.COLUMN_ID_DATA_ID, 1);

        try {
            List<ParseObject> idData = query.find();
            if (idData.size() > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getIdInServer(String columnName) {
        int id = 1; //default

//        SyncUtils.connectAutoUserToParseServer();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_ID_DATA);
        query.whereEqualTo(DatabaseUtils.COLUMN_ID_DATA_ID, 1);

        try {
            ParseObject idData = query.getFirst();
            if (idData != null) {
                id = idData.getInt(columnName);
                idData.put(columnName, id + 1);

                idData.save();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
//            ParseUser.logOut();
        }

        return id;
    }

    public static void deleteAllData() {
        ParseQuery<ParseObject> query0 = ParseQuery.getQuery(DatabaseUtils.TABLE_ID_DATA);
        try {
            List<ParseObject> objs = query0.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
        try {
            List<ParseObject> objs = query.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        try {
            List<ParseObject> objs = query2.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        try {
            List<ParseObject> objs = query3.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseUser> query5 = ParseUser.getQuery();
        try {
            List<ParseUser> objs = query5.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseSession> query6 = ParseSession.getQuery();
        try {
            List<ParseSession> objs = query6.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
        try {
            List<ParseObject> objs = query4.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //delete from DB
        ReportDetailControl reportDetailControl = new ReportDetailControl(context);
        reportDetailControl.deleteAllReports();
        CategoryDetailControl categoryDetailControl = new CategoryDetailControl(context);
        categoryDetailControl.deleteAllCategories();
        TypeDetaillControl typeDetailControl = new TypeDetaillControl(context);
        typeDetailControl.deleteAllTypes();
        UserDetailControl userControl = new UserDetailControl(context);
        userControl.deleteAllUsers();
        GroupDetailControl groupControl = new GroupDetailControl(context);
        groupControl.deleteAllGroups();
    }

    public static boolean connectAutoUserToParseServer() {

        //for auto login user
        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        return true;
    }

}
