package com.xalenmy.ffm.control;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.ImportantCatListItem;
import com.xalenmy.ffm.model.MainSummaryReport;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class CategoryDetailControl {
    private DatabaseUtils databaseUtils;
    private Context mContext;

    public CategoryDetailControl(Context context) {
        mContext = context;
        databaseUtils = AppConfig.databaseUtils;
    }

    public ArrayList<CategoryDetail> getCategoryDetailFromDB() {
        databaseUtils.open();
        ArrayList<CategoryDetail> arrCategoryDetail = databaseUtils.getAllCategoryDetail();
        databaseUtils.close();
        return arrCategoryDetail;
    }

    public ArrayList<CategoryDetail> getCategoryDetailFromDBByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<CategoryDetail> arrCategoryDetail = databaseUtils.getAllCategoryDetailByUser(user);
        databaseUtils.close();
        return arrCategoryDetail;
    }

    public CategoryDetail getCategoryDetailFromDBByUserAndName(String cateName) {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<CategoryDetail> arrCategoryDetail = databaseUtils.getCategoryDetailByUserAndName(user, cateName);
        databaseUtils.close();

        if (arrCategoryDetail != null)
            return arrCategoryDetail.get(0);

        return null;
    }

    public ArrayList<Integer> getCategoryDetailIdServerDataByUser() {
        UserDetail user = AppConfig.getUserLogInInfor();

        int groupId = user.getUserGroupId();
        TypeDetailServerControl control = new TypeDetailServerControl(mContext);
        List<Integer> typeDetails = control.getLogInTypeIdsFromGroupId(groupId);

        final ArrayList<Integer> catDetailIdsServer = new ArrayList<Integer>();

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(DatabaseUtils.TABLE_CATEGORY_DETAIL);
        query2.whereContainedIn(DatabaseUtils.COLUMN_CATEGORY_TYPE_ID, typeDetails);

        try {
            List<ParseObject> objects = query2.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int catId = objects.get(i).getInt(DatabaseUtils.COLUMN_CATEGORY_ID);
                    catDetailIdsServer.add(catId);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return catDetailIdsServer;
    }


    public ArrayList<CategoryDetail> getAllCategoryDetailFromDBByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<CategoryDetail> arrCategoryDetail = databaseUtils.getAllTotalCategoryDetailByUser(user);
        databaseUtils.close();
        return arrCategoryDetail;
    }

    public ArrayList<ImportantCatListItem> getAllImportantCategoryFromDBByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        if (user.getUserStatus() != 1) {
            return new ArrayList<ImportantCatListItem>();
        }

        databaseUtils.open();
        ArrayList<ImportantCatListItem> arrCategoryDetail = databaseUtils.getAllMarkedCategoryByUser(user);

        //get number of report on each cate item
        for (int i = 0; i < arrCategoryDetail.size(); i++) {
            int numOfReport = databaseUtils.getNumOfReportInCategory(arrCategoryDetail.get(i).getCatName(), user);
        }

        databaseUtils.close();
        return arrCategoryDetail;
    }

    public ArrayList<String> getAllCategoryNameFromDBByUser() {

        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<String> arrCategoryDetail = databaseUtils.getAllTotalCategoryNameByUser(user);
        databaseUtils.close();
        return arrCategoryDetail;
    }

    public ArrayList<CategoryDetail> getCategoryDetailByType(String type) {
        databaseUtils.open();
        ArrayList<CategoryDetail> arrCategoryDetail = databaseUtils.getCategoryDetailByType(type);
        databaseUtils.close();
        return arrCategoryDetail;
    }

    public boolean addCategory(CategoryDetail categoryDetail) {
        boolean res = false;
        databaseUtils.open();
        if (databaseUtils.insertCategoryDetail(categoryDetail) != -1) {
            res = true;
        }
        databaseUtils.close();
        return res;
    }

    public void updateCategory(CategoryDetail categoryDetail) {
        databaseUtils.open();
        databaseUtils.updateCategoryDetail(categoryDetail);
        databaseUtils.close();
    }

    public void deleteCategoryTemporary(ArrayList<MainSummaryReport> mainSummaryReports) {
        databaseUtils.open();
        for (int i = 0; i < mainSummaryReports.size(); i++) {
            databaseUtils.deleteCategoryTemporaryById(mainSummaryReports.get(i).getId());
        }
        databaseUtils.close();
    }

    public void deleteCategoryTemporary(List<CategoryDetail> categoryDetails) {
        databaseUtils.open();
        for (int i = 0; i < categoryDetails.size(); i++) {
            databaseUtils.deleteCategoryTemporary(categoryDetails.get(i));
        }
        databaseUtils.close();
    }

    public void deleteCategory(CategoryDetail categoryDetail) {
        databaseUtils.open();
        databaseUtils.deleteCategoryDetail(categoryDetail);
        databaseUtils.close();
    }

    public void deleteCategoryTemporary(CategoryDetail categoryDetail) {
        databaseUtils.open();
        databaseUtils.deleteCategoryTemporary(categoryDetail);
        databaseUtils.close();
    }

    public int getCategoryIdFromName(String name) {
        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        int catId = databaseUtils.getCategoryIdFromName(name, user);
        databaseUtils.close();

        return catId;
    }


    public void deleteAllCategories() {
        databaseUtils.open();
        databaseUtils.deleteAllCategories();
        databaseUtils.close();
    }

    public boolean checkTableCategoryExist() {
        databaseUtils.open();
        boolean res = databaseUtils.isExistTableCategoryDetail();
        databaseUtils.close();

        return res;
    }

    public boolean createTableCategory() {
        databaseUtils.open();
        boolean res = databaseUtils.createTableCategoryDetail();
        databaseUtils.close();

        return res;
    }

    public List<CategoryDetail> getDeletedCategoriesOnDb() {
        databaseUtils.open();
        List<CategoryDetail> res = databaseUtils.getDeletedCategories();
        databaseUtils.close();

        return res;
    }

    public boolean checkCatNameExistInDb(String catName) {
        UserDetail user = AppConfig.getUserLogInInfor();
        databaseUtils.open();
        boolean res = databaseUtils.checkCatNameExistInDb(catName, user);
        databaseUtils.close();

        return res;
    }

    public ArrayList<CategoryDetail> getCateDetailDBdata() {
        ArrayList<CategoryDetail> catDetailsDB = new ArrayList<CategoryDetail>();
        catDetailsDB = getCategoryDetailFromDBByUser();

        return catDetailsDB;
    }

    public ArrayList<CategoryDetail> getAllCateDetailDBdata() {
        ArrayList<CategoryDetail> catDetailsDB = new ArrayList<CategoryDetail>();
        catDetailsDB = getAllCategoryDetailFromDBByUser();

        return catDetailsDB;
    }

    public boolean saveCatDetailToDB(ArrayList<CategoryDetail> arrCats) {
        for (int i = 0; i < arrCats.size(); i++) {
            addCategory(arrCats.get(i));
        }

        return true;

    }

    public CategoryDetail getCategoryFromId(int id) {
        ArrayList<CategoryDetail> catDetailsDB = new ArrayList<CategoryDetail>();
        catDetailsDB = getAllCategoryDetailFromDBByUser();

        for (CategoryDetail c : catDetailsDB) {
            if (c.getCatId() == id) {
                return c;
            }
        }

        return null;
    }

    public int getCountReportEachCat(int catId) {
        databaseUtils.open();
        int res = databaseUtils.getCountReportEachCat(catId);
        databaseUtils.close();

        return res;
    }
}
