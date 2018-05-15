package com.muoipt.ffm.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.muoipt.ffm.R;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 3/26/2018.
 */

public class UserDetailCustomServerControl {
    private Context mContext;

    public UserDetailCustomServerControl(Context context) {
        mContext = context;
    }

    public ArrayList<UserDetail> getUserServer() {
        final ArrayList<UserDetail>[] usersServer = new ArrayList[]{null};

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    usersServer[0] = new ArrayList<UserDetail>();
                    for (int i = 0; i < objects.size(); i++) {
                        int userId = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_ID);
                        String userEmail = objects.get(i).getString(DatabaseUtils.COLUMN_USER_EMAIL);
                        String userPassword = objects.get(i).getString(DatabaseUtils.COLUMN_USER_PASSWORD);
                        int userGroupId = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_GROUP_ID);
                        int userAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_AVATAR);
                        String userAvatarImgPath = objects.get(i).getString(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH);
                        int userStatus = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_STATUS);
                        int userRole = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_ROLE);

                        UserDetail user = new UserDetail(userId, userEmail, userPassword, userGroupId, userAvatar, userAvatarImgPath, userStatus, userRole);
                        usersServer[0].add(user);
                    }

                } else {
                    // Something went wrong.
                    Toast.makeText(mContext, "Can't get user data from server", Toast.LENGTH_LONG).show();
                }
            }
        });

        return usersServer[0];
    }

    public ArrayList<UserDetail> getUserServerData() {
        ArrayList<UserDetail> usersServer = null;

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null) {
                usersServer = new ArrayList<UserDetail>();
                for (int i = 0; i < objects.size(); i++) {
                    int userId = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_ID);
                    String userEmail = objects.get(i).getString(DatabaseUtils.COLUMN_USER_EMAIL);
                    String userPassword = objects.get(i).getString(DatabaseUtils.COLUMN_USER_PASSWORD);
                    int userGroupId = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_GROUP_ID);
                    int userAvatar = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_AVATAR);
                    String userAvatarImgPath = objects.get(i).getString(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH);
                    int userStatus = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_STATUS);
                    int userRole = objects.get(i).getInt(DatabaseUtils.COLUMN_USER_ROLE);

                    UserDetail user = new UserDetail(userId, userEmail, userPassword, userGroupId, userAvatar, userAvatarImgPath, userStatus, userRole);
                    usersServer.add(user);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return usersServer;
    }

    public boolean checkDataUserExistInServer(String email) {
        final boolean[] res = {false};

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);
        query3.whereEqualTo(DatabaseUtils.COLUMN_USER_EMAIL, email);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null && objects.size() > 0) {
                res[0] = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
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

    public boolean addSignUpUserData(UserDetail userData) {
        final boolean[] res = {true};

        ParseObject user = new ParseObject(DatabaseUtils.TABLE_USER);

        user.put(DatabaseUtils.COLUMN_USER_ID, userData.getUserId());
        user.put(DatabaseUtils.COLUMN_USER_EMAIL, userData.getUserEmail());
        user.put(DatabaseUtils.COLUMN_USER_PASSWORD, userData.getUserPassword());
        user.put(DatabaseUtils.COLUMN_USER_GROUP_ID, userData.getUserGroupId());
        user.put(DatabaseUtils.COLUMN_USER_AVATAR, userData.getUserAvatar());
        if (userData.getUserAvatarImgPath() == null) {
            user.put(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH, "");
        } else {
            user.put(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH, userData.getUserAvatarImgPath());
        }
        user.put(DatabaseUtils.COLUMN_USER_STATUS, userData.getUserStatus());
        user.put(DatabaseUtils.COLUMN_USER_ROLE, userData.getUserRole());

        ParseFile parseFile = null;
        if (userData.getUserAvatarImgPath() != null && !userData.getUserAvatarImgPath().equals("")) {
            Bitmap bitmap = getAvatarBitmapFromPath(userData.getUserAvatarImgPath());
            if (bitmap != null) {
                parseFile = ComonUtils.saveBitmapToFile(bitmap, userData.getUserAvatarImgPath());
            }

            user.put(DatabaseUtils.USER_AVATAR_IMG_FILE, parseFile);
        }

        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    res[0] = true;
                } else {
                    res[0] = false;
                }
            }
        });

        return res[0];
    }

    public int getLogInGroupIdFromServer(UserDetail user) {
        int groupid = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);
        query.whereEqualTo(DatabaseUtils.COLUMN_USER_EMAIL, user.getUserEmail());
        try {
            List<ParseObject> users = query.find();
            if (users.size() > 0) {
                groupid = users.get(0).getInt(DatabaseUtils.COLUMN_USER_GROUP_ID);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return groupid;
    }

    public int getUserIdFromUserInServer(UserDetail user) {
        int userId = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);
        query.whereEqualTo(DatabaseUtils.COLUMN_USER_EMAIL, user.getUserEmail());
        try {
            ParseObject userObject = query.getFirst();
            if (userObject != null) {
                userId = userObject.getInt(DatabaseUtils.COLUMN_USER_ID);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public int getNextUserTblId() {
        return SyncUtils.getIdInServer(DatabaseUtils.COLUMN_USER_ID_DATA);
    }

    public UserDetail getLogInUserFromServer(String logInEmail) {
        UserDetail user = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);
        query.whereEqualTo(DatabaseUtils.COLUMN_USER_EMAIL, logInEmail);

        try {
            ParseObject object = query.getFirst();
            if (object != null) {
                int logInUserId = object.getInt(DatabaseUtils.COLUMN_USER_ID);
                int logInGroupId = object.getInt(DatabaseUtils.COLUMN_USER_GROUP_ID);
                int logInUserAvatar = object.getInt(DatabaseUtils.COLUMN_USER_AVATAR);
                String logInUserAvatarImgPath = object.getString(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH);
                int logInUserStatus = object.getInt(DatabaseUtils.COLUMN_USER_STATUS);
                int logInUserRole = object.getInt(DatabaseUtils.COLUMN_USER_ROLE);
                user = new UserDetail();
                user.setUserId(logInUserId);
                user.setUserGroupId(logInGroupId);
                user.setUserAvatar(logInUserAvatar);
                user.setUserAvatarImgPath(logInUserAvatarImgPath);
                user.setUserStatus(logInUserStatus);
                user.setUserRole(logInUserRole);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean deleteAllUserServer() {
        ParseQuery<ParseUser> query5 = ParseUser.getQuery();
        try {
            List<ParseUser> objs = query5.find();
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

    public boolean deleteAllUserSessionServer() {
        ParseQuery<ParseSession> query6 = ParseSession.getQuery();
        try {
            List<ParseSession> objs = query6.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++) {
                    objs.get(i).delete();

                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void setUserServerWritable(ParseUser user) {
        ParseObject groupMessage = new ParseObject("User");
        ParseACL groupACL = new ParseACL();

        groupACL.setPublicReadAccess(true);
        groupACL.setPublicWriteAccess(true);

        groupMessage.setACL(groupACL);
        try {
            groupMessage.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUserToServer(List<UserDetail> userChangedList) {

        final boolean[] res = {true};

        for (UserDetail u : userChangedList) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_USER);
            query.whereEqualTo(DatabaseUtils.COLUMN_USER_EMAIL, u.getUserEmail());

            try {
                ParseObject user = query.getFirst();
                if (user != null) {
                    user.put(DatabaseUtils.COLUMN_USER_EMAIL, u.getUserEmail());
                    user.put(DatabaseUtils.COLUMN_USER_PASSWORD, u.getUserPassword());
                    user.put(DatabaseUtils.COLUMN_USER_GROUP_ID, u.getUserGroupId());
                    user.put(DatabaseUtils.COLUMN_USER_AVATAR, u.getUserAvatar());
                    if (u.getUserAvatarImgPath() == null) {
                        user.put(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH, "");
                    } else {
                        user.put(DatabaseUtils.COLUMN_USER_AVATAR_IMG_PATH, u.getUserAvatarImgPath());
                    }
                    user.put(DatabaseUtils.COLUMN_USER_STATUS, u.getUserStatus());
                    user.put(DatabaseUtils.COLUMN_USER_ROLE, u.getUserRole());

                    ParseFile parseFile = null;
                    if (u.getUserAvatarImgPath() != null && !u.getUserAvatarImgPath().equals("")) {
                        Bitmap bitmap = getAvatarBitmapFromPath(u.getUserAvatarImgPath());
                        if (bitmap != null) {
                            parseFile = ComonUtils.saveBitmapToFile(bitmap, u.getUserAvatarImgPath());
                        }
                        user.put(DatabaseUtils.USER_AVATAR_IMG_FILE, parseFile);
                    }
                    try {
                        user.save();
                    } catch (Exception e) {
                        Log.i("updateUser", "updateUser failed");
                        res[0] = false;
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return res[0];
    }

}
