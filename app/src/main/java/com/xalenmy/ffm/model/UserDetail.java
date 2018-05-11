package com.xalenmy.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class UserDetail implements Serializable {
    private int userId;
    private String userEmail;
    private String userPassword;
    private int userGroupId;
    private int userAvatar;
    private String userAvatarImgPath;
    private int userStatus;
    private int userRole;

    public UserDetail() {
    }

    public UserDetail(String userEmail, String userPassword, int userAvatar) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAvatar = userAvatar;
    }

    public UserDetail(String userEmail, String userPassword, int userAvatar, int userStatus, int userRole) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAvatar = userAvatar;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public UserDetail(String userEmail, String userPassword, int userGroupId, int userAvatar) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGroupId = userGroupId;
        this.userAvatar = userAvatar;
    }

    public UserDetail(int userId, String userEmail, String userPassword, int userGroupId, int userAvatar) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGroupId = userGroupId;
        this.userAvatar = userAvatar;
    }

    public UserDetail(int userId, String userEmail, String userPassword, int userGroupId, int userAvatar, int userStatus, int userRole) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGroupId = userGroupId;
        this.userAvatar = userAvatar;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public UserDetail(int userId, String userEmail, String userPassword, int userGroupId, int userAvatar, String userAvatarImgPath,int userStatus, int userRole) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGroupId = userGroupId;
        this.userAvatar = userAvatar;
        this.userAvatarImgPath = userAvatarImgPath;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public UserDetail(String userEmail, String userPassword, int userAvatar, String userAvatarImgPath,int userStatus, int userRole) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAvatar = userAvatar;
        this.userAvatarImgPath = userAvatarImgPath;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public UserDetail(String email) {
        this.userEmail = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserAvatarImgPath() {
        return userAvatarImgPath;
    }

    public void setUserAvatarImgPath(String userAvatarImgPath) {
        this.userAvatarImgPath = userAvatarImgPath;
    }
}
