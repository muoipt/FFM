package com.muoipt.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class CategoryDetail implements Serializable {
    private int catId;
    private String catName;
    private int catTypeId;
    private int catLimit;
    private boolean catStatus;
    private int catAvatar;
    private String catAvatarImagePath;
    private boolean isMark;
    private String catUpdatedAt;
    private boolean isCatDeleted;

    public CategoryDetail() {
    }

    public CategoryDetail(int catId, String catName, int catTypeId, int catLimit, boolean catStatus, int catAvatar, String catUpdatedAt, boolean isCatDeleted) {
        this.catId = catId;
        this.catName = catName;
        this.catTypeId = catTypeId;
        this.catLimit = catLimit;
        this.catStatus = catStatus;
        this.catAvatar = catAvatar;
        this.catUpdatedAt = catUpdatedAt;
        this.isCatDeleted = isCatDeleted;
    }

    public CategoryDetail(int catId, String catName, int catTypeId, int catLimit, boolean catStatus, int catAvatar, boolean isMark, String catUpdatedAt, boolean isCatDeleted) {
        this.catId = catId;
        this.catName = catName;
        this.catTypeId = catTypeId;
        this.catLimit = catLimit;
        this.catStatus = catStatus;
        this.catAvatar = catAvatar;
        this.isMark = isMark;
        this.catUpdatedAt = catUpdatedAt;
        this.isCatDeleted = isCatDeleted;
    }

    public CategoryDetail(int catId, String catName, int catTypeId, int catLimit, boolean catStatus, int catAvatar, String catAvatarImagePath, boolean isMark, String catUpdatedAt, boolean isCatDeleted) {
        this.catId = catId;
        this.catName = catName;
        this.catTypeId = catTypeId;
        this.catLimit = catLimit;
        this.catStatus = catStatus;
        this.catAvatar = catAvatar;
        this.catAvatarImagePath = catAvatarImagePath;
        this.isMark = isMark;
        this.catUpdatedAt = catUpdatedAt;
        this.isCatDeleted = isCatDeleted;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatTypeId() {
        return catTypeId;
    }

    public void setCatTypeId(int catTypeId) {
        this.catTypeId = catTypeId;
    }

    public int getCatLimit() {
        return catLimit;
    }

    public void setCatLimit(int catLimit) {
        this.catLimit = catLimit;
    }

    public boolean isCatStatus() {
        return catStatus;
    }

    public void setCatStatus(boolean catStatus) {
        this.catStatus = catStatus;
    }

    public int getCatAvatar() {
        return catAvatar;
    }

    public void setCatAvatar(int catAvatar) {
        this.catAvatar = catAvatar;
    }

    public String getCatUpdatedAt() {
        return catUpdatedAt;
    }

    public void setCatUpdatedAt(String catUpdatedAt) {
        this.catUpdatedAt = catUpdatedAt;
    }

    public boolean isCatDeleted() {
        return isCatDeleted;
    }

    public void setCatDeleted(boolean catDeleted) {
        isCatDeleted = catDeleted;
    }

    public boolean isMark() {
        return isMark;
    }

    public void setMark(boolean mark) {
        isMark = mark;
    }

    public String getCatAvatarImagePath() {
        return catAvatarImagePath;
    }

    public void setCatAvatarImagePath(String catAvatarImagePath) {
        this.catAvatarImagePath = catAvatarImagePath;
    }
}
