package org.crawl.tianya.vo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * 帖子的实体类
 * Created by lenovo on 2014/11/13.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
@XmlType(name = "post", propOrder = {"title", "author","name", "message", "time","postReplyList"})
public class Post {

    //标题
    @XmlElement
    private String title;

    //作者
    @XmlElement(required = true)
    private String author;

    //用户名
    @XmlElement(required = true)
    private String name;

    //帖子内容
    @XmlElement(required = true)
    private String message;

    //发帖时间
    @XmlElement(required = true)
    private String time;

    //回复当前帖子回复集合
    @XmlElementWrapper(name = "postReplys")
    @XmlElement(name = "postReply")
    private LinkedList<PostReply> postReplyList = new LinkedList<PostReply>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PostReply> getPostReplyList() {
        return postReplyList;
    }

    public void setPostReplyList(LinkedList<PostReply> postReplyList) {
        this.postReplyList = postReplyList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlContent() {
        JAXBContext jAXBContext = null;
        try {
            StringWriter sw = new StringWriter();
            jAXBContext = JAXBContext.newInstance(getClass());
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "gb2312");
            marshaller.marshal(this, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
