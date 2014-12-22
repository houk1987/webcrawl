package org.crawl.tianya;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.crawl.tianya.vo.Post;
import org.crawl.tianya.vo.PostReply;
import org.protocl.httpclient.HttpConnectionManager;
import org.tools.FileUtils;
import org.tools.HtmlParser;
import org.tools.ParseUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
public class ParserAndSavePost implements Runnable {
    protected HttpConnectionManager httpConnectionManager;  //httpclient管理类
    protected String nextItem; //下一页地址参数
    private List<String> htmlList;
    public static int index;

    public ParserAndSavePost(HttpConnectionManager httpConnectionManager, String nextItem, List<String> htmlList) {
        this.httpConnectionManager = httpConnectionManager;
        this.nextItem = nextItem;
        this.htmlList = htmlList;
    }

    @Override
    public void run() {
        index++;
        for(String html : htmlList){
            crawlPost(html,nextItem,httpConnectionManager);
        }
    }

    /**
     * 抓取html中的所有帖子
     * @param
     */
    private void crawlPost(String html,String nextItem,HttpConnectionManager httpConnectionManager) {
            List<Element> hrefList = ParseUtils.getAllHref(html, "a href=\"/post-" + nextItem);
            if (hrefList != null && hrefList.size() > 0) {
                for (Element element : hrefList) {
                    net.htmlparser.jericho.Attributes childs = element.getAttributes();
                    final String postUrl = "http://bbs.tianya.cn" + childs.getValue("href");
                    //logger.info("当前抓取帖子的页面"+postUrl);
                    final String htmlContent = httpConnectionManager.getHtml(postUrl);
                    //logger.info("内容"+htmlContent);
                    Source source = new Source(htmlContent);
                    Post post = new Post();
                    post = setPostContent(post, source);
                    post = setAuthor(post, source);
                    post = setPostReplyMessage(post, source);
                    FileUtils.saveFile(post.getXmlContent(), "D:/test1/" + childs.getValue("href") + ".xml");
                    //postList.add(post);
                   // System.out.println(nextHtmlList.size());
                }
            }
        index--;
    }

    protected static Post setAuthor(Post post, Source source) {
        List<Element> author = source.getAllElements("div class=\"atl-info\"");
        int i = 0;
        for (Element element : author) {
            if (element != null) {
                List<Element> elements = element.getChildElements();
                PostReply postReply = new PostReply();
                for (Element element1 : elements) {
                    String content = element1.getContent().toString();
                    //logger.info("账号内容"+content);
                    if (content.contains("uid=\"") && content.contains("uname=\"")) {
                        int start = content.indexOf("uid=\"");
                        int end = content.indexOf("uname=\"");
                        String uid = content.substring(start + 5, end - 2);
                        String userName = content.substring(end + 7, content.length()).split("\">")[0];
                        //logger.info("账号ID:"+uid);
                        // logger.info("账号名称:"+userName);
                        postReply.setUid(uid);
                        postReply.setName(userName);
                        if (i == 0) {
                            post.setAuthor(uid);
                            post.setName(userName);
                        }
                    } else if (content.contains("时间")) {
                        postReply.setTime(content.split("时间：")[1]);
                        if (i == 0) {
                            post.setTime(postReply.getTime());
                        }
                    }
                }
                if (i != 0) {
                    post.getPostReplyList().add(postReply);
                } else {
                    i++;
                }
            }
        }
        return post;
    }

    private static Post setPostContent(Post post, Source source){
        Element messageElement = source.getFirstElement("div class=\"bbs-content clearfix\"");
        if (messageElement != null && messageElement.getContent() != null) {
            post.setMessage(HtmlParser.Html2Text(messageElement.getContent().toString()).trim());
        }
        return post;
    }

    private static Post setPostReplyMessage(Post post, Source source) {
        List<Element> elementList = source.getAllElements("div class=\"bbs-content\"");
        for (int i = 0; i < elementList.size(); i++) {
            Element element1 = elementList.get(i);
            String content = element1.getContent().toString();
            if (i > post.getPostReplyList().size() - 1) continue;
            PostReply postReply = post.getPostReplyList().get(i);
            if (postReply != null) {
                /**
                 * 去掉图片
                 * 将图片的html 代码替换成空
                 */
                try {
                    if(content.contains("img")){
                        int index = content.indexOf("<img");
                        int index2 = content.lastIndexOf("/>");
                        if(index==-1){
                            content="";
                        }else{
                            String rex = content.substring(index,index2+2);
                            content = content.replaceAll(rex,"");
                        }
                    }
                    postReply.setMessage(content.replaceAll("<br>","").trim());
                }catch (Exception e){
                    continue;
                }

                //logger.info("回复内容"+postReply.getMessage());
            }
        }
        return post;
    }
}
