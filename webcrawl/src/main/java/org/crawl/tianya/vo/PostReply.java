package org.crawl.tianya.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 帖子回复的实体类
 * Created by lenovo on 2014/11/13.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
@XmlType(name = "PostReply", propOrder = {"uid", "name", "message", "time"})
public class PostReply {
    private String uid;
    private String name;
    private String message;
    private String time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
