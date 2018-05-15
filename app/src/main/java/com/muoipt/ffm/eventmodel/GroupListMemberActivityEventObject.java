package com.muoipt.ffm.eventmodel;

import com.muoipt.ffm.model.UserDetail;

/**
 * Created by XalenMy on 3/23/2018.
 */

public class GroupListMemberActivityEventObject {
    private String msg;
    private UserDetail u;

    public GroupListMemberActivityEventObject(UserDetail u) {
        this.u = u;
    }

    public UserDetail getObj() {
        return u;
    }

    public void setObj(UserDetail s) {
        this.u = s;
    }

    public GroupListMemberActivityEventObject(String msg, UserDetail u) {
        this.u = u;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setIndex(String msg) {
        this.msg = msg;
    }
}
