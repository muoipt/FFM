package com.xalenmy.ffm.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class UserDetailServerControl {
    private Context mContext;

    public UserDetailServerControl(Context context) {
        mContext = context;
    }

    public boolean logIn(String email, String pass) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseUser.logOut();
        }

        try {
            ParseUser.logIn(email, pass);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSignUpUserData(UserDetail userData) {
        final boolean[] res = {true};

        ParseUser user = new ParseUser();
        user.setUsername(userData.getUserEmail());
        user.setPassword(userData.getUserPassword());
        user.setEmail(userData.getUserEmail());

        user.signUpInBackground(new SignUpCallback() {
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

    public boolean updateUserToServer(UserDetail updateUser) {

        final boolean[] res = {true};

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            currentUser.setUsername(updateUser.getUserEmail());
            currentUser.setPassword(updateUser.getUserPassword());
            currentUser.setEmail(updateUser.getUserEmail());

            try {
                currentUser.save();
            } catch (Exception e) {
                Log.i("updateUser", "updateUser failed");
                res[0] = false;
            }
        }

        return res[0];
    }
}
