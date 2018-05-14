package com.xalenmy.ffm.eventmodel;

import com.xalenmy.ffm.model.CategoryDetail;

/**
 * Created by XalenMy on 3/29/2018.
 */

public class MainEventObject {
    private String msg;
    private CategoryDetail obj;

    public MainEventObject(String msg, CategoryDetail obj) {
        this.msg = msg;
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CategoryDetail getObj() {
        return obj;
    }

    public void setObj(CategoryDetail obj) {
        this.obj = obj;
    }
}
