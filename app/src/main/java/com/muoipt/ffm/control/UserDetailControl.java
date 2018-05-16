package com.muoipt.ffm.control;

import android.content.Context;

import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class UserDetailControl {
    public static DatabaseUtils databaseUtils;

    public UserDetailControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
    }

    public boolean checkTableUserExistInDB(){
        boolean isExist = false;

        databaseUtils.open();
        isExist = databaseUtils.isExistTableUser();
        databaseUtils.close();

        return isExist;
    }

    public void createTableUser(){
        databaseUtils.open();
        databaseUtils.createTableUser();
        databaseUtils.close();
    }

    public ArrayList<UserDetail> getAllUsersFromDB(){

        databaseUtils.open();
        ArrayList<UserDetail> arrUsers = null;
        arrUsers = databaseUtils.getAllUsers();
        databaseUtils.close();

        return arrUsers;
    }

    public ArrayList<UserDetail> getNewUsersFromDB(){

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        ArrayList<UserDetail> arrUsers = new ArrayList<UserDetail>();

        databaseUtils.open();
        arrUsers = databaseUtils.getNewUsers(currentUser);
        databaseUtils.close();

        return arrUsers;
    }

    public ArrayList<UserDetail> getActiveUsersFromDB(UserDetail currentUser){

        ArrayList<UserDetail> arrUsers = new ArrayList<UserDetail>();
        databaseUtils.open();
        arrUsers = databaseUtils.getActiveUsers(currentUser);
        databaseUtils.close();

        return arrUsers;
    }

    public ArrayList<UserDetail> getRemovedUsersFromDB(){

        UserDetail currentUser = AppConfig.getUserLogInInfor();
        ArrayList<UserDetail> arrUsers = new ArrayList<UserDetail>();

        databaseUtils.open();
        arrUsers = databaseUtils.getRemovedUsers(currentUser);
        databaseUtils.close();

        return arrUsers;
    }

    public void addUser(UserDetail user){
        databaseUtils.open();
        databaseUtils.insertUser(user);
        databaseUtils.close();
    }

    public void updateUser(UserDetail user){
        databaseUtils.open();
        databaseUtils.updateUser(user);
        databaseUtils.close();
    }

    public boolean updateUser(ArrayList<UserDetail> users){
        databaseUtils.open();
        for(int i=0; i<users.size(); i++)
            databaseUtils.updateUser(users.get(i));
        databaseUtils.close();
        return true;
    }

    public UserDetail checkDataUserExistInDb(String email) {
        ArrayList<UserDetail> users = getAllUsersFromDB();

        for(int i=0; i<users.size(); i++){
            if(users.get(i).getUserEmail().equals(email)){
                return users.get(i);
            }
        }

        return null;
    }

    public int getGroupIdFromUser(UserDetail user) {

        ArrayList<UserDetail> users = getAllUsersFromDB();
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getUserEmail().equals(user.getUserEmail())){
                return users.get(i).getUserGroupId();
            }
        }

        return 0;
    }

    public int getUserIdFromUserInDB(UserDetail user) {
        ArrayList<UserDetail> users = getAllUsersFromDB();
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getUserEmail().equals(user.getUserEmail())){
                return users.get(i).getUserId();
            }
        }

        return -1;
    }

    public int getMaxUserId() {
        databaseUtils.open();
        int maxId = databaseUtils.getMaxUserId();
        databaseUtils.close();

        return maxId;
    }

    public void deleteAllUsers(){
        databaseUtils.open();
        databaseUtils.deleteAllUsers();
        databaseUtils.close();
    }

    public ArrayList<UserDetail> getUserDBdata() {
        ArrayList<UserDetail> usersServer = null;

        usersServer = getAllUsersFromDB();

        return usersServer;
    }

    public ArrayList<UserDetail> getAllUserInGroup() {
        ArrayList<UserDetail> usersServer = null;

        UserDetail currentUser = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        usersServer = databaseUtils.getAllUsersInGroupFromDB(currentUser);
        databaseUtils.close();

        return usersServer;
    }

    public int getLogInGroupIdFromDb(UserDetail user) {
        return getGroupIdFromUser(user);
    }

    public boolean saveUserToDB(ArrayList<UserDetail> arrUsers) {

        for (int i = 0; i < arrUsers.size(); i++) {
            addUser(arrUsers.get(i));
        }

        return true;
    }

    public int getTotalUsersInGroup() {
        UserDetail currentUser = AppConfig.getUserLogInInfor();

        if(currentUser.getUserEmail() != null) {

            databaseUtils.open();
            ArrayList<UserDetail> userList = databaseUtils.getAllUsersInGroupFromDB(currentUser);
            databaseUtils.close();

            return userList.size();
        }

        return 0;
    }
}
