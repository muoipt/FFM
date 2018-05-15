package com.muoipt.ffm.control;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.muoipt.ffm.database.DatabaseUtils;
import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.ReportDetail;
import com.muoipt.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailServerControl {

    private Context mContext;

    public ReportDetailServerControl(Context context){
        mContext = context;
    }

    public static ArrayList<ReportDetail> getReportDetailServerData() {
        final ArrayList<ReportDetail> reportDetailsServer = new ArrayList<ReportDetail>();

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int reportId = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_ID);
                    String reportDateTime = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_DATETIME);
                    int reportCatId = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_CAT_ID);
                    int reportAmount = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_AMOUNT);
                    String reportTitle = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_TITLE);
                    String reportNote = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_NOTE);
                    String reportUpdateAt = objects.get(i).getUpdatedAt().toString();
                    boolean reportDeleted = objects.get(i).getBoolean(DatabaseUtils.COLUMN_REPORT_DELETED);
                    boolean reportMarkImportant = objects.get(i).getBoolean(DatabaseUtils.COLUMN_REPORT_MARK_IMPORTANT);

                    ReportDetail reportDetail = new ReportDetail(reportId, reportDateTime, reportCatId, reportAmount, reportTitle, reportNote, reportMarkImportant, reportUpdateAt, reportDeleted);
                    reportDetailsServer.add(reportDetail);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reportDetailsServer;
    }

    public ArrayList<ReportDetail> getReportDetailServerDataByUser() {

        CategoryDetailControl control = new CategoryDetailControl(mContext);
        ArrayList<Integer> categoryDetailIds = control.getCategoryDetailIdServerDataByUser();

        final ArrayList<ReportDetail> reportDetailsServer = new ArrayList<ReportDetail>();

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
        query3.whereContainedIn(DatabaseUtils.COLUMN_REPORT_CAT_ID, categoryDetailIds);
        query3.whereEqualTo(DatabaseUtils.COLUMN_REPORT_DELETED, false);

        try {
            List<ParseObject> objects = query3.find();
            if (objects != null) {
                for (int i = 0; i < objects.size(); i++) {
                    int reportId = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_ID);
                    String reportDateTime = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_DATETIME);
                    int reportCatId = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_CAT_ID);
                    int reportAmount = objects.get(i).getInt(DatabaseUtils.COLUMN_REPORT_AMOUNT);
                    String reportTitle = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_TITLE);
                    String reportNote = objects.get(i).getString(DatabaseUtils.COLUMN_REPORT_NOTE);
                    String reportUpdatedAt = objects.get(i).getUpdatedAt().toString();
                    boolean reportDeleted = objects.get(i).getBoolean(DatabaseUtils.COLUMN_REPORT_DELETED);
                    boolean reportMarkImportant = objects.get(i).getBoolean(DatabaseUtils.COLUMN_REPORT_MARK_IMPORTANT);

                    ReportDetail reportDetail = new ReportDetail(reportId, reportDateTime, reportCatId, reportAmount, reportTitle, reportNote, reportMarkImportant, reportUpdatedAt, reportDeleted);
                    reportDetailsServer.add(reportDetail);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reportDetailsServer;
    }

    public boolean saveReportDetailToServer(ArrayList<ReportDetail> arrReports) {
        final boolean[] res = {true};

        for (int i = 0; i < arrReports.size(); i++) {
            ParseObject reportDetail = new ParseObject(DatabaseUtils.TABLE_REPORT_DETAIL);
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_ID, arrReports.get(i).getReportId());
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_DATETIME, arrReports.get(i).getReportDatetime());
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_CAT_ID, arrReports.get(i).getReportCatId());
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_AMOUNT, arrReports.get(i).getReportAmount());
            if (arrReports.get(i).getReportTitle() != null) {
                reportDetail.put(DatabaseUtils.COLUMN_REPORT_TITLE, arrReports.get(i).getReportTitle());
            }
            if (arrReports.get(i).getReportNote() != null) {
                reportDetail.put(DatabaseUtils.COLUMN_REPORT_NOTE, arrReports.get(i).getReportNote());
            }
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_DELETED, arrReports.get(i).isReportDeleted());
            reportDetail.put(DatabaseUtils.COLUMN_REPORT_MARK_IMPORTANT, arrReports.get(i).isReportMark());

            reportDetail.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Save in background", "sucessful");
                    } else {
                        Log.i("Save in background", "failed error : " + e.toString());
                        res[0] = false;
                    }
                }
            });

        }

        return res[0];
    }

    public static boolean updateReportDetailToServer(ArrayList<ReportDetail> arrReports) {
        final boolean[] res = {true};

        for (int i = 0; i < arrReports.size(); i++) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
            query.whereEqualTo(DatabaseUtils.COLUMN_REPORT_ID, arrReports.get(i).getReportId());

            try {
                List<ParseObject> reportDetail = query.find();
                for (int j = 0; j < reportDetail.size(); j++) {
                    if (reportDetail != null) {
                        reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_DATETIME, arrReports.get(i).getReportDatetime());
                        reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_AMOUNT, arrReports.get(i).getReportAmount());
                        if (arrReports.get(i).getReportTitle() != null)
                            reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_TITLE, arrReports.get(i).getReportTitle());
                        if (arrReports.get(i).getReportNote() != null)
                            reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_NOTE, arrReports.get(i).getReportNote());
                        reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_DELETED, arrReports.get(i).isReportDeleted());
                        reportDetail.get(j).put(DatabaseUtils.COLUMN_REPORT_MARK_IMPORTANT, arrReports.get(i).isReportMark());

                        reportDetail.get(j).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("updateReportToServer", "sucessful");
                                } else {
                                    Log.i("updateReportToServer", "failed error : " + e.toString());
                                    res[0] = false;
                                }
                            }
                        });
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return res[0];
    }

    public int getNextReportDetailTblId() {
        return SyncUtils.getIdInServer(DatabaseUtils.COLUMN_REPORT_ID_DATA);
    }

    public boolean deleteAllReportData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(DatabaseUtils.TABLE_REPORT_DETAIL);
        try {
            List<ParseObject> objs = query.find();
            if (objs != null) {
                for (int i = 0; i < objs.size(); i++) {
                    objs.get(i).delete();
                    objs.get(i).save();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
