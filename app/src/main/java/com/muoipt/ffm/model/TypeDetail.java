package com.muoipt.ffm.model;

import java.io.Serializable;

/**
 * Created by XalenMy on 2/8/2018.
 */

public class TypeDetail implements Serializable {
    private int typeId;
    private String typeName;
    private int typeGroupId;

    public TypeDetail() {
    }

    public TypeDetail(int typeId, String typeName, int typeGroupId) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeGroupId = typeGroupId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeGroupId() {
        return typeGroupId;
    }

    public void setTypeGroupId(int typeGroupId) {
        this.typeGroupId = typeGroupId;
    }
}
