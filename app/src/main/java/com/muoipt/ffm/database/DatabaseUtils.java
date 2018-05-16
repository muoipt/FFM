package com.muoipt.ffm.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.model.ImportantCatListItem;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.ReportDetail;
import com.muoipt.ffm.model.ReportDetailDisplay;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.ComonUtils;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class DatabaseUtils extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FFM.db";

    public static final String TABLE_CATEGORY_DETAIL = "CategoryDetailTbl";
    public static final String COLUMN_CATEGORY_ID = "categoryID";
    public static final String COLUMN_CATEGORY_NAME = "categoryName";
    public static final String COLUMN_CATEGORY_TYPE_ID = "categoryTypeId";
    public static final String COLUMN_CATEGORY_LIMIT = "categoryLimit";
    public static final String COLUMN_CATEGORY_STATUS = "categoryStatus";
    public static final String COLUMN_CATEGORY_AVATAR = "categoryAvatar";
    public static final String COLUMN_CATEGORY_AVATAR_IMG_PATH = "categoryAvatarImgPath";
    public static final String COLUMN_CATEGORY_UPDATE_AT = "categoryUpdateAt";
    public static final String COLUMN_CATEGORY_DELETED = "categoryDeleted";
    public static final String COLUMN_CATEGORY_MARK_IMPORTAN = "categoryMarkImportant";

    public static final String TABLE_REPORT_DETAIL = "ReportDetailTbl";
    public static final String COLUMN_REPORT_ID = "reportId";
    public static final String COLUMN_REPORT_DATETIME = "reportDateTime";
    public static final String COLUMN_REPORT_CAT_ID = "reportCategoryId";
    public static final String COLUMN_REPORT_AMOUNT = "reportAmount";
    public static final String COLUMN_REPORT_TITLE = "reportTitle";
    public static final String COLUMN_REPORT_NOTE = "reportNote";
    public static final String COLUMN_REPORT_UPDATE_AT = "reportUpdateAt";
    public static final String COLUMN_REPORT_DELETED = "reportDeleted";
    public static final String COLUMN_REPORT_MARK_IMPORTANT = "reportMarkImportant";

    public static final String TABLE_TYPE_DETAIL = "TypeDetailTbl";
    public static final String COLUMN_TYPE_ID = "typeId";
    public static final String COLUMN_TYPE_NAME = "typeName";
    public static final String COLUMN_TYPE_GROUP_ID = "typeGroupId";

    public static final String TABLE_GROUP = "GroupTbl";
    public static final String COLUMN_GROUP_ID = "groupId";
    public static final String COLUMN_GROUP_NAME = "groupName";
    public static final String COLUMN_GROUP_AVATAR = "groupAvatar";
    public static final String COLUMN_GROUP_AVATAR_IMG_PATH = "groupAvatarImgPath";
    public static final String GROUP_AVATAR_IMG_FILE_SERVER = "groupAvatarImgFile";

    public static final String TABLE_USER = "UserTbl";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_EMAIL = "userEmail";
    public static final String COLUMN_USER_PASSWORD = "userPassword";
    public static final String COLUMN_USER_GROUP_ID = "userGroupId";
    public static final String COLUMN_USER_AVATAR = "userAvatar";
    public static final String COLUMN_USER_AVATAR_IMG_PATH = "userAvatarImgPath";
    public static final String COLUMN_USER_STATUS = "userStatus";
    public static final String COLUMN_USER_ROLE = "userRole";
    public static final String USER_AVATAR_IMG_FILE_SERVER = "userAvatarImgFile";

    public static final String TABLE_ID_DATA = "IdDataTbl";
    public static final String COLUMN_ID_DATA_ID = "idData";
    public static final String COLUMN_TYPE_ID_DATA = "typeId";
    public static final String COLUMN_CATEGORY_ID_DATA = "categoryId";
    public static final String COLUMN_REPORT_ID_DATA = "reportId";
    public static final String COLUMN_GROUP_ID_DATA = "groupId";
    public static final String COLUMN_USER_ID_DATA = "userId";

    public static final String COLUMN_REPORT_AMOUNT_EACH_TOTAL = "Amount";

    public static final String CREATE_TABLE_TYPE_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TYPE_DETAIL + "(" +
                    COLUMN_TYPE_ID + " INTEGER PRIMARY KEY " +
                    "," + COLUMN_TYPE_NAME + " TEXT NOT NULL" +
                    "," + COLUMN_TYPE_GROUP_ID + " INTEGER" +
                    "," + "FOREIGN KEY (" + COLUMN_TYPE_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(" + COLUMN_GROUP_ID + ") ON UPDATE SET NULL" +
                    ");";

    public static final String DROP_TABLE_TYPE_DETAIL =
            "DROP TABLE IF EXISTS " + TABLE_TYPE_DETAIL + ";";

    public static final String CREATE_TABLE_CATEGORY_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_DETAIL + "(" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY " +
                    "," + COLUMN_CATEGORY_NAME + " TEXT NOT NULL" +
                    "," + COLUMN_CATEGORY_TYPE_ID + " INTEGER " +
                    "," + COLUMN_CATEGORY_LIMIT + " INTEGER NOT NULL" +
                    "," + COLUMN_CATEGORY_STATUS + " INTEGER NOT NULL" +
                    "," + COLUMN_CATEGORY_AVATAR + " INTEGER " +
                    "," + COLUMN_CATEGORY_AVATAR_IMG_PATH + " TEXT " +
                    "," + COLUMN_CATEGORY_UPDATE_AT + " TEXT NOT NULL" +
                    "," + COLUMN_CATEGORY_DELETED + " INTEGER NOT NULL" +
                    "," + COLUMN_CATEGORY_MARK_IMPORTAN + " INTEGER" +
                    "," + "FOREIGN KEY (" + COLUMN_CATEGORY_TYPE_ID + ") REFERENCES " + TABLE_TYPE_DETAIL + "(" + COLUMN_TYPE_ID + ") ON UPDATE SET NULL" +
                    ");";

    public static final String DROP_TABLE_CATEGORY_DETAIL =
            "DROP TABLE IF EXISTS " + TABLE_CATEGORY_DETAIL + ";";

    public static final String CREATE_TABLE_REPORT_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_REPORT_DETAIL + "(" +
                    COLUMN_REPORT_ID + " INTEGER PRIMARY KEY " +
                    "," + COLUMN_REPORT_DATETIME + " DATE NOT NULL" +
                    "," + COLUMN_REPORT_CAT_ID + " INTEGER NOT NULL " +
                    "," + COLUMN_REPORT_AMOUNT + " INTEGER NOT NULL" +
                    "," + COLUMN_REPORT_TITLE + " TEXT" +
                    "," + COLUMN_REPORT_NOTE + " TEXT" +
                    "," + COLUMN_REPORT_UPDATE_AT + " TEXT NOT NULL" +
                    "," + COLUMN_REPORT_DELETED + " INTEGER NOT NULL" +
                    "," + COLUMN_REPORT_MARK_IMPORTANT + " INTEGER" +
                    "," + "FOREIGN KEY (" + COLUMN_REPORT_CAT_ID + ") REFERENCES " + TABLE_CATEGORY_DETAIL + "(" + COLUMN_CATEGORY_ID + ") ON UPDATE SET NULL" +
                    ");";

    public static final String DROP_TABLE_REPORT_DETAIL =
            "DROP TABLE IF EXISTS " + TABLE_REPORT_DETAIL + ";";

    public static final String CREATE_TABLE_GROUP =
            "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + "(" +
                    COLUMN_GROUP_ID + " INTEGER PRIMARY KEY " +
                    "," + COLUMN_GROUP_NAME + " TEXT NOT NULL" +
                    "," + COLUMN_GROUP_AVATAR + " INTEGER " +
                    "," + COLUMN_GROUP_AVATAR_IMG_PATH + " TEXT " +
                    ");";
    public static final String DROP_TABLE_GROUP =
            "DROP TABLE IF EXISTS " + TABLE_GROUP + ";";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY " +
                    "," + COLUMN_USER_EMAIL + " TEXT NOT NULL" +
                    "," + COLUMN_USER_PASSWORD + " TEXT NOT NULL" +
                    "," + COLUMN_USER_GROUP_ID + " INTEGER NOT NULL" +
                    "," + COLUMN_USER_AVATAR + " INTEGER " +
                    "," + COLUMN_USER_AVATAR_IMG_PATH + " TEXT " +
                    "," + COLUMN_USER_STATUS + " INTEGER NOT NULL " +
                    "," + COLUMN_USER_ROLE + " INTEGER NOT NULL " +
                    "," + "FOREIGN KEY (" + COLUMN_USER_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(" + COLUMN_GROUP_ID + ") ON UPDATE SET NULL" +
                    ");";

    public static final String DROP_TABLE_USER =
            "DROP TABLE IF EXISTS " + TABLE_USER + ";";


    public static final String CHECK_TABLE_EXIST =
            "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ";

    public static final String SELECT_ALL_TYPE_DETAIL =
            "SELECT * FROM " + TABLE_TYPE_DETAIL + ";";
    public static final String SELECT_ALL_CATEGORY_DETAIL =
            "SELECT * FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_DELETED + " = 0";
    public static final String SELECT_ALL_REPORT_DETAIL =
            "SELECT * FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0";
    public static final String SELECT_ALL_GROUP =
            "SELECT * FROM " + TABLE_GROUP + ";";
    public static final String SELECT_ALL_USER =
            "SELECT * FROM " + TABLE_USER + ";";

    public static final String SELECT_TOTAL_AMOUNT_OUTCOME =
            "SELECT SUM(" + COLUMN_REPORT_AMOUNT + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + " FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0 AND "
                    + COLUMN_REPORT_CAT_ID + " IN (SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_TYPE_ID + " = 2 AND " + COLUMN_CATEGORY_DELETED + " = 0 );";
    public static final String SELECT_TOTAL_AMOUNT_INCOME =
            "SELECT SUM (" + COLUMN_REPORT_AMOUNT + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + " FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0 AND "
                    + COLUMN_REPORT_CAT_ID + " IN (SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_TYPE_ID + " = 1 AND " + COLUMN_CATEGORY_DELETED + " = 0 );";
    public static final String GET_LIST_MAIN_SUMMARY_REPORT =
            "SELECT " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0 AND "
                    + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                    + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + ";";


    public static final String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY =
            "SELECT " + COLUMN_REPORT_ID + " , " + COLUMN_REPORT_DATETIME + " , (SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE "
                    + COLUMN_CATEGORY_DELETED + " = 0 AND "
                    + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID + " = " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                    + " ) AS " + COLUMN_CATEGORY_NAME
                    + " , " + COLUMN_REPORT_TITLE + " , " + COLUMN_REPORT_AMOUNT + " , " + COLUMN_REPORT_NOTE + " FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0; ";
    //SELECT reportDateTime, (SELECT categoryName from [CategoryDetail] WHERE [CategoryDetail].[categoryId] = [ReportDetail].[reportCategoryId]) as reportCategory,
    // reportTitle, reportAmount, reportNote from [ReportDetail];

    public static final String GET_LIST_MAIN_SUMMARY_REPORT_ORDERBY_1 =
            "SELECT " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE "
                    + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                    + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + ";";


    /**
     * value for update database
     */
    public static final int DATA_VERSION = 1;

    /**
     * Sqlite database
     */
    private SQLiteDatabase db;

    public DatabaseUtils(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);

    }

    /**
     * create db when app start, and only call when database don't create
     * When database created, it will not call
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * call when change DATA_VERSION
     * help we update database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db = getWritableDatabase();
            dropTables(); //drop all tables

            //then create new ones
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * open database
     */
    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close database
     */
    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /************************* method work with database *******************/

    public void createTables() {
        createTableGroup();
        createTableUser();
        createTableTypeDetail();
        createTableCategoryDetail();
        createTableReportDetail();
    }

    public boolean dropTables() {
        try {
            db.execSQL(DROP_TABLE_GROUP);
            db.execSQL(DROP_TABLE_TYPE_DETAIL);
            db.execSQL(DROP_TABLE_USER);
            db.execSQL(DROP_TABLE_CATEGORY_DETAIL);
            db.execSQL(DROP_TABLE_REPORT_DETAIL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * get all row of table with sql command then return cursor
     * cursor move to frist to redy for get data
     */
    public Cursor getAll(String sql) {
        open();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }

    public boolean createTableTypeDetail() {
        try {
            db.execSQL(CREATE_TABLE_TYPE_DETAIL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistTableTypeDetail() {
        open();

        Cursor cursor = db.rawQuery(CHECK_TABLE_EXIST + " '" + TABLE_TYPE_DETAIL + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    public boolean createTableCategoryDetail() {
        try {
            db.execSQL(CREATE_TABLE_CATEGORY_DETAIL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistTableCategoryDetail() {
        open();

        Cursor cursor = db.rawQuery(CHECK_TABLE_EXIST + " '" + TABLE_CATEGORY_DETAIL + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    public boolean createTableReportDetail() {
        try {
            db.execSQL(CREATE_TABLE_REPORT_DETAIL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistTableReportDetail() {
        open();

        Cursor cursor = db.rawQuery(CHECK_TABLE_EXIST + " '" + TABLE_REPORT_DETAIL + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    public boolean createTableGroup() {
        try {
            db.execSQL(CREATE_TABLE_GROUP);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistTableGroup() {
        open();

        Cursor cursor = db.rawQuery(CHECK_TABLE_EXIST + " '" + TABLE_GROUP + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    public boolean createTableUser() {
        try {
            db.execSQL(CREATE_TABLE_USER);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistTableUser() {
        open();

        Cursor cursor = db.rawQuery(CHECK_TABLE_EXIST + " '" + TABLE_USER + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    public void execQuery(String sql) {
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert contentvaluse to table
     *
     * @param values value of data want insert
     * @return index row insert
     */
    public long insert(String table, ContentValues values) {
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }

    /**
     * update values to table
     *
     * @return index row update
     */
    public boolean update(String table, ContentValues values, String where) {
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }

    /**
     * delete id row of table
     */
    public boolean delete(String table, String where) {
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }

    /************************* method work with CategoryDetailTbl table *******************/
    public CategoryDetail getCategoryDetail(String sql) {
        CategoryDetail categoryDetail = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            categoryDetail = cursorToCategoryDetail(cursor);
            cursor.close();
        }
        return categoryDetail;
    }

    public ArrayList<CategoryDetail> getAllCategoryDetail() {
        ArrayList<CategoryDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_DETAIL);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    //get all cats including deleted and not deleted
    public ArrayList<CategoryDetail> getAllTotalCategoryDetailByUser(UserDetail user) {
        String SELECT_ALL_CATEGORY_DETAIL_BY_USER =
                "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + ");";

        ArrayList<CategoryDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<ImportantCatListItem> getAllMarkedCategoryByUser(UserDetail user) {
        String NUMBER = "CAT_NUMBER";

        String SELECT_ALL_CATEGORY_DETAIL_BY_USER =
                "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL
                        + " WHERE " + COLUMN_CATEGORY_MARK_IMPORTAN + " = 1 AND "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL
                        + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + ");";


        ArrayList<ImportantCatListItem> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
//            int number = cursor.getInt(cursor.getColumnIndex(NUMBER));

            ImportantCatListItem item = new ImportantCatListItem(name);

            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }


    public ArrayList<String> getAllTotalCategoryNameByUser(UserDetail user) {
        String SELECT_ALL_CATEGORY_NAME_BY_USER =
                "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + ");";

        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_NAME_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public int getCountReportEachCat(int catId) {
        int res = 0;

        String SELECT_ALL_CATEGORY_NAME_BY_CAT_ID =
                "SELECT COUNT(" + COLUMN_REPORT_ID + ") AS NUMBER FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_CAT_ID + " = " + catId + ";";

        Cursor cursor = getAll(SELECT_ALL_CATEGORY_NAME_BY_CAT_ID);

        while (!cursor.isAfterLast()) {
            res = (cursor.getInt(cursor.getColumnIndex("NUMBER")));
            cursor.moveToNext();
        }
        cursor.close();

        return res;
    }

    //just get not deleted category
    public ArrayList<CategoryDetail> getAllCategoryDetailByUser(UserDetail user) {

        String SELECT_ALL_CATEGORY_DETAIL_BY_USER =
                "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND " +
                        COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() +
                        ");";

        ArrayList<CategoryDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<CategoryDetail> getCategoryDetailByUserAndName(UserDetail user, String categoryName) {

        String SELECT_ALL_CATEGORY_DETAIL_BY_USER =
                "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND " +
                        COLUMN_CATEGORY_NAME + " = '" + categoryName + "' AND " +
                        COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() +
                        ");";

        ArrayList<CategoryDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_CATEGORY_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<CategoryDetail> getListCategoryDetail(String sql) {
        ArrayList<CategoryDetail> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<CategoryDetail> getCategoryDetailByType(String type) {
        String GET_CATEGORY_BY_TYPE = "SELECT * FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT "
                + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_NAME + " = '" + type + "'" + " )";

        ArrayList<CategoryDetail> list = getListCategoryDetail(GET_CATEGORY_BY_TYPE);
        return list;
    }

    public long insertCategoryDetail(CategoryDetail categoryDetail) {
        return insert(TABLE_CATEGORY_DETAIL, CategoryDetailToValues(categoryDetail));
    }

    public boolean updateCategoryDetail(CategoryDetail categoryDetail) {
        return update(TABLE_CATEGORY_DETAIL, CategoryDetailToValues(categoryDetail), COLUMN_CATEGORY_ID + " = " + categoryDetail.getCatId());
    }

    public boolean deleteCategoryDetail(String where) {
        return delete(TABLE_CATEGORY_DETAIL, where);
    }

    public boolean deleteCategoryDetail(CategoryDetail categoryDetail) {
        //first delete all report detail in category
        ArrayList<ReportDetail> arr = getDetailReportFromDBByCategoryDeleted(categoryDetail);
        for (int i = 0; i < arr.size(); i++) {
            deleteReportDetail(COLUMN_REPORT_ID + " = " + arr.get(i).getReportId());
        }

        //then delete category
        return delete(TABLE_CATEGORY_DETAIL, COLUMN_CATEGORY_ID + " = " + categoryDetail.getCatId());
    }

    public boolean deleteCategoryTemporary(CategoryDetail categoryDetail) {
        //just change column delete = 1 in DB
        categoryDetail.setCatDeleted(true);
        return updateCategoryDetail(categoryDetail);
    }

    public boolean deleteCategoryTemporaryById(int categoryDetailId) {

        String SELECT_CATEGORY_DETAIL_BY_ID =
                "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_ID + " = " + categoryDetailId;

        Cursor cursor = getAll(SELECT_CATEGORY_DETAIL_BY_ID);

        CategoryDetail categoryDetail = null;
        while (!cursor.isAfterLast()) {
            categoryDetail = cursorToCategoryDetail(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        //just change column delete = 1 in DB
        categoryDetail.setCatDeleted(true);
        return updateCategoryDetail(categoryDetail);
    }

    public boolean deleteReportTemporary(ReportDetail reportDetail) {
        //just change column delete = 1 in DB
        reportDetail.setReportDeleted(true);
        return updateReportDetail(reportDetail);
    }

    public boolean deleteReportTemporaryById(ReportDetailDisplay reportDetail) {

        String SELECT_REPORT_BY_ID =
                "SELECT * FROM " + TABLE_REPORT_DETAIL +
                        " WHERE " + COLUMN_REPORT_ID + " = " + reportDetail.getReportId();

        Cursor cursor = getAll(SELECT_REPORT_BY_ID);

        ReportDetail r = null;
        while (!cursor.isAfterLast()) {
            r = cursorToReportDetail(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        if (r != null) {

            r.setReportDeleted(true);
        }
        return updateReportDetail(r);
    }


    private ContentValues CategoryDetailToValues(CategoryDetail categoryDetail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, categoryDetail.getCatId());
        values.put(COLUMN_CATEGORY_NAME, categoryDetail.getCatName());
        values.put(COLUMN_CATEGORY_TYPE_ID, categoryDetail.getCatTypeId());
        values.put(COLUMN_CATEGORY_LIMIT, categoryDetail.getCatLimit());
        values.put(COLUMN_CATEGORY_STATUS, categoryDetail.isCatStatus() == true ? 1 : 0);
        values.put(COLUMN_CATEGORY_AVATAR, categoryDetail.getCatAvatar());
        values.put(COLUMN_CATEGORY_AVATAR_IMG_PATH, categoryDetail.getCatAvatarImagePath());
        values.put(COLUMN_CATEGORY_UPDATE_AT, getUpdateAtDatetime());
        values.put(COLUMN_CATEGORY_DELETED, categoryDetail.isCatDeleted() == true ? 1 : 0);
        values.put(COLUMN_CATEGORY_MARK_IMPORTAN, categoryDetail.isMark() == true ? 1 : 0);
        return values;
    }

    private CategoryDetail cursorToCategoryDetail(Cursor cursor) {
        CategoryDetail categoryDetail = new CategoryDetail();
        categoryDetail.setCatId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
        categoryDetail.setCatName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
        categoryDetail.setCatTypeId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE_ID)));
        categoryDetail.setCatLimit(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_LIMIT)));
        categoryDetail.setCatStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_STATUS)) == 1 ? true : false);
        categoryDetail.setCatAvatar(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_AVATAR)));
        categoryDetail.setCatAvatarImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_AVATAR_IMG_PATH)));
        categoryDetail.setCatUpdatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_UPDATE_AT)));
        categoryDetail.setCatDeleted(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_DELETED)) == 1 ? true : false);
        categoryDetail.setMark(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_MARK_IMPORTAN)) == 1 ? true : false);
        return categoryDetail;
    }


    /************************* method work with TypeDetailTbl table *******************/
    public TypeDetail getTypeDetail(String sql) {
        TypeDetail typeDetail = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            typeDetail = cursorToTypeDetail(cursor);
            cursor.close();
        }
        return typeDetail;
    }

    public ArrayList<TypeDetail> getAllTypeDetail() {
        ArrayList<TypeDetail> list = new ArrayList<TypeDetail>();
        Cursor cursor = getAll(SELECT_ALL_TYPE_DETAIL);

        while (!cursor.isAfterLast()) {
            list.add(cursorToTypeDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<TypeDetail> getAllTypeDetailByUser(UserDetail user) {

        String SELECT_ALL_TYPE_DETAIL_BY_USER =
                "SELECT * FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + ";";

        ArrayList<TypeDetail> list = new ArrayList<TypeDetail>();
        Cursor cursor = getAll(SELECT_ALL_TYPE_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToTypeDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<TypeDetail> getListTypeDetail(String sql) {
        ArrayList<TypeDetail> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            list.add(cursorToTypeDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public long insertTypeDetail(TypeDetail typeDetail) {
        return insert(TABLE_TYPE_DETAIL, TypeDetailToValues(typeDetail));
    }

    public boolean updateTypeDetail(TypeDetail typeDetail) {
        return update(TABLE_TYPE_DETAIL, TypeDetailToValues(typeDetail), COLUMN_TYPE_ID + " = " + typeDetail.getTypeId());
    }

    public boolean deleteTypeDetail(String where) {
        return delete(TABLE_TYPE_DETAIL, where);
    }

    private ContentValues TypeDetailToValues(TypeDetail typeDetail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_ID, typeDetail.getTypeId());
        values.put(COLUMN_TYPE_NAME, typeDetail.getTypeName());
        values.put(COLUMN_TYPE_GROUP_ID, typeDetail.getTypeGroupId());
        return values;
    }

    private TypeDetail cursorToTypeDetail(Cursor cursor) {
        TypeDetail typeDetail = new TypeDetail();
        typeDetail.setTypeId(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE_ID)));
        typeDetail.setTypeName(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)));
        typeDetail.setTypeGroupId(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE_GROUP_ID)));
        return typeDetail;
    }

    /************************* method work with ReportDetailTbl table *******************/
    public ReportDetailDisplay getReportDetail(String sql) {
        ReportDetailDisplay reportDetail = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            reportDetail = cursorToReportDetailDisplay(cursor);
            cursor.close();
        }
        return reportDetail;
    }

    public ArrayList<ReportDetailDisplay> getAllReportDetail() {
        ArrayList<ReportDetailDisplay> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY);

        while (!cursor.isAfterLast()) {
            ReportDetailDisplay reportDetailDisplay = cursorToReportDetailDisplay(cursor);
            if (reportDetailDisplay != null && reportDetailDisplay.getCategoryName() != null) {
                String dateStr = reportDetailDisplay.getReportDatetime();
                String displayDate = ComonUtils.getDisplayStrDatetimeFormatMMMdd(dateStr);
                reportDetailDisplay.setReportDatetime(displayDate);
                list.add(reportDetailDisplay);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }


    public ArrayList<ReportDetail> getListReportDetailWithCatDeleted(String sql) {
        ArrayList<ReportDetail> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            list.add(cursorToReportDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<ReportDetailDisplay> getListReportDetail(String sql) {
        ArrayList<ReportDetailDisplay> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            ReportDetailDisplay reportDetailDisplay = cursorToReportDetailDisplay(cursor);
            if (reportDetailDisplay != null && reportDetailDisplay.getCategoryName() != null) {
                String dateStr = reportDetailDisplay.getReportDatetime();
                String displayDate = ComonUtils.getDisplayStrDatetimeFormatMMMdd(dateStr);
                reportDetailDisplay.setReportDatetime(displayDate);
                list.add(reportDetailDisplay);
            }

            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByType(String selected) {
        ArrayList<ReportDetailDisplay> listResult = new ArrayList<ReportDetailDisplay>();

        String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_TYPE_FILTER =
                "SELECT " + COLUMN_REPORT_ID + " , " + COLUMN_REPORT_DATETIME + " , (SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID + " = " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " AND " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_TYPE_ID +
                        " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + TABLE_TYPE_DETAIL + "." + COLUMN_TYPE_NAME + " = '" + selected + "'" + ") ) AS " + COLUMN_CATEGORY_NAME
                        + " , " + COLUMN_REPORT_TITLE + " , " + COLUMN_REPORT_AMOUNT + " , " + COLUMN_REPORT_NOTE + " FROM " + TABLE_REPORT_DETAIL + ";";


        ArrayList<ReportDetailDisplay> list = getListReportDetail(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_TYPE_FILTER);

        for (int i = 0; i < list.size(); i++) {
            ReportDetailDisplay r = list.get(i);
            if (r.getCategoryName() != null && !r.getCategoryName().equals("") && !r.getCategoryName().isEmpty()) {
                listResult.add(list.get(i));
            }
        }

        return listResult;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByCategoryName(String name, int userGroupId) {
        ArrayList<ReportDetailDisplay> listResult = new ArrayList<ReportDetailDisplay>();

        String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_CAT_FILTER =
                "SELECT " + COLUMN_REPORT_ID + " , " + COLUMN_REPORT_DATETIME + ", " + COLUMN_CATEGORY_NAME + ", " + COLUMN_REPORT_TITLE + ", "
                        + COLUMN_REPORT_AMOUNT + ", " + COLUMN_REPORT_NOTE
                        + " FROM " + TABLE_CATEGORY_DETAIL + " INNER JOIN " + TABLE_REPORT_DETAIL + " INNER JOIN " + TABLE_TYPE_DETAIL
                        + " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_REPORT_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_ID + " = " + COLUMN_REPORT_CAT_ID + " AND "
                        + COLUMN_CATEGORY_NAME + " = '" + name + "' AND "
                        + COLUMN_CATEGORY_TYPE_ID + " = " + COLUMN_TYPE_ID + " AND "
                        + COLUMN_TYPE_GROUP_ID + " = " + userGroupId
                        + " ORDER BY " + COLUMN_REPORT_DATETIME + " DESC; ";

        ArrayList<ReportDetailDisplay> list = getListReportDetail(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_CAT_FILTER);

        for (int i = 0; i < list.size(); i++) {
            ReportDetailDisplay r = list.get(i);
            if (r.getCategoryName() != null && !r.getCategoryName().equals("") && !r.getCategoryName().isEmpty()) {
                listResult.add(list.get(i));
            }
        }

        return listResult;
    }

    public List<TypeDetail> getTypeIdsFromCategoryName(String catName) {
        List<TypeDetail> results = new ArrayList<TypeDetail>();

        String SELECT_QUERY =
                "SELECT * FROM " + TABLE_TYPE_DETAIL
                        + " WHERE " + TABLE_TYPE_DETAIL + "." + COLUMN_TYPE_ID + " = "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_TYPE_ID + " AND "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_NAME + " = '" + catName + "'; ";

        Cursor cursor = getAll(SELECT_QUERY);

        while (!cursor.isAfterLast()) {
            TypeDetail typeDetail = cursorToTypeDetail(cursor);
            results.add(typeDetail);

            cursor.moveToNext();
        }
        cursor.close();

        return results;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByCategoryId(int id) {
        ArrayList<ReportDetailDisplay> listResult = new ArrayList<ReportDetailDisplay>();

        String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_CAT_FILTER =
                "SELECT " + COLUMN_REPORT_ID + " , " + COLUMN_REPORT_DATETIME + " , (SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID + " = " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " AND " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID +
                        " = " + id + " ) AS " + COLUMN_CATEGORY_NAME
                        + " , " + COLUMN_REPORT_TITLE + " , " + COLUMN_REPORT_AMOUNT + " , " + COLUMN_REPORT_NOTE + " FROM " + TABLE_REPORT_DETAIL + ";";


        ArrayList<ReportDetailDisplay> list = getListReportDetail(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_CAT_FILTER);

        for (int i = 0; i < list.size(); i++) {
            ReportDetailDisplay r = list.get(i);
            if (r.getCategoryName() != null && !r.getCategoryName().equals("") && !r.getCategoryName().isEmpty()) {
                listResult.add(list.get(i));
            }
        }

        return listResult;
    }

    public ArrayList<ReportDetail> getDetailReportFromDBByCategoryDeleted(CategoryDetail categoryDetail) {
        String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_DELETED = "SELECT * FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_CAT_ID +
                " IN (SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_DELETED + " = 1 AND " + COLUMN_CATEGORY_ID + " = " + categoryDetail.getCatId() + "); ";

        return getListReportDetailWithCatDeleted(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_DELETED);
    }

    public long insertReportDetail(ReportDetail reportDetail) {
        return insert(TABLE_REPORT_DETAIL, ReportDetailToValues(reportDetail));
    }

    public boolean updateReportDetail(ReportDetail reportDetail) {
        return update(TABLE_REPORT_DETAIL, ReportDetailUpdateToValues(reportDetail), COLUMN_REPORT_ID + " = " + reportDetail.getReportId());
    }

    public boolean deleteReportDetail(String where) {
        return delete(TABLE_REPORT_DETAIL, where);
    }

    private ContentValues ReportDetailToValues(ReportDetail reportDetail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT_ID, reportDetail.getReportId());
        values.put(COLUMN_REPORT_DATETIME, String.valueOf(ComonUtils.getCurrentStrDatetime()));
        values.put(COLUMN_REPORT_CAT_ID, reportDetail.getReportCatId());
        values.put(COLUMN_REPORT_AMOUNT, reportDetail.getReportAmount());
        values.put(COLUMN_REPORT_TITLE, reportDetail.getReportTitle());
        values.put(COLUMN_REPORT_NOTE, reportDetail.getReportNote());
        values.put(COLUMN_REPORT_UPDATE_AT, getUpdateAtDatetime());
        values.put(COLUMN_REPORT_DELETED, reportDetail.isReportDeleted() == true ? 1 : 0);
        values.put(COLUMN_REPORT_MARK_IMPORTANT, reportDetail.isReportMark() == true ? 1 : 0);
        return values;
    }

    private ContentValues ReportDetailUpdateToValues(ReportDetail reportDetail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT_ID, reportDetail.getReportId());
        values.put(COLUMN_REPORT_DATETIME, reportDetail.getReportDatetime());
        values.put(COLUMN_REPORT_CAT_ID, reportDetail.getReportCatId());
        values.put(COLUMN_REPORT_AMOUNT, reportDetail.getReportAmount());
        values.put(COLUMN_REPORT_TITLE, reportDetail.getReportTitle());
        values.put(COLUMN_REPORT_NOTE, reportDetail.getReportNote());
        values.put(COLUMN_REPORT_UPDATE_AT, getUpdateAtDatetime());
        values.put(COLUMN_REPORT_DELETED, reportDetail.isReportDeleted() == true ? 1 : 0);
        values.put(COLUMN_REPORT_MARK_IMPORTANT, reportDetail.isReportMark() == true ? 1 : 0);
        return values;
    }

    private ReportDetailDisplay cursorToReportDetailDisplay(Cursor cursor) {
        ReportDetailDisplay reportDetail = new ReportDetailDisplay();
        reportDetail.setReportId(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_ID)));
        reportDetail.setReportDatetime(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_DATETIME)));
        reportDetail.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
        reportDetail.setReportAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_AMOUNT)));
        reportDetail.setReportTitle(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_TITLE)));
        reportDetail.setReportNote(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_NOTE)));
        return reportDetail;
    }

    private ReportDetail cursorToReportDetail(Cursor cursor) {
        ReportDetail reportDetail = new ReportDetail();
        reportDetail.setReportId(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_ID)));
        reportDetail.setReportDatetime(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_DATETIME)));
        reportDetail.setReportCatId(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_CAT_ID)));
        reportDetail.setReportAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_AMOUNT)));
        reportDetail.setReportTitle(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_TITLE)));
        reportDetail.setReportNote(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_NOTE)));
        reportDetail.setReportUpdatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_REPORT_UPDATE_AT)));
        reportDetail.setReportDeleted(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_DELETED)) == 1 ? true : false);
        reportDetail.setReportMark(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_MARK_IMPORTANT)) == 1 ? true : false);
        return reportDetail;
    }

    /************************* method work with MainSummaryReport *******************/
    //
    public ArrayList<MainSummaryReport> getAllMainSummaryReport() {
        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<MainSummaryReport> getAllMainSummaryReportByUser(UserDetail user) {
        //    GET_LIST_MAIN_SUMMARY_REPORT_BY_USER
        String GET_LIST_MAIN_SUMMARY_REPORT_BY_USER =
                "SELECT " + COLUMN_CATEGORY_ID + ", " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId()
                        + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                        + ") AND " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                        + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + //";";
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_TYPE_ID + " IN " +
                        "( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() /*+ " OR " + COLUMN_TYPE_GROUP_ID + " = 0"*/ + ") "
                        + " ORDER BY(" + COLUMN_CATEGORY_UPDATE_AT + ") DESC ;";

        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }


    public ArrayList<MainSummaryReport> getAllMainSummaryReportByTypeByUser(String type, UserDetail user) {
        String GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER =
                "SELECT " + COLUMN_CATEGORY_ID + ", " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' "
                        + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                        + ") AND " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                        + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + //";";
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_TYPE_ID + " IN " +
                        "( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' " + ") "
                        + " ORDER BY(" + COLUMN_CATEGORY_UPDATE_AT + ") DESC ;";

        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<MainSummaryReport> getAllMainSummaryReportByTypeByUserByTime(String type, UserDetail user, String timeFrom, String timeTo) {
        String GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER =
                "SELECT " + COLUMN_CATEGORY_ID + ", " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' " + " AND " + COLUMN_REPORT_DATETIME + " BETWEEN '" + timeFrom + "' AND '" + timeTo + "' "
                        + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                        + ") AND " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                        + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + //";";
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_TYPE_ID + " IN " +
                        "( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' " + ") "
                        + " ORDER BY(" + COLUMN_CATEGORY_UPDATE_AT + ") DESC ;";

        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<MainSummaryReport> getAllMainSummaryReportByTypeByUserByToday(String type, UserDetail user, String today) {
        String GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER =
                "SELECT " + COLUMN_CATEGORY_ID + ", " + COLUMN_CATEGORY_NAME + ", ( SELECT SUM( " + COLUMN_REPORT_AMOUNT + ") FROM " + TABLE_REPORT_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' " + " AND " + COLUMN_REPORT_DATETIME + " = '" + today + "' "
                        + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                        + ") AND " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " = " + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID
                        + " GROUP BY " + COLUMN_REPORT_CAT_ID + ") AS " + COLUMN_REPORT_AMOUNT_EACH_TOTAL + ", " + COLUMN_CATEGORY_STATUS + " FROM " + TABLE_CATEGORY_DETAIL + //";";
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + COLUMN_CATEGORY_TYPE_ID + " IN " +
                        "( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " AND " + COLUMN_TYPE_NAME + " = '" + type + "' "/*+ " AND "+COLUMN_REPORT_DATETIME + " = '"+today+"' "*/ + ") "
                        + " ORDER BY(" + COLUMN_CATEGORY_UPDATE_AT + ") DESC ;";

        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT_BY_TYPE_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private MainSummaryReport cursorToMainSummaryReport(Cursor cursor) {
        MainSummaryReport mainSummaryReport = new MainSummaryReport();
        mainSummaryReport.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
        mainSummaryReport.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
        mainSummaryReport.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_AMOUNT_EACH_TOTAL)));
        mainSummaryReport.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_STATUS)) == 1 ? false : true);
        return mainSummaryReport;
    }

    private ArrayList<MainSummaryReport> getAllMainSummaryReportByDateLastest() {
        ArrayList<MainSummaryReport> list = new ArrayList<>();
        Cursor cursor = getAll(GET_LIST_MAIN_SUMMARY_REPORT);

        while (!cursor.isAfterLast()) {
            list.add(cursorToMainSummaryReport(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    /************************* method work with OutComeDetail/IncomeDetail *******************/

    public ArrayList<ReportDetail> getTotalReportDetail() {
        ArrayList<ReportDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_REPORT_DETAIL);

        while (!cursor.isAfterLast()) {
            list.add(cursorToReportDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private String getUpdateAtDatetime() {
        Date date = new Date();
        return String.valueOf(date);
    }

    public int getTotalOutcomeAmount() {
        int sum = 0;
        Cursor cursor = getAll(SELECT_TOTAL_AMOUNT_OUTCOME);

        while (!cursor.isAfterLast()) {
            sum += cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_AMOUNT_EACH_TOTAL));
            cursor.moveToNext();
        }
        cursor.close();

        return sum;
    }

    public int getTotalIncomeAmount() {
        int sum = 0;
        Cursor cursor = getAll(SELECT_TOTAL_AMOUNT_INCOME);

        while (!cursor.isAfterLast()) {
            sum += cursor.getInt(cursor.getColumnIndex(COLUMN_REPORT_AMOUNT_EACH_TOTAL));
            cursor.moveToNext();
        }
        cursor.close();

        return sum;
    }


    public boolean isIncomeReport(String categoryName) {
        String sql = "SELECT * FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND "
                + COLUMN_CATEGORY_NAME + " = '" + categoryName + "';";
        CategoryDetail categoryDetail = getCategoryDetail(sql);

        if (categoryDetail.getCatTypeId() == 1) {
            return true;
        }
        return false;
    }

    /************************* method work with UserDetail *******************/

    public ArrayList<UserDetail> getAllUsers() {
        ArrayList<UserDetail> users = null;

        if (isExistTableUser()) {
            users = new ArrayList<UserDetail>();
            Cursor cursor = getAll(SELECT_ALL_USER);
            while (!cursor.isAfterLast()) {
                users.add(cursorToUser(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }


    public ArrayList<UserDetail> getNewUsers(UserDetail currentUser) {
        ArrayList<UserDetail> users = null;

        String SELECT_NEW_USER =
                "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_STATUS + " = " + ComonUtils.USER_STATUS_NEW
                        + " AND " + COLUMN_USER_GROUP_ID + " = " + currentUser.getUserGroupId();

        if (isExistTableUser()) {
            users = new ArrayList<UserDetail>();
            Cursor cursor = getAll(SELECT_NEW_USER);
            while (!cursor.isAfterLast()) {
                users.add(cursorToUser(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }

    public ArrayList<UserDetail> getActiveUsers(UserDetail currentUser) {
        ArrayList<UserDetail> users = null;

        String SELECT_ACTIVE_USER =
                "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_STATUS + " = " + ComonUtils.USER_STATUS_NORMAL
                        + " AND " + COLUMN_USER_GROUP_ID + " = " + currentUser.getUserGroupId();

        if (isExistTableUser()) {
            users = new ArrayList<UserDetail>();
            Cursor cursor = getAll(SELECT_ACTIVE_USER);
            while (!cursor.isAfterLast()) {
                UserDetail user = cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }

    public ArrayList<UserDetail> getRemovedUsers(UserDetail currentUser) {
        ArrayList<UserDetail> users = null;

        String SELECT_DEACTIVE_USER =
                "SELECT * FROM " + TABLE_USER + " WHERE (" + COLUMN_USER_STATUS + " = " + ComonUtils.USER_STATUS_REMOVED
                        + " OR " + COLUMN_USER_STATUS + " = " + ComonUtils.USER_STATUS_REJECT
                        + ") AND " + COLUMN_USER_GROUP_ID + " = " + currentUser.getUserGroupId();

        if (isExistTableUser()) {
            users = new ArrayList<UserDetail>();
            Cursor cursor = getAll(SELECT_DEACTIVE_USER);
            while (!cursor.isAfterLast()) {
                UserDetail user = cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }

    public ArrayList<UserDetail> getAllUsersInGroupFromDB(UserDetail currentUser) {
        ArrayList<UserDetail> users = null;

        String SELECT_ALL_USER_IN_GROUP =
                "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_GROUP_ID + " = " + currentUser.getUserGroupId() + ";";

        if (isExistTableUser()) {
            users = new ArrayList<UserDetail>();
            Cursor cursor = getAll(SELECT_ALL_USER_IN_GROUP);
            while (!cursor.isAfterLast()) {
                UserDetail user = cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }

    private UserDetail cursorToUser(Cursor cursor) {
        UserDetail user = new UserDetail();
        user.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        user.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setUserPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        user.setUserGroupId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GROUP_ID)));
        user.setUserAvatar(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_AVATAR)));
        user.setUserAvatarImgPath(cursor.getString(cursor.getColumnIndex(COLUMN_USER_AVATAR_IMG_PATH)));
        user.setUserStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_STATUS)));
        user.setUserRole(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ROLE)));
        return user;
    }

    public long insertUser(UserDetail user) {
        return insert(TABLE_USER, userToValues(user));
    }

    private ContentValues userToValues(UserDetail user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getUserPassword());
        values.put(COLUMN_USER_GROUP_ID, user.getUserGroupId());
        values.put(COLUMN_USER_AVATAR, user.getUserAvatar());
        values.put(COLUMN_USER_AVATAR_IMG_PATH, user.getUserAvatarImgPath());
        values.put(COLUMN_USER_STATUS, user.getUserStatus());
        values.put(COLUMN_USER_ROLE, user.getUserRole());
        return values;
    }

    public boolean updateUser(UserDetail user) {
        return update(TABLE_USER, userToValues(user), COLUMN_USER_ID + " = " + user.getUserId());
    }


    /************************* method work with GroupDetail *******************/

    public ArrayList<GroupDetail> getAllGroups() {
        ArrayList<GroupDetail> groups = null;

        if (isExistTableGroup()) {
            groups = new ArrayList<GroupDetail>();
            Cursor cursor = getAll(SELECT_ALL_GROUP);
            while (!cursor.isAfterLast()) {
                groups.add(cursorToGroup(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return groups;
    }

    private GroupDetail cursorToGroup(Cursor cursor) {
        GroupDetail group = new GroupDetail();
        group.setGroupId(cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_ID)));
        group.setGroupName(cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_NAME)));
        group.setGroupAvatar(cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_AVATAR)));
        group.setGroupAvatarImgPath(cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_AVATAR_IMG_PATH)));
        return group;
    }

    public long insertGroup(GroupDetail group) {
        return insert(TABLE_GROUP, groupToValues(group));
    }

    private ContentValues groupToValues(GroupDetail group) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUP_ID, group.getGroupId());
        values.put(COLUMN_GROUP_NAME, group.getGroupName());
        values.put(COLUMN_GROUP_AVATAR, group.getGroupAvatar());
        values.put(COLUMN_GROUP_AVATAR_IMG_PATH, group.getGroupAvatarImgPath());
        return values;
    }

    public boolean updateGroup(GroupDetail group) {
        return update(TABLE_GROUP, groupToValues(group), COLUMN_GROUP_ID + " = " + group.getGroupId());
    }

    public GroupDetail findGroupByName(String name) {
        String query = "SELECT * FROM " + TABLE_GROUP + " WHERE " + COLUMN_GROUP_NAME + " = '" + name + "';";
        Cursor cursor = getAll(query);
        GroupDetail group = null;

        while (!cursor.isAfterLast()) {
            group = cursorToGroup(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return group;
    }

    public GroupDetail findGroupById(int id) {
        String query = "SELECT * FROM " + TABLE_GROUP + " WHERE " + COLUMN_GROUP_ID + " = " + id + ";";
        Cursor cursor = getAll(query);
        GroupDetail group = null;

        while (!cursor.isAfterLast()) {
            group = cursorToGroup(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return group;
    }

    public ArrayList<ReportDetail> getAllReportDetailByUser(UserDetail user) {

        String SELECT_ALL_REPORT_DETAIL_BY_USER =
                "SELECT * FROM " + TABLE_REPORT_DETAIL +
                        " WHERE " + COLUMN_REPORT_DELETED + " = 0 AND " +
                        COLUMN_REPORT_CAT_ID + " IN ( SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 0 AND " + COLUMN_CATEGORY_TYPE_ID +
                        " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() +
                        "));";

        ArrayList<ReportDetail> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_REPORT_DETAIL_BY_USER);

        while (!cursor.isAfterLast()) {
            list.add(cursorToReportDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public int getCategoryIdFromName(String name, UserDetail user) {
        String SELECT_CATEGORY_ID =
                "SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE " + COLUMN_CATEGORY_NAME + " ='" + name + "' AND "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND " + COLUMN_CATEGORY_TYPE_ID +
                        " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                        " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() +
                        ");";

        Cursor cursor = getAll(SELECT_CATEGORY_ID);

        int catId = 0;

        while (!cursor.isAfterLast()) {
            catId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
            cursor.moveToNext();
        }
        cursor.close();

        return catId;
    }

    public int getMaxTypeDetailId() {
        String SELECT_MAX_TYPE_ID =
                "SELECT MAX(" + COLUMN_TYPE_ID + ") AS maxId FROM " + TABLE_TYPE_DETAIL;

        Cursor cursor = getAll(SELECT_MAX_TYPE_ID);

        int maxId = 0;

        while (!cursor.isAfterLast()) {
            maxId = cursor.getInt(cursor.getColumnIndex("maxId"));
            cursor.moveToNext();
        }
        cursor.close();

        return maxId;
    }

    public int getMaxUserId() {
        String SELECT_MAX_USER_ID =
                "SELECT MAX(" + COLUMN_USER_ID + ") AS maxId FROM " + TABLE_USER;

        Cursor cursor = getAll(SELECT_MAX_USER_ID);

        int maxId = 0;

        while (!cursor.isAfterLast()) {
            maxId = cursor.getInt(cursor.getColumnIndex("maxId"));
            cursor.moveToNext();
        }
        cursor.close();

        return maxId;
    }

    public int getMaxGroupId() {
        String SELECT_MAX_GROUP_ID =
                "SELECT MAX(" + COLUMN_GROUP_ID + ") AS maxId FROM " + TABLE_GROUP;

        Cursor cursor = getAll(SELECT_MAX_GROUP_ID);

        int maxId = 0;

        while (!cursor.isAfterLast()) {
            maxId = cursor.getInt(cursor.getColumnIndex("maxId"));
            cursor.moveToNext();
        }
        cursor.close();

        return maxId;
    }

    public GroupDetail getGroupDetailById(int id) {
        String SELECT_GROUP =
                "SELECT * FROM " + TABLE_GROUP + " WHERE "+ COLUMN_GROUP_ID + " = "+ id + ";";

        Cursor cursor = getAll(SELECT_GROUP);

        GroupDetail groupDetail = null;

        while (!cursor.isAfterLast()) {
            groupDetail = cursorToGroup(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return groupDetail;
    }

    public int getMaxReportDetailId() {
        String SELECT_MAX_REPORT_ID =
                "SELECT MAX(" + COLUMN_REPORT_ID + ") AS maxId FROM " + TABLE_REPORT_DETAIL;

        Cursor cursor = getAll(SELECT_MAX_REPORT_ID);

        int maxId = 0;

        while (!cursor.isAfterLast()) {
            maxId = cursor.getInt(cursor.getColumnIndex("maxId"));
            cursor.moveToNext();
        }
        cursor.close();

        return maxId;
    }

    public void deleteAllReports() {
        String DELETE_ALL_REPORTS =
                "DELETE FROM " + TABLE_REPORT_DETAIL + ";";
        db.execSQL(DELETE_ALL_REPORTS);
    }

    public void deleteAllCategories() {
        String DELETE_ALL_CATEGORIES =
                "DELETE FROM " + TABLE_CATEGORY_DETAIL + ";";
        db.execSQL(DELETE_ALL_CATEGORIES);
    }

    public void deleteAllTypes() {
        String DELETE_ALL_TYPES =
                "DELETE FROM " + TABLE_TYPE_DETAIL + ";";
        db.execSQL(DELETE_ALL_TYPES);
    }

    public void deleteAllUsers() {
        String DELETE_ALL_USERS =
                "DELETE FROM " + TABLE_USER + ";";
        db.execSQL(DELETE_ALL_USERS);
    }

    public void deleteAllGroups() {
        String DELETE_ALL_GROUPS =
                "DELETE FROM " + TABLE_GROUP + ";";
        db.execSQL(DELETE_ALL_GROUPS);
    }

    public List<CategoryDetail> getDeletedCategories() {
        String SELECT_DELETED_CATEGORY =
                "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                        " WHERE " + COLUMN_CATEGORY_DELETED + " = 1 ;";

        List<CategoryDetail> list = new ArrayList<CategoryDetail>();
        Cursor cursor = getAll(SELECT_DELETED_CATEGORY);

        while (!cursor.isAfterLast()) {
            list.add(cursorToCategoryDetail(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public ArrayList<ReportDetailDisplay> getAllReportDetaiDisplaylByUser(UserDetail user) {

        String SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_BY_USER =
                "SELECT " + COLUMN_REPORT_ID + " , " + COLUMN_REPORT_DATETIME + " , (SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID + " = " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID + " AND " + COLUMN_REPORT_DELETED + " = 0 "
                        + " ) AS " + COLUMN_CATEGORY_NAME
                        + " , " + COLUMN_REPORT_TITLE + " , " + COLUMN_REPORT_AMOUNT + " , " + COLUMN_REPORT_NOTE + " FROM " + TABLE_REPORT_DETAIL + " WHERE " + COLUMN_REPORT_DELETED + " = 0 "//;
                        + " AND " + COLUMN_REPORT_CAT_ID + " IN(SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL + " WHERE "
                        + COLUMN_CATEGORY_DELETED + " = 0 AND "
                        + TABLE_CATEGORY_DETAIL + "." + COLUMN_CATEGORY_ID + " = " + TABLE_REPORT_DETAIL + "." + COLUMN_REPORT_CAT_ID
                        + " AND " + COLUMN_CATEGORY_TYPE_ID + " IN " + "(SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL + " WHERE " +
                        COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + "))"
                        + " ORDER BY(" + COLUMN_REPORT_UPDATE_AT + ") DESC;";


        ArrayList<ReportDetailDisplay> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_ALL_REPORT_DETAIL_WITH_CATEGORY_BY_USER);

        while (!cursor.isAfterLast()) {
            ReportDetailDisplay reportDetailDisplay = cursorToReportDetailDisplay(cursor);
            if (reportDetailDisplay != null && reportDetailDisplay.getCategoryName() != null) {
                String dateStr = reportDetailDisplay.getReportDatetime();
                String displayDate = ComonUtils.getDisplayStrDatetimeFormatMMMdd(dateStr);
                reportDetailDisplay.setReportDatetime(displayDate);

                list.add(reportDetailDisplay);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }


    public boolean checkCatNameExistInDb(String catName, UserDetail user) {
        String SELECT_CAT_BY_NAME = "SELECT * FROM " + TABLE_CATEGORY_DETAIL +
                " WHERE " + COLUMN_CATEGORY_NAME + " = '" + catName + "'" +
                " AND " + COLUMN_CATEGORY_TYPE_ID + " IN (SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL +
                " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + " ); ";


        ArrayList<ReportDetailDisplay> list = new ArrayList<>();
        Cursor cursor = getAll(SELECT_CAT_BY_NAME);

        while (!cursor.isAfterLast()) {
            return true;
        }
        cursor.close();

        return false;
    }


    public DatabaseUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public int getNumOfReportInCategory(String catName, UserDetail user) {
        String NUMBER = "NUMBER";
        String SELECT_NO_REPORT = "SELECT COUNT(" + COLUMN_REPORT_ID + ") AS " + NUMBER + " FROM " + TABLE_REPORT_DETAIL
                + " WHERE " + COLUMN_REPORT_CAT_ID + " = ("
                + " SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DETAIL
                + " WHERE " + COLUMN_CATEGORY_NAME + " = '" + catName + "' AND "
                + COLUMN_CATEGORY_MARK_IMPORTAN + " = 1 AND "
                + COLUMN_CATEGORY_TYPE_ID + " IN ( SELECT " + COLUMN_TYPE_ID + " FROM " + TABLE_TYPE_DETAIL
                + " WHERE " + COLUMN_TYPE_GROUP_ID + " = " + user.getUserGroupId() + ");";
//                                    + " GROUP BY("+COLUMN_REPORT_CAT_ID+");";


//        Cursor cursor = getAll(SELECT_NO_REPORT);
//
//        while (!cursor.isAfterLast()) {
//            return cursor.getInt(cursor.getColumnIndex(NUMBER));
//        }
//        cursor.close();

        return 0;
    }

    //lay toan bo sum amount cua income report theo tung thang
    public ArrayList<Integer> getAllAmountByMonths(String typeName, int year) {

        ArrayList<Integer> list = new ArrayList<Integer>();

        ArrayList<String> months = ComonUtils.getListMonthString();

        int count = ComonUtils.getCurrentMonthInteger();

        for (int i = 0; i < count; i++) {
            String dateStart = year + "-" + months.get(i) + "-01";
            String dateEnd = year + "-" + months.get(i) + "-32";

            String SELECT_NO_REPORT = "select sum(reportAmount) as NUMBER from ReportDetailTbl inner join [CategoryDetailTbl] inner join [TypeDetailTbl]" +
                    " where [ReportDetailTbl].[reportCategoryId] = [CategoryDetailTbl].[categoryID] AND [CategoryDetailTbl].[categoryTypeId] = [TypeDetailTbl].[typeId]" +
                    " AND [TypeDetailTbl].[typeName] = '" + typeName + "' " +
                    " AND DATETIME([ReportDetailTbl].[reportDateTime]) BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ";
            //" group by ReportDetailTbl.[reportCategoryId]";

            Cursor cursor = getAll(SELECT_NO_REPORT);

            while (!cursor.isAfterLast()) {
                list.add(cursor.getInt(cursor.getColumnIndex("NUMBER")));
            }

            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return list;
    }

    //lay toan bo sum amount cua income report theo tung thang
    public int getAllAmountByMonths(String typeName, int year, String month) {
        int sum = -1;

        String dateStart = year + "-" + month + "-01";
        String dateEnd = year + "-" + month + "-32";

        String SELECT_NO_REPORT = "select sum(reportAmount) as NUMBER from ReportDetailTbl inner join [CategoryDetailTbl] inner join [TypeDetailTbl]" +
                " where [ReportDetailTbl].[reportCategoryId] = [CategoryDetailTbl].[categoryID] AND [CategoryDetailTbl].[categoryTypeId] = [TypeDetailTbl].[typeId]" +
                " AND [TypeDetailTbl].[typeName] = '" + typeName + "' " +
                " AND DATETIME([ReportDetailTbl].[reportDateTime]) BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ";

        Cursor cursor = getAll(SELECT_NO_REPORT);

        while (!cursor.isAfterLast()) {
            sum = cursor.getInt(cursor.getColumnIndex("NUMBER"));
            cursor.moveToNext();
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return sum;
    }


    public String getMaxDateByUser(UserDetail user) {
        String max_date = null;

        String SELECT_MAX_DATE = "SELECT MAX(reportDateTime) AS max_date from [ReportDetailTbl] " +
                " inner join [CategoryDetailTbl] inner join [TypeDetailTbl] " +
                " where [ReportDetailTbl].[reportCategoryId] = [CategoryDetailTbl].[categoryID] " +
                " AND [CategoryDetailTbl].[categoryTypeId] = [TypeDetailTbl].[typeId] " +
                " AND [TypeDetailTbl].[typeGroupId] = " + user.getUserGroupId() + " ;";

        Cursor cursor = getAll(SELECT_MAX_DATE);

        while (!cursor.isAfterLast()) {
            max_date = cursor.getString(cursor.getColumnIndex("max_date"));
            cursor.moveToNext();
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return max_date;
    }

    public String getMinDateByUser(UserDetail user) {
        String max_date = null;

        String SELECT_MAX_DATE = "SELECT MIN(reportDateTime) AS max_date from [ReportDetailTbl] " +
                " inner join [CategoryDetailTbl] inner join [TypeDetailTbl] " +
                " where [ReportDetailTbl].[reportCategoryId] = [CategoryDetailTbl].[categoryID] " +
                " AND [CategoryDetailTbl].[categoryTypeId] = [TypeDetailTbl].[typeId] " +
                " AND [TypeDetailTbl].[typeGroupId] = " + user.getUserGroupId() + " ;";

        Cursor cursor = getAll(SELECT_MAX_DATE);

        while (!cursor.isAfterLast()) {
            max_date = cursor.getString(cursor.getColumnIndex("max_date"));
            cursor.moveToNext();
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return max_date;
    }
}
