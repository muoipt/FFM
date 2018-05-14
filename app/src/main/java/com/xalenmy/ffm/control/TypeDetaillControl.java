package com.xalenmy.ffm.control;

import android.content.Context;

import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.TypeDetail;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;

import java.util.ArrayList;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class TypeDetaillControl {
    public static DatabaseUtils databaseUtils;

    public TypeDetaillControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
    }

    public ArrayList<TypeDetail> getDetailTypeFromDB() {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<TypeDetail> arrTypeDetail = new ArrayList<TypeDetail>();
        arrTypeDetail = databaseUtils.getAllTypeDetailByUser(user);
        databaseUtils.close();

        return arrTypeDetail;
    }

    public ArrayList<TypeDetail> getDetailTypeFromDBByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<TypeDetail> arrTypeDetail = new ArrayList<TypeDetail>();
        arrTypeDetail = databaseUtils.getAllTypeDetailByUser(user);
        databaseUtils.close();

        return arrTypeDetail;
    }

    public void addTypeDetail(TypeDetail typeDetail) {
        databaseUtils.open();
        databaseUtils.insertTypeDetail(typeDetail);
        databaseUtils.close();
    }

    public void updateTypeDetail(TypeDetail typeDetail) {
        databaseUtils.open();
        databaseUtils.updateTypeDetail(typeDetail);
        databaseUtils.close();
    }

    public int getGroupId() {
        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<TypeDetail> arrTypeDetail = new ArrayList<TypeDetail>();
        arrTypeDetail = databaseUtils.getAllTypeDetailByUser(user);
        databaseUtils.close();

        return arrTypeDetail.get(0).getTypeGroupId();
    }


    public boolean checkOutcomeType(int typeId) {
        databaseUtils.open();
        ArrayList<TypeDetail> arrTypeDetail = new ArrayList<TypeDetail>();
        arrTypeDetail = databaseUtils.getAllTypeDetail();
        databaseUtils.close();

        String typeName = null;
        for (int i = 0; i < arrTypeDetail.size(); i++) {
            if (arrTypeDetail.get(i).getTypeId() == typeId) {
                typeName = arrTypeDetail.get(i).getTypeName();
            }
        }

        if (typeName.equals("income")) {
            return false;
        }

        return true;
    }

    //get type id from name and group id from DB
    public int getDetailTypeIdFromDB(TypeDetail t) {
        String typeName = t.getTypeName();
        int groupId = t.getTypeGroupId();

        databaseUtils.open();
        ArrayList<TypeDetail> arrTypeDetail = new ArrayList<TypeDetail>();
        arrTypeDetail = databaseUtils.getAllTypeDetail();
        databaseUtils.close();

        for (int i = 0; i < arrTypeDetail.size(); i++) {
            if (arrTypeDetail.get(i).getTypeName().equals(typeName) && arrTypeDetail.get(i).getTypeGroupId() == groupId) {
                return arrTypeDetail.get(i).getTypeId();
            }
        }

        return -1;
    }

    public int getMaxTypeDetailId() {
        databaseUtils.open();
        int maxId = databaseUtils.getMaxTypeDetailId();
        databaseUtils.close();

        return maxId;
    }

    public void deleteAllTypes() {
        databaseUtils.open();
        databaseUtils.deleteAllTypes();
        databaseUtils.close();
    }

    public boolean checkTableTypeExistInDB() {
        boolean isExist = false;

        databaseUtils.open();
        isExist = databaseUtils.isExistTableTypeDetail();
        databaseUtils.close();

        return isExist;
    }

    public boolean checkDataTypeExistInDb(TypeDetail t) {
        databaseUtils.open();

        ArrayList<TypeDetail> types = getDetailTypeFromDB();
        if (types == null)
            return false;

        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getTypeName().equals(t.getTypeName()) && types.get(i).getTypeGroupId() == t.getTypeGroupId()) {
                return true;
            }
        }

        databaseUtils.close();

        return false;
    }

    public boolean createTableType() {
        boolean res = false;

        databaseUtils.open();
        res = databaseUtils.createTableTypeDetail();
        databaseUtils.close();

        return res;
    }

    private boolean checkTypeDetailDbExist(TypeDetail typeDetail) {
        if (getDetailTypeIdFromDB(typeDetail) != -1) {
            return true;
        }

        return false;
    }

    public boolean saveTypeDetailToDB(ArrayList<TypeDetail> arrTypes) {
        for (int i = 0; i < arrTypes.size(); i++) {
            if (!checkTypeDetailDbExist(arrTypes.get(i))) {
                addTypeDetail(arrTypes.get(i));
            }
        }

        return true;
    }

    public ArrayList<TypeDetail> getTypeDetailDBdata() {
        ArrayList<TypeDetail> typeDetailsDB = new ArrayList<TypeDetail>();
        typeDetailsDB = getDetailTypeFromDBByUser();

        return typeDetailsDB;

    }

}
