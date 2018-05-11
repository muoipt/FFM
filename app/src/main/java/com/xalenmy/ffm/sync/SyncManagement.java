package com.xalenmy.ffm.sync;

import android.content.Context;

import com.xalenmy.ffm.control.CategoryDetailControl;
import com.xalenmy.ffm.control.CategoryDetailServerControl;
import com.xalenmy.ffm.control.GroupDetailControl;
import com.xalenmy.ffm.control.GroupDetailServerControl;
import com.xalenmy.ffm.control.ReportDetailControl;
import com.xalenmy.ffm.control.ReportDetailServerControl;
import com.xalenmy.ffm.control.TypeDetailServerControl;
import com.xalenmy.ffm.control.TypeDetaillControl;
import com.xalenmy.ffm.control.UserDetailControl;
import com.xalenmy.ffm.control.UserDetailCustomServerControl;
import com.xalenmy.ffm.control.UserDetailServerControl;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.GroupDetail;
import com.xalenmy.ffm.model.ReportDetail;
import com.xalenmy.ffm.model.TypeDetail;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.ComonUtils;
import com.xalenmy.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class SyncManagement {
    private Context mContext;
    private ArrayList<String> changeTypes;

    private ArrayList<GroupDetail> groupServerList = null;
    private ArrayList<UserDetail> userServerList = null;
    private ArrayList<TypeDetail> typeDetailsServerList = null;
    private ArrayList<CategoryDetail> catDetailsServerList = null;
    private ArrayList<ReportDetail> reportDetailsServerList = null;

    private ArrayList<GroupDetail> groupDBList = null;
    private ArrayList<UserDetail> userDBList = null;
    private ArrayList<TypeDetail> typeDetailsDBList = null;
    private ArrayList<CategoryDetail> catDetailsDBList = null;
    private ArrayList<ReportDetail> reportDetailsDBList = null;

    private ArrayList<GroupDetail> groupUpdateServerList = null;
    private ArrayList<UserDetail> userUpdateServerList = null;
    private ArrayList<TypeDetail> typeDetailsUpdateServerList = null;
    private ArrayList<CategoryDetail> catDetailsUpdateServerList = null;
    private ArrayList<ReportDetail> reportDetailsUpdateServerList = null;

    private ArrayList<CategoryDetail> catDetailsUpdateDbList = null;
    private ArrayList<ReportDetail> reportDetailsUpdateDbList = null;
    private ArrayList<UserDetail> userDetailsUpdateDbList = null;

    private CategoryDetailControl categoryDetailControl;
    private CategoryDetailServerControl categoryDetailServerControl;
    private GroupDetailControl groupDetailControl;
    private GroupDetailServerControl groupDetailServerControl;
    private ReportDetailControl reportDetailControl;
    private ReportDetailServerControl reportDetailServerControl;
    private TypeDetaillControl typeDetaillControl;
    private TypeDetailServerControl typeDetailServerControl;
    private UserDetailControl userDetailControl;
    private UserDetailCustomServerControl userDetailCustomServerControl;

    public SyncManagement(Context context) {
        mContext = context;
    }

    public void prepareSync() {
        changeTypes = new ArrayList<String>();

        categoryDetailControl = new CategoryDetailControl(mContext);
        categoryDetailServerControl = new CategoryDetailServerControl(mContext);
        groupDetailControl = new GroupDetailControl(mContext);
        groupDetailServerControl = new GroupDetailServerControl(mContext);
        reportDetailControl = new ReportDetailControl(mContext);
        reportDetailServerControl = new ReportDetailServerControl(mContext);
        typeDetaillControl = new TypeDetaillControl(mContext);
        typeDetailServerControl = new TypeDetailServerControl(mContext);
        userDetailControl = new UserDetailControl(mContext);
        userDetailCustomServerControl = new UserDetailCustomServerControl(mContext);

        groupServerList = new ArrayList<GroupDetail>();
        groupDBList = new ArrayList<GroupDetail>();
        groupUpdateServerList = new ArrayList<GroupDetail>();

        userServerList = new ArrayList<UserDetail>();
        userDBList = new ArrayList<UserDetail>();
        userUpdateServerList = new ArrayList<UserDetail>();

        typeDetailsServerList = new ArrayList<TypeDetail>();
        typeDetailsDBList = new ArrayList<TypeDetail>();
        typeDetailsUpdateServerList = new ArrayList<TypeDetail>();

        catDetailsServerList = new ArrayList<CategoryDetail>();
        catDetailsDBList = new ArrayList<CategoryDetail>();
        catDetailsUpdateServerList = new ArrayList<CategoryDetail>();

        reportDetailsServerList = new ArrayList<ReportDetail>();
        reportDetailsDBList = new ArrayList<ReportDetail>();
        reportDetailsUpdateServerList = new ArrayList<ReportDetail>();

        catDetailsUpdateDbList = new ArrayList<CategoryDetail>();
        reportDetailsUpdateDbList = new ArrayList<ReportDetail>();
        userDetailsUpdateDbList = new ArrayList<UserDetail>();
    }

    public boolean startSync() {
        prepareSync();

        boolean needSave = false;

        if (checkUserBetweenDBandServer()) {
            needSave = true;
        }

        if (checkTypeDetailBetweenDBandServer()) {
            needSave = true;
        }
        if (checkCategoryDetailBetweenDBandServer()) {
            needSave = true;
        }
        if (checkReportDetailBetweenDBandServer()) {
            needSave = true;
        }

        if (needSave) {
            for (int i = 0; i < changeTypes.size(); i++) {
                if (!processSaveDataSynced(changeTypes.get(i))) {
                    return false;
                }
            }

        }

        return true;
    }

    private boolean processSaveDataSynced(String changeType) {
        switch (changeType) {
//            case SyncUtils.CHANGE_GROUP_ADD_IN_DB_FROM_SERVER:
//                if (!saveGroupToDB())
//                    return false;
//                break;
//            case SyncUtils.CHANGE_GROUP_ADD_IN_SERVER_FROM_DB:
//                if (!saveGroupToServer())
//                    return false;
//                break;
            case SyncUtils.CHANGE_USER_ADD_IN_DB_FROM_SERVER:
                if (!saveUserToDB())
                    return false;
                break;
            case SyncUtils.CHANGE_USER_UPDATE_IN_DB_FROM_SERVER:
                if (!updateUserToDB())
                    return false;
                break;
            case SyncUtils.CHANGE_USER_UPDATE_IN_SERVER_FROM_DB:
                if (!updateUserToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_TYPE_ADD_IN_DB_FROM_SERVER:
                if (!saveTypeDetailToDB())
                    return false;
                break;
            case SyncUtils.CHANGE_TYPE_ADD_IN_SERVER_FROM_DB:
                if (!saveTypeDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_CAT_ADD_IN_DB_FROM_SERVER:
                if (!saveCategoryDetailToDB())
                    return false;
                break;
            case SyncUtils.CHANGE_CAT_ADD_IN_SERVER_FROM_DB:
                if (!saveCategoryDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_REPORT_ADD_IN_DB_FROM_SERVER:
                if (!saveReportDetailToDB())
                    return false;
                break;
            case SyncUtils.CHANGE_REPORT_ADD_IN_SERVER_FROM_DB:
                if (!saveReportDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_GROUP_UPDATE_IN_SERVER_FROM_DB:
                if (!updateGroupToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_TYPE_UPDATE_IN_SERVER_FROM_DB:
                if (!updateTypeDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_CAT_UPDATE_IN_SERVER_FROM_DB:
                if (!updateCategoryDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_CAT_UPDATE_IN_DB_FROM_SERVER:
                if (!updateCategoryDetailToDb())
                    return false;
                break;
            case SyncUtils.CHANGE_REPORT_UPDATE_IN_SERVER_FROM_DB:
                if (!updateReportDetailToServer())
                    return false;
                break;
            case SyncUtils.CHANGE_REPORT_UPDATE_IN_DB_FROM_SERVER:
                if (!updateReportDetailToDb())
                    return false;
                break;
        }

        return true;
    }

    private boolean checkUserBetweenDBandServer() {
        boolean isChange = false;

        ArrayList<UserDetail> usersServer = userDetailCustomServerControl.getUserServerData();
        ArrayList<UserDetail> usersDB = userDetailControl.getUserDBdata();

        //get typedetail from server to add to DB
        if (usersDB.size() < usersServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_USER_ADD_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_USER_ADD_IN_DB_FROM_SERVER);
            }

            userDBList = processGetUserChange(usersServer, usersDB);

        } else if (usersDB.size() > usersServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_USER_ADD_IN_SERVER_FROM_DB, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_USER_ADD_IN_SERVER_FROM_DB);
            }

            userServerList = processGetUserChange(usersDB, usersServer);
        }

        //check change in each type details
        //only allow update local from server
        boolean isUpdate = false;
        for (int i = 0; i < usersDB.size(); i++) {
            UserDetail t1 = usersDB.get(i);
            for (int j = 0; j < usersServer.size(); j++) {
                UserDetail t2 = usersServer.get(j);
                if (checkDiffUser(t1, t2)) {
                    isChange = true;
                    isUpdate = true;
                    userDetailsUpdateDbList.add(t2);
                }
            }
        }

        if (isUpdate) {
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_USER_UPDATE_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_USER_UPDATE_IN_DB_FROM_SERVER);
            }
        }

        return isChange;
    }

    private boolean checkDiffUser(UserDetail u1, UserDetail u2) {
        if (u1.getUserId() == u2.getUserId() && u1.getUserStatus() != u2.getUserStatus())
            return true;
        if (u1.getUserId() == u2.getUserId() && u1.getUserRole() != u2.getUserRole())
            return true;
        if (u1.getUserId() == u2.getUserId() && u1.getUserAvatar() != u2.getUserAvatar())
            return true;
        if (u1.getUserId() == u2.getUserId() && u1.getUserPassword() != u2.getUserPassword())
            return true;

        return false;
    }

    private ArrayList<UserDetail> processGetUserChange(ArrayList<UserDetail> users1, ArrayList<UserDetail> users2) {
        ArrayList<UserDetail> arr = new ArrayList<UserDetail>();

        for (int i = 0; i < users1.size(); i++) {
            UserDetail user = users1.get(i);
            if (getUserByIdInList(user.getUserId(), users2) == null) {
                arr.add(user);
            }
        }

        return arr;
    }

    private Object getUserByIdInList(int id, ArrayList<UserDetail> users2) {
        for (int i = 0; i < users2.size(); i++) {
            UserDetail user = users2.get(i);
            if (user.getUserId() == id) {
                return user;
            }
        }

        return null;
    }

    private boolean saveUserToDB() {
        return userDetailControl.saveUserToDB(userDBList);
    }

    private boolean updateUserToServer() {
        return userDetailCustomServerControl.updateUserToServer(userUpdateServerList);
    }

    private boolean updateUserToDB() {
        return userDetailControl.updateUser(userDetailsUpdateDbList);
    }

/////////////////////////////SYNC GROUP DETAIL//////////////////////////////

    private boolean checkGroupBetweenDBandServer() {
        boolean isChange = false;

        ArrayList<GroupDetail> groupsServer = groupDetailServerControl.getGroupServerData();
        ArrayList<GroupDetail> groupsDB = groupDetailControl.getGroupDBdata();

        //get typedetail from server to add to DB
        if (groupsDB.size() < groupsServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_GROUP_ADD_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_GROUP_ADD_IN_DB_FROM_SERVER);
            }

            groupDBList = processGetGroupChange(groupsServer, groupsDB);

        } else if (groupsDB.size() > groupsServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_GROUP_ADD_IN_SERVER_FROM_DB, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_GROUP_ADD_IN_SERVER_FROM_DB);
            }

            groupServerList = processGetGroupChange(groupsDB, groupsServer);
        }

        //check change in each type details
        //only allow local update to server
        boolean isUpdate = false;
        for (int i = 0; i < groupsDB.size(); i++) {
            GroupDetail t1 = groupsDB.get(i);
            for (int j = 0; j < groupsServer.size(); j++) {
                GroupDetail t2 = groupsServer.get(j);
                if (checkDiffGroup(t1, t2)) {
                    isChange = true;
                    isUpdate = true;
                    groupUpdateServerList.add(t1);
                }
            }
        }

        if (isUpdate) {
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_GROUP_UPDATE_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_GROUP_UPDATE_IN_DB_FROM_SERVER);
            }
        }

        return isChange;
    }

    private boolean checkDiffGroup(GroupDetail t1, GroupDetail t2) {
        if (!t1.getGroupName().equals(t2.getGroupName()) && t1.getGroupId() == t2.getGroupId())
            return true;

        return false;
    }

    private ArrayList<GroupDetail> processGetGroupChange(ArrayList<GroupDetail> groups1, ArrayList<GroupDetail> groups2) {

        ArrayList<GroupDetail> arr = new ArrayList<GroupDetail>();

        for (int i = 0; i < groups1.size(); i++) {
            GroupDetail group = groups1.get(i);
            if (getGroupByIdInList(group.getGroupId(), groups2) == null) {
                arr.add(group);
            }
        }

        return arr;
    }

    private Object getGroupByIdInList(int id, ArrayList<GroupDetail> groups2) {
        for (int i = 0; i < groups2.size(); i++) {
            GroupDetail group = groups2.get(i);
            if (group.getGroupId() == id) {
                return group;
            }
        }

        return null;
    }

    private boolean saveGroupToDB() {
        return groupDetailControl.saveGroupToDB(groupDBList);
    }

    private boolean saveGroupToServer() {
        return groupDetailServerControl.saveGroupToServer(groupServerList);
    }

    private boolean updateGroupToServer() {
        return groupDetailServerControl.updateGroupToServer(groupUpdateServerList);
    }

