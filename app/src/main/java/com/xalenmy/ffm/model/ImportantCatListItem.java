package com.xalenmy.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 3/5/2018.
 */

public class ImportantCatListItem implements Serializable {

    private String catName;
    private int number;

    public ImportantCatListItem() {
    }

    public ImportantCatListItem(String catName) {
        this.catName = catName;
    }

    public ImportantCatListItem(String catName, int number) {
        this.catName = catName;
        this.number = number;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
