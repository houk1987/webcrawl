package org.crawl.tianya;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;
import org.crawl.tianya.vo.Post;
import org.crawl.tianya.vo.PostReply;
import org.protocl.httpclient.HttpConnectionManager;
import org.tools.FileUtils;
import org.tools.HtmlParser;
import org.tools.ParseUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2014/12/16.
 */
public class Tianya {
    private static final Logger logger = Logger.getLogger(Tianya.class);
    protected HttpConnectionManager httpConnectionManager;  //httpclient管理类
    protected String crawlUrl;  //抓取地址
    protected String nextItem; //下一页地址参数
    protected int nextCount;  //下一页总数
    protected Map<String, String> postHtmlList;  //抓取的页面集合(key 为地址，value 为content 内容)
    protected static Map<String, String> postUrlList;
    protected Thread getPostHtml;
    protected boolean isStart;
    protected GetPostHtmlThread getPostHtmlThread;
    public static int index;
    public static int index1;
    List<Thread> threads;
    protected Tianya(final HttpConnectionManager httpConnectionManager, String crawlUrl, String item) {
        this.httpConnectionManager = httpConnectionManager;
        this.crawlUrl = crawlUrl;
        this.nextItem = item;
        postHtmlList = new ConcurrentHashMap<String, String>();
        postUrlList = new ConcurrentHashMap<String, String>();
        getPostHtmlThread = new GetPostHtmlThread();
        getPostHtml = new Thread(getPostHtmlThread);
        threads = new LinkedList<Thread>();

    }

    /**
     * 抓取下一页html内容
     *
     * @param nextUrl 下一页的目标地址
     */
    protected void crawlNextHtml(String nextUrl) {
        final String html = httpConnectionManager.getHtml(nextUrl);
        crawlPost(html);
        if(!isStart){
            System.out.println("12321");
            getPostHtml.start();
            isStart = true;
        }else if(!getPostHtml.isAlive()){
            getPostHtml = new Thread(getPostHtmlThread);
            getPostHtml.start();
        }
        List<Element> getNextHref = ParseUtils.getAllHref(html, "a href=\"/list.jsp?item=" + this.nextItem + "&nextid=");
        if (getNextHref != null && getNextHref.size() > 0) {
            for (Element element : getNextHref) {
                nextCount++;
                logger.info("下一页" + "http://bbs.tianya.cn" + element.getAttributes().getValue("href") + "总数" + nextCount);

                crawlNextHtml("http://bbs.tianya.cn" + element.getAttributes().getValue("href"));
            }
        }

//        for (Thread thread : threads){
//            thread.start();
//        }

        while (true){
            if (index==0){
                return;
            }
        }
    }


    /**
     * 抓取html中的所有帖子
     * @param
     */
    private void crawlPost(final String html) {
        final  List<Element> hrefList = ParseUtils.getAllHref(html, "a href=\"/post-" + nextItem);
        Map<String,String> temp = new ConcurrentHashMap<String, String>();
        if (hrefList != null && hrefList.size() > 0) {
            for (Element element : hrefList) {
                Attributes childs = element.getAttributes();
                final String postUrl =  childs.getValue("href");
//                final String htmlContent = httpConnectionManager.getHtml("http://bbs.tianya.cn" +postUrl);
//                Source source = new Source(htmlContent);
//                Post post = new Post();
//                post = setPostContent(post, source);
//                post = setAuthor(post, source);
//                post = setPostReplyMessage(post, source);
//                String xxxx = post.getXmlContent();
                postUrlList.put(postUrl,postUrl);
            }
        }
    }

    protected  Post setAuthor(Post post, Source source) {
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

    private  Post setPostContent(Post post, Source source){
        Element messageElement = source.getFirstElement("div class=\"bbs-content clearfix\"");
        if (messageElement != null && messageElement.getContent() != null) {
            post.setMessage(HtmlParser.Html2Text(messageElement.getContent().toString()).trim());
        }
        return post;
    }

    private  Post setPostReplyMessage(Post post, Source source) {
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

    class GetPostHtmlThread implements Runnable{

        @Override
        public void run() {
            System.out.println("个数:"+postUrlList.size());
            if(postUrlList.size()>0&&postUrlList.size()%100000==0){
              //  new Thread(new SavePost()).start();
            }
        }
    }

    class SavePost implements Runnable{
        @Override
        public void run() {
            index++;
            Iterator iterator = postUrlList.keySet().iterator();
          while (iterator.hasNext()){
                String postUrl = String.valueOf(iterator.next());
                if(postHtmlList.containsKey(postUrl))continue;
                FileUtils.saveFile(postUrlList.get(postUrl), "D:/test1/" + postUrl + ".xml");
                postHtmlList.put(postUrl,postUrlList.get(postUrl));
                System.out.println("获取的个数"+postHtmlList.size());
            }
            index--;
        }
    }
}
