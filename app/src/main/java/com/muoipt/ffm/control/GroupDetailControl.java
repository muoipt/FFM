package com.muoipt.ffm.control;

import android.content.Context;

import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.GroupDetail;
import com.muoipt.ffm.utils.AppConfig;

import java.util.ArrayList;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class GroupDetailControl {
    public static DatabaseUtils databaseUtils;

    public GroupDetailControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
    }

    public boolean checkTableGroupExistInDb(){
        boolean isExist = false;

        databaseUtils.open();
        isExist = databaseUtils.isExistTableGroup();
        databaseUtils.close();

        return isExist;
    }

    public boolean checkDataGroupExistInDb(String name){
        databaseUtils.open();

        ArrayList<GroupDetail> groups = getAllGroupsFromDB();
        if(groups == null)
            return false;

        for(int i=0; i<groups.size(); i++){
            if(groups.get(i).getGroupName().equals(name)){
                return true;
            }
        }

        databaseUtils.close();

        return false;
    }

    public boolean createTableGroup(){
        boolean res = false;

        databaseUtils.open();

        res = databaseUtils.createTableGroup();

        databaseUtils.close();

        return  res;
    }

    public ArrayList<GroupDetail> getAllGroupsFromDB(){

        databaseUtils.open();
        ArrayList<GroupDetail> arrGroups = null;
        arrGroups = databaseUtils.getAllGroups();
        databaseUtils.close();

        return arrGroups;
    }

    public void addGroup(GroupDetail group){
        databaseUtils.open();
        databaseUtils.insertGroup(group);
        databaseUtils.close();
    }

    public boolean updateGroup(GroupDetail group){
        boolean res = false;

        databaseUtils.open();
        res = databaseUtils.updateGroup(group);
        databaseUtils.close();

        return res;
    }

    //    public int findGroupIdByName(String groupName) {
    public GroupDetail findGroupByName(String groupName) {
        databaseUtils.open();
        GroupDetail group = databaseUtils.findGroupByName(groupName);
        databaseUtils.close();

        if(group != null)
            return group;
        else{
            return null;
        }
    }

    public GroupDetail findGroupById(int groupId) {
        databaseUtils.open();
        GroupDetail group = databaseUtils.findGroupById(groupId);
        databaseUtils.close();

        if(group != null)
            return group;
        else{
            return null;
        }
    }

    public int getMaxGroupId() {
        databaseUtils.open();
        int maxId = databaseUtils.getMaxGroupId();
        databaseUtils.close();

        return maxId;
    }

    public void deleteAllGroups(){
        databaseUtils.open();
        databaseUtils.deleteAllGroups();
        databaseUtils.close();
    }

    public boolean saveGroupToDB(ArrayList<GroupDetail> arrGroups) {

        for (int i = 0; i < arrGroups.size(); i++) {
            addGroup(arrGroups.get(i));
        }

        return true;
    }

    public ArrayList<GroupDetail> getGroupDBdata() {
        ArrayList<GroupDetail> groupsServer = null;

        groupsServer = getAllGroupsFromDB();

        return groupsServer;
    }

    public GroupDetail getGroupDetailById(int id) {
        GroupDetail groupDetail = null;

        databaseUtils.open();
        groupDetail = databaseUtils.getGroupDetailById(id);
        databaseUtils.close();

        return groupDetail;
    }

}