/////////////////////////////SYNC TYPE DETAIL//////////////////////////////
    
    public boolean checkTypeDetailBetweenDBandServer() {
        boolean isChange = false;

        ArrayList<TypeDetail> typeDetailsServer = typeDetailServerControl.getTypeDetailServerDataByUser();
        ArrayList<TypeDetail> typeDetailsDB = typeDetaillControl.getTypeDetailDBdata();

        //get typedetail from server to add to DB
        if (typeDetailsDB.size() < typeDetailsServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_TYPE_ADD_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_TYPE_ADD_IN_DB_FROM_SERVER);
            }

            typeDetailsDBList = processGetTypeChange(typeDetailsServer, typeDetailsDB);

        } else if (typeDetailsDB.size() > typeDetailsServer.size()) {
            isChange = true;
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_TYPE_ADD_IN_SERVER_FROM_DB, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_TYPE_ADD_IN_SERVER_FROM_DB);
            }

            typeDetailsServerList = processGetTypeChange(typeDetailsDB, typeDetailsServer);
        }

        //check change in each type details
        //only allow local update to server
        boolean isUpdate = false;
        for (int i = 0; i < typeDetailsDB.size(); i++) {
            TypeDetail t1 = typeDetailsDB.get(i);
            for (int j = 0; j < typeDetailsServer.size(); j++) {
                TypeDetail t2 = typeDetailsServer.get(j);
                if (checkDiffTypeDetail(t1, t2)) {
                    isChange = true;
                    isUpdate = true;
                    typeDetailsUpdateServerList.add(t1);
                }
            }
        }

        if (isUpdate) {
            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_TYPE_UPDATE_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_TYPE_UPDATE_IN_DB_FROM_SERVER);
            }
        }

        return isChange;
    }

    private TypeDetail getTypeDetailByIdInList(int id, ArrayList<TypeDetail> typeDetails) {
        for (int i = 0; i < typeDetails.size(); i++) {
            TypeDetail typeDetail = typeDetails.get(i);
            if (typeDetail.getTypeId() == id) {
                return typeDetail;
            }
        }

        return null;
    }

    private boolean checkDiffTypeDetail(TypeDetail t1, TypeDetail t2) {
        if (!t1.getTypeName().equals(t2.getTypeName()) && t1.getTypeId() == t2.getTypeId())
            return true;

        return false;
    }

    private ArrayList<TypeDetail> processGetTypeChange(ArrayList<TypeDetail> typeDetailsS1, ArrayList<TypeDetail> typeDetailsS2) {

        ArrayList<TypeDetail> arr = new ArrayList<TypeDetail>();

        for (int i = 0; i < typeDetailsS1.size(); i++) {
            TypeDetail typeDetail = typeDetailsS1.get(i);
            if (getTypeDetailByIdInList(typeDetail.getTypeId(), typeDetailsS2) == null) {
                arr.add(typeDetail);
            }
        }

        return arr;
    }

    private boolean saveTypeDetailToDB() {
        return typeDetaillControl.saveTypeDetailToDB(typeDetailsDBList);
    }

    private boolean saveTypeDetailToServer() {
        return typeDetailServerControl.saveTypeDetailToServer(typeDetailsServerList);
    }

    private boolean updateTypeDetailToServer() {
        return typeDetailServerControl.updateTypeDetailToServer(typeDetailsUpdateServerList);
    }

    /////////////////////////////SYNC CATEGORY DETAIL//////////////////////////////

    public boolean checkCategoryDetailBetweenDBandServer() {
        boolean isAdd = false;
        boolean isUpdate = false;

        ArrayList<CategoryDetail> categoryDetailDB = categoryDetailControl.getAllCateDetailDBdata();
        ArrayList<CategoryDetail> categoryDetailServer = categoryDetailServerControl.getCategoryDetailServerDataByUser();

        //kiem tra tung cat db so voi tung cat server
        //neu ben nao chua co thi add vao ben do
        //neu 2 ben deu co roi(check id) thi kiem tra update

        //kiem tra phia server
        for (int i = 0; i < categoryDetailDB.size(); i++) {
            CategoryDetail catDb = categoryDetailDB.get(i);

            CategoryDetail catExistServer = getCatExistInList(catDb, categoryDetailServer);
            if (catExistServer == null) {
                //add to list add server
                catDetailsServerList.add(catDb);
                if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_CAT_ADD_IN_SERVER_FROM_DB, changeTypes)) {
                    changeTypes.add(SyncUtils.CHANGE_CAT_ADD_IN_SERVER_FROM_DB);
                }
                isAdd = true;
            } else {
                //check update
                if (checkDiffCategoryDetail(catDb, catExistServer)) {
                    isUpdate = true;
                }
            }
        }

        //kiem tra phia DB
        for (int i = 0; i < categoryDetailServer.size(); i++) {
            CategoryDetail catServer = categoryDetailServer.get(i);

            CategoryDetail catExistDb = getCatExistInList(catServer, categoryDetailDB);
            if (catExistDb == null) {
                //add to list add db
                catDetailsDBList.add(catServer);
                if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_CAT_ADD_IN_DB_FROM_SERVER, changeTypes)) {
                    changeTypes.add(SyncUtils.CHANGE_CAT_ADD_IN_DB_FROM_SERVER);
                }
                isAdd = true;
            } else {
                //check update
                if (checkDiffCategoryDetail(catExistDb, catServer)) {
                    isUpdate = true;
                }
            }
        }

        return isAdd || isUpdate;
    }

    //lay ve category tuong ung voi catDb from server
    private CategoryDetail getCatExistInList(CategoryDetail cat, ArrayList<CategoryDetail> catList) {
        for (int i = 0; i < catList.size(); i++) {
            if (catList.get(i).getCatId() == cat.getCatId()) {
                return catList.get(i);
            }
        }

        return null;
    }

    private boolean processUpdateCategory(ArrayList<CategoryDetail> categoryDetailDB, ArrayList<CategoryDetail> categoryDetailServer) {
        boolean isChange = false;
        for (int i = 0; i < categoryDetailDB.size(); i++) {
            CategoryDetail catDb = categoryDetailDB.get(i);
            for (int j = 0; j < categoryDetailServer.size(); j++) {
                CategoryDetail catServer = categoryDetailServer.get(j);
                if (checkDiffCategoryDetail(catDb, catServer)) {
                    isChange = true;
                }
            }
        }

        return isChange;
    }

    private CategoryDetail getCategoryDetailByIdInList(int id, ArrayList<CategoryDetail> categoryDetail) {
        for (int i = 0; i < categoryDetail.size(); i++) {
            CategoryDetail CategoryDetail = categoryDetail.get(i);
            if (CategoryDetail.getCatId() == id) {
                return CategoryDetail;
            }
        }

        return null;
    }

    //check t1 va cateServer co khac data nhau ko
    //neu khac thi cap nhat cai nao theo cai nao theo updatedAt
    private boolean checkDiffCategoryDetail(CategoryDetail catDb, CategoryDetail cateServer) {
        boolean res = false;

        if (!catDb.getCatName().equals(cateServer.getCatName()) && catDb.getCatId() == cateServer.getCatId()) {
            res = true;
        } else if (catDb.getCatLimit() != cateServer.getCatLimit() && catDb.getCatId() == cateServer.getCatId())
            res = true;
        else if (catDb.isCatStatus() != cateServer.isCatStatus() && catDb.getCatId() == cateServer.getCatId())
            res = true;
        else if (catDb.getCatAvatar() != cateServer.getCatAvatar() && catDb.getCatId() == cateServer.getCatId())
            res = true;
        else if (catDb.isCatDeleted() != cateServer.isCatDeleted() && catDb.getCatId() == cateServer.getCatId())
            res = true;

        if (res) {
            checkTimeCategoryUpdatedAt(catDb, cateServer);
        }
        return res;
    }

    //kiem tra updatedTime cua catDb va catServer xem cai nao updated moi nhat
    private void checkTimeCategoryUpdatedAt(CategoryDetail catDb, CategoryDetail catServer) {
        String catDbUpdatedTime = catDb.getCatUpdatedAt();
        String catServerUpdatedTime = catServer.getCatUpdatedAt();

        Date dateDb = new Date(catDbUpdatedTime);
        Date dateServer = new Date(catServerUpdatedTime);

        //user update in DB moi hon server
        //thi update server theo DB
        if (dateDb.after(dateServer)) {
            if (!ComonUtils.checkItemExistInList(catDb, catDetailsUpdateServerList)) {
                catDetailsUpdateServerList.add(catDb);
            }

            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_CAT_UPDATE_IN_SERVER_FROM_DB, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_CAT_UPDATE_IN_SERVER_FROM_DB);
            }
        } else {
            if (!ComonUtils.checkItemExistInList(catServer, catDetailsUpdateDbList)) {
                catDetailsUpdateDbList.add(catServer);
            }

            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_CAT_UPDATE_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_CAT_UPDATE_IN_DB_FROM_SERVER);
            }
        }
    }

    //kiem tra updatedTime cua catDb va catServer xem cai nao updated moi nhat
    private void checkTimeReportUpdatedAt(ReportDetail reportDb, ReportDetail reportServer) {
        String reportDbUpdatedTime = reportDb.getReportUpdatedAt();
        String reportServerUpdatedTime = reportServer.getReportUpdatedAt();

        Date dateDb = new Date(reportDbUpdatedTime);
        Date dateServer = new Date(reportServerUpdatedTime);

        //user update in DB moi hon server
        //thi update server theo DB
        if (dateDb.after(dateServer)) {
            if (!ComonUtils.checkItemExistInList(reportDb, reportDetailsUpdateServerList)) {
                reportDetailsUpdateServerList.add(reportDb);
            }

            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_REPORT_UPDATE_IN_SERVER_FROM_DB, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_REPORT_UPDATE_IN_SERVER_FROM_DB);
            }
        } else {
            if (!ComonUtils.checkItemExistInList(reportServer, reportDetailsUpdateDbList)) {
                reportDetailsUpdateDbList.add(reportServer);
            }

            if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_REPORT_UPDATE_IN_DB_FROM_SERVER, changeTypes)) {
                changeTypes.add(SyncUtils.CHANGE_REPORT_UPDATE_IN_DB_FROM_SERVER);
            }
        }
    }

    //update t1 theo t2
    private void updateChangedCategory(CategoryDetail t1, CategoryDetail t2) {
        t1.setCatName(t2.getCatName());
        t1.setCatLimit(t2.getCatLimit());
        t1.setCatStatus(t2.isCatStatus());
        t1.setCatAvatar(t2.getCatAvatar());
    }

    private ArrayList<CategoryDetail> processGetCatChange(ArrayList<CategoryDetail> categoryDetailS1, ArrayList<CategoryDetail> categoryDetailS2) {

        ArrayList<CategoryDetail> arr = new ArrayList<CategoryDetail>();

        for (int i = 0; i < categoryDetailS1.size(); i++) {
            CategoryDetail CategoryDetail = categoryDetailS1.get(i);
            if (getCategoryDetailByIdInList(CategoryDetail.getCatId(), categoryDetailS2) == null) {
                arr.add(CategoryDetail);
            }
        }

        return arr;
    }

    private boolean saveCategoryDetailToDB() {
        return categoryDetailControl.saveCatDetailToDB(catDetailsDBList);
    }

    private boolean saveCategoryDetailToServer() {
        return categoryDetailServerControl.saveCatDetailToServer(catDetailsServerList);
    }

    private boolean updateCategoryDetailToServer() {
        return categoryDetailServerControl.updateCatDetailToServer(catDetailsUpdateServerList);
    }

    /////////////////////////////SYNC REPORT DETAIL//////////////////////////////

    public boolean checkReportDetailBetweenDBandServer() {
        boolean isAdd = false;
        boolean isUpdate = false;

        ArrayList<ReportDetail> reportDetailDB = reportDetailControl.getReportDetailDBdataByUser();
        ArrayList<ReportDetail> reportDetailServer = reportDetailServerControl.getReportDetailServerDataByUser();

        //kiem tra phia server
        for (int i = 0; i < reportDetailDB.size(); i++) {
            ReportDetail reportDb = reportDetailDB.get(i);

            ReportDetail reportExistServer = getReportExistInList(reportDb, reportDetailServer);
            if (reportExistServer == null) {
                //add to list add server
                reportDetailsServerList.add(reportDb);
                if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_REPORT_ADD_IN_SERVER_FROM_DB, changeTypes)) {
                    changeTypes.add(SyncUtils.CHANGE_REPORT_ADD_IN_SERVER_FROM_DB);
                }
                isAdd = true;
            } else {
                //check update
                if (checkDiffReportDetail(reportDb, reportExistServer)) {
                    isUpdate = true;
                }
            }
        }

        //kiem tra phia DB
        for (int i = 0; i < reportDetailServer.size(); i++) {
            ReportDetail reportServer = reportDetailServer.get(i);

            ReportDetail reportExistDb = getReportExistInList(reportServer, reportDetailDB);
            if (reportExistDb == null) {
                //add to list add db
                reportDetailsDBList.add(reportServer);
                if (!ComonUtils.checkItemExistInList(SyncUtils.CHANGE_REPORT_ADD_IN_DB_FROM_SERVER, changeTypes)) {
                    changeTypes.add(SyncUtils.CHANGE_REPORT_ADD_IN_DB_FROM_SERVER);
                }
                isAdd = true;
            } else {
                //check update
                if (checkDiffReportDetail(reportExistDb, reportServer)) {
                    isUpdate = true;
                }
            }
        }

        return isAdd || isUpdate;

    }

    private ReportDetail getReportExistInList(ReportDetail reportDb, ArrayList<ReportDetail> reportList) {
        for (int i = 0; i < reportList.size(); i++) {
            if (reportList.get(i).getReportId() == reportDb.getReportId()) {
                return reportList.get(i);
            }
        }

        return null;
    }

    private ReportDetail getReportDetailByIdInList(int id, ArrayList<ReportDetail> reportDetail) {
        for (int i = 0; i < reportDetail.size(); i++) {
            ReportDetail ReportDetail = reportDetail.get(i);
            if (ReportDetail.getReportId() == id) {
                return ReportDetail;
            }
        }

        return null;
    }

    private boolean checkDiffReportDetail(ReportDetail reportDb, ReportDetail reportServer) {
        boolean res = false;

        if (!reportDb.getReportDatetime().equals(reportServer.getReportDatetime()) && reportDb.getReportId() == reportServer.getReportId()) {
            res = true;
        } else if (reportDb.getReportAmount() != reportServer.getReportAmount() && reportDb.getReportId() == reportServer.getReportId())
            res = true;
        else if (reportDb.getReportCatId() != reportServer.getReportCatId() && reportDb.getReportId() == reportServer.getReportId())
            res = true;
        else if (!reportDb.getReportNote().equals(reportServer.getReportNote()) && reportDb.getReportId() == reportServer.getReportId())
            res = true;
        else if (!reportDb.getReportTitle().equals(reportServer.getReportTitle()) && reportDb.getReportId() == reportServer.getReportId())
            res = true;
        else if (reportDb.isReportDeleted() != reportServer.isReportDeleted() && reportDb.getReportId() == reportServer.getReportId())
            res = true;

        if (res) {
            checkTimeReportUpdatedAt(reportDb, reportServer);
        }
        return res;
    }

    private ArrayList<ReportDetail> processGetReportChange(ArrayList<ReportDetail> reportDetailS1, ArrayList<ReportDetail> reportDetailS2) {

        ArrayList<ReportDetail> arr = new ArrayList<ReportDetail>();

        for (int i = 0; i < reportDetailS1.size(); i++) {
            ReportDetail ReportDetail = reportDetailS1.get(i);
            if (getReportDetailByIdInList(ReportDetail.getReportId(), reportDetailS2) == null) {
                arr.add(ReportDetail);
            }
        }

        return arr;
    }

    private boolean saveReportDetailToDB() {
        return reportDetailControl.saveReportDetailToDB(reportDetailsDBList);
    }

    private boolean saveReportDetailToServer() {
        return reportDetailServerControl.saveReportDetailToServer(reportDetailsServerList);
    }

    private boolean updateReportDetailToServer() {
        return reportDetailServerControl.updateReportDetailToServer(reportDetailsUpdateServerList);
    }

    private boolean updateCategoryDetailToDb() {
        for (int i = 0; i < catDetailsUpdateDbList.size(); i++) {
            categoryDetailControl.updateCategory(catDetailsUpdateDbList.get(i));
        }

        return true;
    }

    private boolean updateReportDetailToDb() {
        for (int i = 0; i < reportDetailsUpdateDbList.size(); i++) {
            reportDetailControl.updateReport(reportDetailsUpdateDbList.get(i));
        }

        return true;
    }
}
