package com.app.abcdadmin.models;


import static com.app.abcdadmin.constants.IConstants.FALSE;
import static com.app.abcdadmin.constants.IConstants.GEN_UNSPECIFIED;
import static com.app.abcdadmin.constants.IConstants.IMG_PREVIEW;
import static com.app.abcdadmin.constants.IConstants.STATUS_OFFLINE;
import static com.app.abcdadmin.constants.IConstants.TYPE_EMAIL;

import com.app.abcdadmin.managers.Utils;

import java.io.Serializable;
import java.net.URLDecoder;

public class Ticket implements Serializable {
    private String id;
    private String user_id;
    private String name;
    private String category;
    private String description;
    private String mobile;
    private String support;
    private String type;
    private String reply;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
