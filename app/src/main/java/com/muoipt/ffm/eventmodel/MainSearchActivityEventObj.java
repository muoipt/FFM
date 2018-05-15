package com.muoipt.ffm.eventmodel;

import com.muoipt.ffm.model.CategoryDetail;
import com.muoipt.ffm.model.MainSummaryReport;
import com.muoipt.ffm.model.ReportDetailDisplay;
import com.muoipt.ffm.model.UserDetail;

/**
 * Created by XalenMy on 3/30/2018.
 */

public class MainSearchActivityEventObj {
    private String msg;
    private UserDetail userDetail;
    private MainSummaryReport summaryReport;
    private ReportDetailDisplay reportDetailDisplay;
    private int position;

    public MainSearchActivityEventObj() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public MainSummaryReport getSummaryReport() {
        return summaryReport;
    }

    public void setSummaryReport(MainSummaryReport summaryReport) {
        this.summaryReport = summaryReport;
    }

    public ReportDetailDisplay getReportDetailDisplay() {
        return reportDetailDisplay;
    }

    public void setReportDetailDisplay(ReportDetailDisplay reportDetailDisplay) {
        this.reportDetailDisplay = reportDetailDisplay;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
