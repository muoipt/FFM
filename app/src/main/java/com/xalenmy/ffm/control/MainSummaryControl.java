package com.xalenmy.ffm.control;

import android.content.Context;

import com.xalenmy.ffm.R;
import com.xalenmy.ffm.database.DatabaseUtils;
import com.xalenmy.ffm.model.CategoryDetail;
import com.xalenmy.ffm.model.MainSummaryReport;
import com.xalenmy.ffm.model.ReportDetail;
import com.xalenmy.ffm.model.UserDetail;
import com.xalenmy.ffm.utils.AppConfig;
import com.xalenmy.ffm.utils.ComonUtils;
import com.xalenmy.ffm.utils.SyncUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class MainSummaryControl {
    private DatabaseUtils databaseUtils;
    private Context mContext;

    public MainSummaryControl(Context context) {
        databaseUtils = AppConfig.databaseUtils;
        mContext = context;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportFromDB() {
        databaseUtils.open();
        ArrayList<MainSummaryReport> arrSummaryReport = databaseUtils.getAllMainSummaryReport();
        databaseUtils.close();
        return arrSummaryReport;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportByUserFromDB(UserDetail user) {

        ArrayList<MainSummaryReport> arrSummaryReport = new ArrayList<MainSummaryReport>();

        if (user.getUserStatus() == 1) {
            databaseUtils.open();
            arrSummaryReport = databaseUtils.getAllMainSummaryReportByUser(user);
            databaseUtils.close();
        }

        return arrSummaryReport;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportByTypeByUserFromDB(String type, UserDetail user) {

        ArrayList<MainSummaryReport> arrSummaryReport = new ArrayList<MainSummaryReport>();

        if (user.getUserStatus() == 1) {
            databaseUtils.open();
            arrSummaryReport = databaseUtils.getAllMainSummaryReportByTypeByUser(type, user);
            databaseUtils.close();
        }

        return arrSummaryReport;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportByTypeByUserByTodayFromDB(String type, UserDetail user) {

        String today = ComonUtils.getCurrentStrDatetime();

        ArrayList<MainSummaryReport> arrSummaryReport = new ArrayList<MainSummaryReport>();
        ArrayList<MainSummaryReport> results = new ArrayList<MainSummaryReport>();

        if (user.getUserStatus() == 1) {
            databaseUtils.open();
            arrSummaryReport = databaseUtils.getAllMainSummaryReportByTypeByUserByToday(type, user, today);
            databaseUtils.close();
        }

        for (int i = 0; i < arrSummaryReport.size(); i++) {
            MainSummaryReport m = arrSummaryReport.get(i);
            if (m.getAmount() != 0) {
                results.add(m);
            }
        }

        return results;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportByTypeByUserByWeekFromDB(String type, UserDetail user) {

        Date dateFrom = firstDayOfWeek(new Date());
        String strdateFrom = ComonUtils.getFormatDatetime(dateFrom);

        String strdateTo = ComonUtils.getCurrentStrDatetime();


        ArrayList<MainSummaryReport> arrSummaryReport = new ArrayList<MainSummaryReport>();
        ArrayList<MainSummaryReport> results = new ArrayList<MainSummaryReport>();

        if (user.getUserStatus() == 1) {
            databaseUtils.open();
            arrSummaryReport = databaseUtils.getAllMainSummaryReportByTypeByUserByTime(type, user, strdateFrom, strdateTo);
            databaseUtils.close();
        }

        for (int i = 0; i < arrSummaryReport.size(); i++) {
            MainSummaryReport m = arrSummaryReport.get(i);
            if (m.getAmount() != 0) {
                results.add(m);
            }
        }

        return results;
    }

    public ArrayList<MainSummaryReport> getMainSummaryReportByTypeByUserByMonthFromDB(String type, UserDetail user) {

        Date dateFrom = firstDayOfMonth(new Date());
        String strdateFrom = ComonUtils.getFormatDatetime(dateFrom);

        String strdateTo = ComonUtils.getCurrentStrDatetime();


        ArrayList<MainSummaryReport> arrSummaryReport = new ArrayList<MainSummaryReport>();
        ArrayList<MainSummaryReport> results = new ArrayList<MainSummaryReport>();

        if (user.getUserStatus() == 1) {
            databaseUtils.open();
            arrSummaryReport = databaseUtils.getAllMainSummaryReportByTypeByUserByTime(type, user, strdateFrom, strdateTo);
            databaseUtils.close();
        }

        for (int i = 0; i < arrSummaryReport.size(); i++) {
            MainSummaryReport m = arrSummaryReport.get(i);
            if (m.getAmount() != 0) {
                results.add(m);
            }
        }

        return results;
    }


    public boolean isIncomeReport(String categoryName) {
        databaseUtils.open();
        boolean b = databaseUtils.isIncomeReport(categoryName);
        databaseUtils.close();

        return b;
    }

    private Date firstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    private Date firstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();       // this returns java.util.Date
    }


    public ArrayList<Integer> getIncomesByMonths(int year) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        String typeName = mContext.getString(R.string.data_type_income);

        ArrayList<String> months = ComonUtils.getListMonthString();

        int count = ComonUtils.getCurrentMonthInteger();

        for(int i=0; i<count; i++) {

            String month = months.get(i);

            int res = databaseUtils.getAllAmountByMonths(typeName, year, month);

            if(res != -1){
                arr.add(res);
            }
        }

        return arr;
    }

    public ArrayList<Integer> getOutcomesByMonths(int year) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        String typeName = mContext.getString(R.string.data_type_outcome);

        ArrayList<String> months = ComonUtils.getListMonthString();

        int count = ComonUtils.getCurrentMonthInteger();

        for(int i=0; i<count; i++) {

            String month = months.get(i);

            int res = databaseUtils.getAllAmountByMonths(typeName, year, month);

            if(res != -1){
                arr.add(res);
            }
        }

        return arr;
    }

    public ArrayList<String> getYearList() {
        ArrayList<String> years = new ArrayList<String>();

        UserDetail userDetail = AppConfig.getUserLogInInfor();

        String max_date = databaseUtils.getMaxDateByUser(userDetail);
        String min_date = databaseUtils.getMinDateByUser(userDetail);

        int max_year = Integer.parseInt(max_date.substring(0,4));
        int min_year = Integer.parseInt(min_date.substring(0,4));

        for(int i=min_year; i<=max_year; i++){
            years.add(String.valueOf(i));
        }

        return years;
    }
}
