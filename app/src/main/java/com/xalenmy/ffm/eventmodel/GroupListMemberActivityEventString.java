package com.xalenmy.ffm.eventmodel;

/**
 * Created by XalenMy on 3/29/2018.
 */

public class GroupListMemberActivityEventString {
    private String s;
    private int index;

    public GroupListMemberActivityEventString(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public GroupListMemberActivityEventString(String s, int index) {
        this.s = s;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
