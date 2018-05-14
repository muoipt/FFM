package com.xalenmy.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class MainSummaryReport implements Serializable {
    private int id;
    private String category;
    private int amount;
    private boolean status;

    public MainSummaryReport() {
    }

    public MainSummaryReport(String category, int amount, boolean status) {
        this.category = category;
        this.amount = amount;
        this.status = status;
    }

    public MainSummaryReport(int id, String category, int amount, boolean status) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
