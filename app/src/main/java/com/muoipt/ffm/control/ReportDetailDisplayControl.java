package com.muoipt.ffm.control;

import android.content.Context;

import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.ReportDetailDisplay;
import com.muoipt.ffm.model.TypeDetail;
import com.muoipt.ffm.model.UserDetail;
import com.muoipt.ffm.utils.AppConfig;
import com.muoipt.ffm.utils.ComonUtils;
import com.muoipt.ffm.utils.SyncUtils;

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

    public ArrayList<ReportDetailDisplay> getReportDetailDisplayFromDBByUser(UserDetail currentUser) {
        ArrayList<ReportDetailDisplay> arrReportDetail = new ArrayList<ReportDetailDisplay>();

        if (currentUser.getUserEmail() != null && currentUser.getUserStatus() == ComonUtils.USER_STATUS_NORMAL) {
            databaseUtils.open();
            arrReportDetail = databaseUtils.getAllReportDetaiDisplaylByUser(currentUser);
            databaseUtils.close();
        }

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
