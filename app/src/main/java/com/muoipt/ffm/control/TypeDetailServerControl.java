package com.muoipt.ffm.control;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class TypeDetailServerControl {
    private Context mContext;

    public TypeDetailServerControl(Context context) {
        mContext = context;
    }

    public ArrayList<TypeDetail> getTypeDetailServerData() {
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        final ArrayList<TypeDetail> typeDetailsServer = new ArrayList<TypeDetail>();

        try {
            List<ParseObject> objects = query1.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int typeId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_ID);
                    String typeName = objects.get(i).getString(DatabaseUtils.COLUMN_TYPE_NAME);
                    int typeGroupId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_GROUP_ID);

                    TypeDetail typeDetail = new TypeDetail(typeId, typeName, typeGroupId);
                    typeDetailsServer.add(typeDetail);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return typeDetailsServer;
    }

    public ArrayList<TypeDetail> getTypeDetailServerDataByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        int groupId = user.getUserGroupId();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        query1.whereEqualTo(DatabaseUtils.COLUMN_TYPE_GROUP_ID, groupId);
        final ArrayList<TypeDetail> typeDetailsServer = new ArrayList<TypeDetail>();

        try {
            List<ParseObject> objects = query1.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int typeId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_ID);
                    String typeName = objects.get(i).getString(DatabaseUtils.COLUMN_TYPE_NAME);
                    int typeGroupId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_GROUP_ID);

                    TypeDetail typeDetail = new TypeDetail(typeId, typeName, typeGroupId);
                    typeDetailsServer.add(typeDetail);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return typeDetailsServer;
    }

    private List<TypeDetail> getLogInTypesFromGroupId(int groupId) {
        List<TypeDetail> typeDetails = new ArrayList<TypeDetail>();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        query2.whereEqualTo(DatabaseUtils.COLUMN_TYPE_GROUP_ID, groupId);

        try {
            List<ParseObject> objects = query2.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int typeId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_ID);
                    String typeName = objects.get(i).getString(DatabaseUtils.COLUMN_TYPE_NAME);
                    int typeGroupId = objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_GROUP_ID);

                    typeDetails.add(new TypeDetail(typeId, typeName, typeGroupId));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return typeDetails;
    }

    public List<Integer> getLogInTypeIdsFromGroupId(int groupId) {
        List<Integer> typeIds = new ArrayList<Integer>();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        query2.whereEqualTo(DatabaseUtils.COLUMN_TYPE_GROUP_ID, groupId);

        try {
            List<ParseObject> objects = query2.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    typeIds.add(objects.get(i).getInt(DatabaseUtils.COLUMN_TYPE_ID));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return typeIds;
    }

    public boolean saveTypeDetailToServer(ArrayList<TypeDetail> arrTypes) {
        final boolean[] res = {true};

        for (int i = 0; i < arrTypes.size(); i++) {
            //check TypeDetail is exist or not
            if (!checkTypeDetailServerExist(arrTypes.get(i))) {
                ParseObject typeDetail = new ParseObject(DatabaseUtils.TABLE_TYPE_DETAIL);
                typeDetail.put(DatabaseUtils.COLUMN_TYPE_ID, arrTypes.get(i).getTypeId());
                typeDetail.put(DatabaseUtils.COLUMN_TYPE_NAME, arrTypes.get(i).getTypeName());
                typeDetail.put(DatabaseUtils.COLUMN_TYPE_GROUP_ID, arrTypes.get(i).getTypeGroupId());
                typeDetail.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Save in background", "sucessful");
                        } else {
                            Log.i("Save in background", "failed error : " + e.toString());
                            res[0] = false;
                        }
                    }
                });
            }

        }

        return res[0];
    }

    private boolean checkTypeDetailServerExist(TypeDetail typeDetail) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        query.whereEqualTo(DatabaseUtils.COLUMN_TYPE_NAME, typeDetail.getTypeName());
        query.whereEqualTo(DatabaseUtils.COLUMN_TYPE_GROUP_ID, typeDetail.getTypeGroupId());

        try {
            List<ParseObject> groups = query.find();
            if (groups.size() > 0) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateTypeDetailToServer(ArrayList<TypeDetail> arrTypes) {
        final boolean[] res = {true};

        for (int i = 0; i < arrTypes.size(); i++) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
            query.whereEqualTo(DatabaseUtils.COLUMN_TYPE_ID, arrTypes.get(i).getTypeId());

            try {
                List<ParseObject> catDetail = query.find();
                for (int j = 0; j < catDetail.size(); j++) {
                    if (catDetail != null) {
                        catDetail.get(j).put(DatabaseUtils.COLUMN_TYPE_NAME, arrTypes.get(i).getTypeName());

                        catDetail.get(j).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("updateTypeToServer", "sucessful");
                                } else {
                                    Log.i("updateTypeToServer", "failed error : " + e.toString());
                                    res[0] = false;
                                }
                            }
                        });
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return res[0];
    }

    public int getNextTypeDetailTblId() {
        return SyncUtils.getIdInServer(DatabaseUtils.COLUMN_TYPE_ID_DATA);
    }

    public List<TypeDetail> getLogInTypesFromServer(UserDetail user) {
        UserDetailCustomServerControl control = new UserDetailCustomServerControl(mContext);
        int groupid = control.getLogInGroupIdFromServer(user);
        List<TypeDetail> types = new ArrayList<TypeDetail>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        query.whereEqualTo(DatabaseUtils.COLUMN_TYPE_GROUP_ID, groupid);
        try {
            List<ParseObject> typeList = query.find();
            if (typeList != null) {
                for (int i = 0; i < typeList.size(); i++) {
                    int typeId = typeList.get(i).getInt(DatabaseUtils.COLUMN_TYPE_ID);
                    String typeName = typeList.get(i).getString(DatabaseUtils.COLUMN_TYPE_NAME);
                    TypeDetail t = new TypeDetail(typeId, typeName, groupid);
                    types.add(t);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return types;
    }

    public boolean deleteAllTypeServer(){
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_TYPE_DETAIL);
        try {
            List<ParseObject> objs = query3.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++) {
                    objs.get(i).delete();
                    objs.get(i).save();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
