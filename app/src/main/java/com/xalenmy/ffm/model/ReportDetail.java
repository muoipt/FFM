package com.xalenmy.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class ReportDetail implements Serializable {
    private int reportId;
    private String reportDatetime;
    private int reportCatId;
    private int reportAmount;
    private String reportTitle;
    private String reportNote;
    private boolean isReportMark;
    private String reportUpdatedAt;
    private boolean isReportDeleted;

    public ReportDetail() {
    }

    public ReportDetail(int reportId, String reportDatetime, int reportCatId, int reportAmount, String reportTitle, String reportNote, boolean isReportMark, String reportUpdatedAt, boolean isReportDeleted) {
        this.reportId = reportId;
        this.reportDatetime = reportDatetime;
        this.reportCatId = reportCatId;
        this.reportAmount = reportAmount;
        this.reportTitle = reportTitle;
        this.reportNote = reportNote;
        this.isReportMark = isReportMark;
        this.reportUpdatedAt = reportUpdatedAt;
        this.isReportDeleted = isReportDeleted;
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

    public int getReportCatId() {
        return reportCatId;
    }

    public void setReportCatId(int reportCatId) {
        this.reportCatId = reportCatId;
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

    public boolean isReportMark() {
        return isReportMark;
    }

    public void setReportMark(boolean reportMark) {
        isReportMark = reportMark;
    }

    public String getReportUpdatedAt() {
        return reportUpdatedAt;
    }

    public void setReportUpdatedAt(String reportUpdatedAt) {
        this.reportUpdatedAt = reportUpdatedAt;
    }

    public boolean isReportDeleted() {
        return isReportDeleted;
    }

    public void setReportDeleted(boolean reportDeleted) {
        isReportDeleted = reportDeleted;
    }
}
