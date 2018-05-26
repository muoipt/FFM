package com.muoipt.ffm.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.muoipt.ffm.R;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class GroupDetailServerControl {
    private Context mContext;

    public GroupDetailServerControl(Context context) {
        mContext = context;
    }

    public boolean saveGroupToServer(ArrayList<GroupDetail> arrGroups) {
        final boolean[] res = {true};

        for (int i = 0; i < arrGroups.size(); i++) {
            ParseObject group = new ParseObject(DatabaseUtils.TABLE_GROUP);
            group.put(DatabaseUtils.COLUMN_GROUP_ID, arrGroups.get(i).getGroupId());
            group.put(DatabaseUtils.COLUMN_GROUP_NAME, arrGroups.get(i).getGroupName());
            group.put(DatabaseUtils.COLUMN_GROUP_AVATAR, arrGroups.get(i).getGroupAvatar());

            group.saveInBackground(new SaveCallback() {
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

    private Bitmap getAvatarBitmapFromPath(String imgPath) {
        File image = new File(imgPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;

        File file = new File(image.getPath());
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(image.getPath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) mContext.getResources().getDimension(R.dimen.img_header_main_icon_width), (int) mContext.getResources().getDimension(R.dimen.img_header_main_icon_width), true);
        }
        return bitmap;
    }


    public boolean saveGroupToServer(GroupDetail g) {
        final boolean[] res = {true};

        ParseFile parseFile = null;
        if (g.getGroupAvatarImgPath() != null && !g.getGroupAvatarImgPath().equals("")) {
            Bitmap bitmap = getAvatarBitmapFromPath(g.getGroupAvatarImgPath());
            if (bitmap != null) {
                parseFile = ComonUtils.saveBitmapToFile(bitmap, g.getGroupAvatarImgPath());
            }
        }

        ParseObject group = new ParseObject(DatabaseUtils.TABLE_GROUP);
        group.put(DatabaseUtils.COLUMN_GROUP_ID, g.getGroupId());
        group.put(DatabaseUtils.COLUMN_GROUP_NAME, g.getGroupName());
        group.put(DatabaseUtils.COLUMN_GROUP_AVATAR, g.getGroupAvatar());
        if (parseFile != null) {
            group.put(DatabaseUtils.GROUP_AVATAR_IMG_FILE_SERVER, parseFile);
        }

        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("saveGroupToServer", "sucessful");
                } else {
                    Log.i("saveGroupToServer", "failed error : " + e.toString());
                    res[0] = false;
                }
            }
        });
        return res[0];
    }

    public ArrayList<CategoryDetail> getCategoryDetailServerData() {
        final ArrayList<CategoryDetail> catDetailsServer = new ArrayList<CategoryDetail>();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);

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

    public boolean updateGroupToServer(ArrayList<GroupDetail> arrGroups) {
        final boolean[] res = {true};

        for (int i = 0; i < arrGroups.size(); i++) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
            query.whereEqualTo(DatabaseUtils.COLUMN_GROUP_ID, arrGroups.get(i).getGroupId());

            try {
                List<ParseObject> groups = query.find();
                for (int j = 0; j < groups.size(); j++) {
                    if (groups != null) {

                        groups.get(j).put(DatabaseUtils.COLUMN_GROUP_NAME, arrGroups.get(i).getGroupName());
                        groups.get(j).put(DatabaseUtils.COLUMN_GROUP_AVATAR, arrGroups.get(i).getGroupAvatar());

                        ParseFile parseFile = null;
                        if (arrGroups.get(j).getGroupAvatarImgPath() != null && !arrGroups.get(j).getGroupAvatarImgPath().equals("")) {
                            Bitmap bitmap = getAvatarBitmapFromPath(arrGroups.get(j).getGroupAvatarImgPath());
                            if (bitmap != null) {
                                parseFile = ComonUtils.saveBitmapToFile(bitmap, arrGroups.get(j).getGroupAvatarImgPath());
                            }
                            groups.get(j).put(DatabaseUtils.GROUP_AVATAR_IMG_FILE_SERVER, parseFile);
                        }
                        groups.get(j).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("updateGroupToServer", "sucessful");
                                } else {
                                    Log.i("updateGroupToServer", "failed error : " + e.toString());
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

    public ArrayList<GroupDetail> getGroupsServerDataByName(String name) {
        ArrayList<GroupDetail> groupsServer = null;

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
        query3.whereEqualTo(DatabaseUtils.COLUMN_GROUP_NAME, name);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null) {
                groupsServer = new ArrayList<GroupDetail>();
                for (int i = 0; i < objects.size(); i++) {
                    int groupId = objects.get(i).getInt(DatabaseUtils.COLUMN_GROUP_ID);
                    String groupName = objects.get(i).getString(DatabaseUtils.COLUMN_GROUP_NAME);
                    int groupAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_GROUP_AVATAR);

                    GroupDetail group = new GroupDetail(groupId, groupName, groupAvatar);
                    groupsServer.add(group);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return groupsServer;
    }

    public GroupDetail getGroupServerDataByName(String name) {
//        SyncUtils.connectAutoUserToParseServer();

        GroupDetail group = null;

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
        query3.whereEqualTo(DatabaseUtils.COLUMN_GROUP_NAME, name);

        try {
            ParseObject object = query3.getFirst();
            if (object != null) {
                int groupId = object.getInt(DatabaseUtils.COLUMN_GROUP_ID);
                String groupName = object.getString(DatabaseUtils.COLUMN_GROUP_NAME);
                int groupAvatar = object.getInt(DatabaseUtils.COLUMN_GROUP_AVATAR);
                group = new GroupDetail(groupId, groupName, groupAvatar);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
//            ParseUser.logOut();
        }

        return group;
    }

    public GroupDetail getGroupServerDataById(int id) {

        GroupDetail group = null;

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
        query3.whereEqualTo(DatabaseUtils.COLUMN_GROUP_ID, id);

        try {
            ParseObject object = query3.getFirst();
            if (object != null) {
                int groupId = object.getInt(DatabaseUtils.COLUMN_GROUP_ID);
                String groupName = object.getString(DatabaseUtils.COLUMN_GROUP_NAME);
                int groupAvatar = object.getInt(DatabaseUtils.COLUMN_GROUP_AVATAR);
                String avatarImgPath = null;

                ParseFile imgAvatarFile = object.getParseFile(DatabaseUtils.GROUP_AVATAR_IMG_FILE_SERVER);

                if (imgAvatarFile != null && imgAvatarFile.getDataStream() != null) {
                    //save file to cache and then set path to group
                    File savedFile = new File(mContext.getExternalCacheDir(), ComonUtils.createNewCacheGroupFileName());

                    ComonUtils.copyInputStreamToFile(imgAvatarFile.getDataStream(), savedFile);

                    avatarImgPath = savedFile.getAbsolutePath();
                }

                group = new GroupDetail(groupId, groupName, groupAvatar, avatarImgPath);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
//            ParseUser.logOut();
        }

        return group;
    }

    public ArrayList<GroupDetail> getGroupServerData() {
        ArrayList<GroupDetail> groupsServer = null;

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null) {
                groupsServer = new ArrayList<GroupDetail>();
                for (int i = 0; i < objects.size(); i++) {
                    int groupId = objects.get(i).getInt(DatabaseUtils.COLUMN_GROUP_ID);
                    String groupName = objects.get(i).getString(DatabaseUtils.COLUMN_GROUP_NAME);
                    int groupAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_GROUP_AVATAR);

                    GroupDetail group = new GroupDetail(groupId, groupName, groupAvatar);
                    groupsServer.add(group);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return groupsServer;
    }

    public boolean checkGroupExistInServer(String groupName) {
        ArrayList<GroupDetail> groups = getGroupsServerDataByName(groupName);
        if (groups != null && groups.size() > 0) {
            return true;
        }

        return false;
    }

    public GroupDetail getLogInGroupFromServer(UserDetail user) {
        UserDetailCustomServerControl control = new UserDetailCustomServerControl(mContext);
        int groupid = control.getLogInGroupIdFromServer(user);
        GroupDetail group = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
        query.whereEqualTo(DatabaseUtils.COLUMN_GROUP_ID, groupid);
        try {
            List<ParseObject> groups = query.find();
            if (groups.size() > 0) {
                String groupName = groups.get(0).getString(DatabaseUtils.COLUMN_GROUP_NAME);
                int groupAvatar = groups.get(0).getInt(DatabaseUtils.COLUMN_GROUP_AVATAR);
                group = new GroupDetail(groupid, groupName, groupAvatar);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return group;
    }

    public int getNextGroupTblId() {
        return SyncUtils.getIdInServer(DatabaseUtils.COLUMN_GROUP_ID_DATA);
    }

    public boolean deleteAllGroupServer() {
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_GROUP);
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
