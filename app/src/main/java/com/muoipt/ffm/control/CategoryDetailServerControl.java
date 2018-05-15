package com.muoipt.ffm.control;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class CategoryDetailServerControl {
    private Context mContext;

    public CategoryDetailServerControl(Context context) {
        mContext = context;
    }

    public boolean saveCatDetailToServer(ArrayList<CategoryDetail> arrCats) {
        final boolean[] res = {true};

        for (int i = 0; i < arrCats.size(); i++) {
            ParseObject catDetail = new ParseObject(DatabaseUtils.TABLE_CATEGORY_DETAIL);
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_ID, arrCats.get(i).getCatId());
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_NAME, arrCats.get(i).getCatName());
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID, arrCats.get(i).getCatTypeId());
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_LIMIT, arrCats.get(i).getCatLimit());
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_STATUS, arrCats.get(i).isCatStatus());
            if (arrCats.get(i).getCatAvatar() != -1) {
                catDetail.put(DatabaseUtils.COLUMN_CATEGORY_AVATAR, arrCats.get(i).getCatAvatar());
            }
            if (!arrCats.get(i).getCatAvatarImagePath().equals("")) {
                catDetail.put(DatabaseUtils.COLUMN_CATEGORY_AVATAR_IMG_PATH, arrCats.get(i).getCatAvatarImagePath());
            }
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_DELETED, arrCats.get(i).isCatDeleted());
            catDetail.put(DatabaseUtils.COLUMN_CATEGORY_MARK_IMPORTAN, arrCats.get(i).isMark());

            catDetail.saveInBackground(new SaveCallback() {
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

        return res[0];
    }

    public boolean updateCatDetailToServer(ArrayList<CategoryDetail> arrCats) {
        final boolean[] res = {true};

        for (int i = 0; i < arrCats.size(); i++) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
            query.whereEqualTo(DatabaseUtils.COLUMN_CATEGORY_ID, arrCats.get(i).getCatId());

            try {
                List<ParseObject> catDetail = query.find();
                for (int j = 0; j < catDetail.size(); j++) {
                    if (catDetail != null) {
                        catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_NAME, arrCats.get(i).getCatName());
                        catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_LIMIT, arrCats.get(i).getCatLimit());
                        catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_STATUS, arrCats.get(i).isCatStatus());
                        if (arrCats.get(i).getCatAvatar() != -1) {
                            catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_AVATAR, arrCats.get(i).getCatAvatar());
                        }
                        if (!arrCats.get(i).getCatAvatarImagePath().equals("")) {
                            catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_AVATAR_IMG_PATH, arrCats.get(i).getCatAvatarImagePath());
                        }
                        catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_DELETED, arrCats.get(i).isCatDeleted());
                        catDetail.get(j).put(DatabaseUtils.COLUMN_CATEGORY_MARK_IMPORTAN, arrCats.get(i).isMark());

                        catDetail.get(j).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("updateCatToServer", "sucessful");
                                } else {
                                    Log.i("updateCatToServer", "failed error : " + e.toString());
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

    public int getNextCategoryDetailTblId() {
        return SyncUtils.getIdInServer(DatabaseUtils.COLUMN_CATEGORY_ID_DATA);
    }

    public List<CategoryDetail> getDeletedCategoriesOnServer() {
        List<CategoryDetail> list = new ArrayList<CategoryDetail>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        query.whereEqualTo(DatabaseUtils.COLUMN_CATEGORY_DELETED, true);
        try {
            List<ParseObject> objects = query.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int catId = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_ID);
                    String catName = objects.get(i).getString(DatabaseUtils.COLUMN_CATEGORY_NAME);
                    int catTypeId = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID);
                    int catLimit = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_LIMIT);
                    boolean catStatus = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_STATUS);
                    int catAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_AVATAR);
                    String catAvatarImgPath = objects.get(i).getString(DatabaseUtils.COLUMN_CATEGORY_AVATAR_IMG_PATH);
                    String updateAt = objects.get(i).getUpdatedAt().toString();
                    boolean catDeleted = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_DELETED);
                    boolean catMark = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_MARK_IMPORTAN);

                    CategoryDetail categoryDetail = new CategoryDetail(catId, catName, catTypeId, catLimit, catStatus, catAvatar, catAvatarImgPath, catMark, updateAt, catDeleted);
                    list.add(categoryDetail);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteCategoriesInServer(List<CategoryDetail> arrCatDeleted) {
        List<Integer> arrCatId = new ArrayList<Integer>();
        for (int i = 0; i < arrCatDeleted.size(); i++) {
            arrCatId.add(arrCatDeleted.get(i).getCatId());
        }

        if (arrCatId.size() == 0)
            return;

        //first delete all report with category deleted
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
        query1.whereContainedIn(DatabaseUtils.COLUMN_REPORT_CAT_ID, arrCatId);
        try {
            List<ParseObject> objs = query1.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //then delete category
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        query2.whereContainedIn(DatabaseUtils.COLUMN_CATEGORY_ID, arrCatId);
        try {
            List<ParseObject> objs = query2.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++)
                    objs.get(i).delete();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean checkCatNameExistInServer(String catName) {
        UserDetail user = AppConfig.getUserLogInInfor();

        TypeDetailServerControl control = new TypeDetailServerControl(mContext);
        List<Integer> catTypeIds = control.getLogInTypeIdsFromGroupId(user.getUserGroupId());

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        query.whereEqualTo(DatabaseUtils.COLUMN_CATEGORY_NAME, catName);
        query.whereContainedIn(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID, catTypeIds);

        try {
            List<ParseObject> data = query.find();
            if (data.size() > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<CategoryDetail> getCategoryDetailServerDataByUser() {
        UserDetail user = AppConfig.getUserLogInInfor();

//        int groupId = SyncUtils.getLogInGroupIdFromServer(user);
        int groupId = user.getUserGroupId();
        TypeDetailServerControl control = new TypeDetailServerControl(mContext);
        List<Integer> typeDetails = control.getLogInTypeIdsFromGroupId(groupId);

        final ArrayList<CategoryDetail> catDetailsServer = new ArrayList<CategoryDetail>();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        query2.whereContainedIn(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID, typeDetails);

        try {
            List<ParseObject> objects = query2.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int catId = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_ID);
                    String catName = objects.get(i).getString(DatabaseUtils.COLUMN_CATEGORY_NAME);
                    int catTypeId = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID);
                    int catLimit = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_LIMIT);
                    boolean catStatus = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_STATUS);
                    int catAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_AVATAR);
                    String catAvatarImgPath = objects.get(i).getString(DatabaseUtils.COLUMN_CATEGORY_AVATAR_IMG_PATH);
                    String updateAt = objects.get(i).getUpdatedAt().toString();
                    boolean catDeleted = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_DELETED);
                    boolean catMarkImportant = objects.get(i).getBoolean(DatabaseUtils.COLUMN_CATEGORY_MARK_IMPORTAN);

                    CategoryDetail categoryDetail = new CategoryDetail(catId, catName, catTypeId, catLimit, catStatus, catAvatar, catAvatarImgPath, catMarkImportant, updateAt, catDeleted);
                    catDetailsServer.add(categoryDetail);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return catDetailsServer;
    }

    public boolean deleteAllCategoryServer(){
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        try {
            List<ParseObject> objs = query2.find();
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
