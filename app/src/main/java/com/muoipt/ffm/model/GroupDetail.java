package com.muoipt.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class GroupDetail implements Serializable {
    private int groupId;
    private String groupName;
    private int groupAvatar;
    private String groupAvatarImgPath;

    public GroupDetail() {
    }

    public GroupDetail(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public GroupDetail(String groupName, int groupAvatar) {
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
    }

    public GroupDetail(int groupId, String groupName, String groupAvatarImgPath) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatarImgPath = groupAvatarImgPath;
    }

    public GroupDetail(int groupId, String groupName, int groupAvatar) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
    }

    public GroupDetail(String groupName, int groupAvatar, String groupAvatarImgPath) {
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
        this.groupAvatarImgPath = groupAvatarImgPath;
    }

    public GroupDetail(int groupId, String groupName, int groupAvatar, String groupAvatarImgPath) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
        this.groupAvatarImgPath = groupAvatarImgPath;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(int groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getGroupAvatarImgPath() {
        return groupAvatarImgPath;
    }

    public void setGroupAvatarImgPath(String groupAvatarImgPath) {
        this.groupAvatarImgPath = groupAvatarImgPath;
    }
}
