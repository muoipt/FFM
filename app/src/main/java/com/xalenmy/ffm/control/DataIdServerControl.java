package com.xalenmy.ffm.control;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xalenmy.ffm.database.DatabaseUtils;

import java.util.List;

/**
 * Created by XalenMy on 2/26/2018.
 */

public class DataIdServerControl {
    private Context mContext;

    public DataIdServerControl(Context context) {
        mContext = context;
    }

    public int getIdInServer(String columnName) {
        int id = 1; //default

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
        }

        return id;
    }

//    public void setIdDataTable() {
//        ParseObject idData = new ParseObject(DatabaseUtils.TABLE_ID_DATA);
//
//        idData.put(DatabaseUtils.COLUMN_ID_DATA_ID, 1);
//        idData.put(DatabaseUtils.COLUMN_TYPE_ID_DATA, 1);
//        idData.put(DatabaseUtils.COLUMN_CATEGORY_ID_DATA, 1);
//        idData.put(DatabaseUtils.COLUMN_REPORT_ID_DATA, 1);
//        idData.put(DatabaseUtils.COLUMN_GROUP_ID_DATA, 1);
//        idData.put(DatabaseUtils.COLUMN_USER_ID_DATA, 1);
//        try {
//            idData.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    public boolean checkIdDataTableExist() {
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

    //just reset value for table to default
    public boolean deleteIdDataServer() {
        ParseQuery<ParseObject> query0 = ParseQuery.getQuery(DatabaseUtils.TABLE_ID_DATA);
        query0.whereEqualTo(DatabaseUtils.COLUMN_ID_DATA_ID, 1);
        try {
            List<ParseObject> objs = query0.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++){
                    objs.get(0).put(DatabaseUtils.COLUMN_REPORT_ID_DATA, 1);
                    objs.get(0).put(DatabaseUtils.COLUMN_CATEGORY_ID_DATA, 1);
                    objs.get(0).put(DatabaseUtils.COLUMN_TYPE_ID_DATA, 1);
                    objs.get(0).put(DatabaseUtils.COLUMN_GROUP_ID_DATA, 1);
//                    objs.get(0).put(DatabaseUtils.COLUMN_USER_ID_DATA, 1);
                    try {
                        objs.get(0).save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
