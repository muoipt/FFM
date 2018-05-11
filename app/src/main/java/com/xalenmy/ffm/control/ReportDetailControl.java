package com.xalenmy.ffm.control;

import android.content.Context;

import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.ReportDetail;
import com.xalenmy.ffm.model.ReportDetailDisplay;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailControl {
    public static DatabaseUtils databaseUtils;

    public ReportDetailControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
    }

    public boolean addReport(ReportDetail reportDetail) {
        databaseUtils.open();

        long res = databaseUtils.insertReportDetail(reportDetail);

        databaseUtils.close();

        return res == -1 ? false : true;
    }

    public int getTotalOutcomeAmount() {
        int sum = 0;
        databaseUtils.open();

        sum = databaseUtils.getTotalOutcomeAmount();

        databaseUtils.close();
        return sum;
    }

    public int getTotalIncomeAmount() {
        int sum = 0;
        databaseUtils.open();

        sum = databaseUtils.getTotalIncomeAmount();

        databaseUtils.close();
        return sum;
    }

    public ArrayList<ReportDetail> getTotalReportDetail() {
        ArrayList<ReportDetail> list = new ArrayList<>();

        databaseUtils.open();
        list = databaseUtils.getTotalReportDetail();
        databaseUtils.close();

        return list;
    }

    public ArrayList<ReportDetail> getTotalReportDetailByUser() {
        UserDetail user = AppConfig.getUserLogInInfor();

        databaseUtils.open();
        ArrayList<ReportDetail> reportDetails = databaseUtils.getAllReportDetailByUser(user);
        databaseUtils.close();
        return reportDetails;
    }

    public int getMaxReportDetailId() {
        databaseUtils.open();
        int maxId = databaseUtils.getMaxReportDetailId();
        databaseUtils.close();

        return maxId;
    }

    public void deleteAllReports() {
        databaseUtils.open();
        databaseUtils.deleteAllReports();
        databaseUtils.close();
    }

    public void deleteReports(List<ReportDetailDisplay> list) {
        databaseUtils.open();
        for (ReportDetailDisplay e : list) {
            databaseUtils.deleteReportTemporaryById(e);
        }
        databaseUtils.close();
    }

    public boolean updateReport(ReportDetail reportDetail) {
        databaseUtils.open();
        boolean res = databaseUtils.updateReportDetail(reportDetail);
        databaseUtils.close();
        return res;
    }

    public ArrayList<ReportDetail> getReportDetailDBdata() {
        ArrayList<ReportDetail> reportDetailsDB = new ArrayList<ReportDetail>();
        reportDetailsDB = getTotalReportDetail();

        return reportDetailsDB;
    }

    public ArrayList<ReportDetail> getReportDetailDBdataByUser() {
        ArrayList<ReportDetail> reportDetailsDB = new ArrayList<ReportDetail>();
        reportDetailsDB = getTotalReportDetailByUser();

        return reportDetailsDB;
    }

    public boolean saveReportDetailToDB(ArrayList<ReportDetail> arrReports) {
        for (int i = 0; i < arrReports.size(); i++) {
            addReport(arrReports.get(i));
        }

        return true;
    }
}
