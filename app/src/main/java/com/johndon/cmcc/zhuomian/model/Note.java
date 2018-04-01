package com.johndon.cmcc.zhuomian.model;

import java.io.Serializable;

/**
 * Created by johndon on 2/25/17.
 */

public class Note implements Serializable{
    private static final long serialVersionUID = -4162212851640420632L;
    private int id;
   // private int _id;
    private String content;
    private String  updateTime;
    private String type;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this. typeId = typeId;
    }

    private String typeId;// 0 meeting,1 memory day,2,remind,3 schedule


    public int getId() {
        return id;
    }

   /* public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }*/

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
