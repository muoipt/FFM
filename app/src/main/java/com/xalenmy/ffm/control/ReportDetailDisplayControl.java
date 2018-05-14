package com.xalenmy.ffm.control;

import android.content.Context;

import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.ReportDetailDisplay;
import com.xalenmy.ffm.model.TypeDetail;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailDisplayControl {
    public static DatabaseUtils databaseUtils;

    public ReportDetailDisplayControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
    }

    //detail report la bao gom ca incomeDetail va outcomeDetail
    public ArrayList<ReportDetailDisplay> getDetailReportFromDB() {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        databaseUtils.open();
        arrReportDetail = databaseUtils.getAllReportDetail();
        databaseUtils.close();

        return arrReportDetail;
    }

    public ArrayList<ReportDetailDisplay> getReportDetailDisplayFromDBByUser(UserDetail user) {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        databaseUtils.open();
        arrReportDetail = databaseUtils.getAllReportDetaiDisplaylByUser(user);
        databaseUtils.close();

        return arrReportDetail;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByType(String selected) {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        databaseUtils.open();
        arrReportDetail = databaseUtils.getDetailReportFromDBByType(selected);
        databaseUtils.close();

        return arrReportDetail;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByCategory(String nameSelected) {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        UserDetail currentUser = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        arrReportDetail = databaseUtils.getDetailReportFromDBByCategoryName(nameSelected, currentUser.getUserGroupId());
        databaseUtils.close();

        return arrReportDetail;
    }

    public ArrayList<ReportDetailDisplay> getDetailReportFromDBByCategory(CategoryDetail categoryDetailSelected) {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        databaseUtils.open();
        arrReportDetail = databaseUtils.getDetailReportFromDBByCategoryId(categoryDetailSelected.getCatId());
        databaseUtils.close();

        return arrReportDetail;
    }

}
