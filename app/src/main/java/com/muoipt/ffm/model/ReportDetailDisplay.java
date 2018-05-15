package com.muoipt.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetailDisplay implements Serializable {
    private int reportId;
    private String reportDatetime;
    private String categoryName;
    private int reportAmount;
    private String reportTitle;
    private String reportNote;

    public ReportDetailDisplay() {
    }

    public ReportDetailDisplay(String reportDatetime, String categoryName, int reportAmount, String reportTitle, String reportNote) {
        this.reportDatetime = reportDatetime;
        this.categoryName = categoryName;
        this.reportAmount = reportAmount;
        this.reportTitle = reportTitle;
        this.reportNote = reportNote;
    }

    public ReportDetailDisplay(int reportId, String reportDatetime, String categoryName, int reportAmount, String reportTitle, String reportNote) {
        this.reportId = reportId;
        this.reportDatetime = reportDatetime;
        this.categoryName = categoryName;
        this.reportAmount = reportAmount;
        this.reportTitle = reportTitle;
        this.reportNote = reportNote;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getReportDatetime() {
        return reportDatetime;
    }

    public void setReportDatetime(String reportDatetime) {
        this.reportDatetime = reportDatetime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getReportAmount() {
        return reportAmount;
    }

    public void setReportAmount(int reportAmount) {
        this.reportAmount = reportAmount;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportNote() {
        return reportNote;
    }

    public void setReportNote(String reportNote) {
        this.reportNote = reportNote;
    }
}
