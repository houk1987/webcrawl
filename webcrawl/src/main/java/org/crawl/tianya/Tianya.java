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
import org.ui.ObserveCenter;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by lenovo on 2014/12/16.
 */
public class Tianya {
    private static final Logger logger = Logger.getLogger(Tianya.class);
    protected HttpConnectionManager httpConnectionManager;//httpclient������
    protected String crawlUrl;  //ץȡ��ַ
    protected String nextUrlItem; //��һҳ��ַ����
    public static int count; //ץȡ������
    private String crawlDataFilePath; //ץȡ�����ļ������ַ(�ļ���)
    private ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * ץȡ������̳
     * @param httpConnectionManager
     * @param crawlUrl
     * @param item
     */
    protected Tianya(final HttpConnectionManager httpConnectionManager, String crawlUrl, String item) {
        this.httpConnectionManager = httpConnectionManager;
        this.crawlUrl = crawlUrl;
        this.nextUrlItem = item;
    }

    public void setCrawlDataFilePath(String crawlDataFilePath) {
        this.crawlDataFilePath = crawlDataFilePath;
    }

    /**
     * ץȡ��һҳhtml����
     *
     * @param nextUrl ��һҳ��Ŀ���ַ
     */
    protected void crawlHtml(String nextUrl) {
        final String html = httpConnectionManager.getHtml(nextUrl); //��ȡ��ǰҳ��html
        crawlPost(html);  //ץȡ��ǰҳ�����е�����
        List<Element> getNextHref = ParseUtils.getAllHref(html, "a href=\"/list.jsp?item=" + this.nextUrlItem + "&nextid=");
        if (getNextHref != null && getNextHref.size() > 0) {
            for (Element element : getNextHref) {
                 crawlHtml("http://bbs.tianya.cn" + element.getAttributes().getValue("href"));
            }
        }
    }

    public int getCrawlCount(){
        return count;
    }

    public boolean isover(){
        return pool.isTerminated();
    }

    /**
     * ץȡhtml�е���������
     * @param
     */
    private void crawlPost(final String html) {
        pool.execute(new SavePost(html));
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
                    if (content.contains("uid=\"") && content.contains("uname=\"")) {
                        int start = content.indexOf("uid=\"");
                        int end = content.indexOf("uname=\"");
                        String uid = content.substring(start + 5, end - 2);
                        String userName = content.substring(end + 7, content.length()).split("\">")[0];
                        postReply.setUid(uid);
                        postReply.setName(userName);
                        if (i == 0) {
                            post.setAuthor(uid);
                            post.setName(userName);
                        }
                    } else if (content.contains("ʱ��")) {
                        postReply.setTime(content.split("ʱ�䣺")[1]);
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
                 * ȥ��ͼƬ
                 * ��ͼƬ��html �����滻�ɿ�
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
            }
        }
        return post;
    }


    class SavePost implements Runnable{
        private String html;

        SavePost(String html) {
            this.html = html;
        }

        @Override
        public void run() {
            final  List<Element> hrefList = ParseUtils.getAllHref(html, "a href=\"/post-" + nextUrlItem);
            if (hrefList != null && hrefList.size() > 0) {
                for (Element element : hrefList) {
                    Attributes attributes = element.getAttributes();
                    String postUrl =  attributes.getValue("href");
                    String htmlContent = httpConnectionManager.getHtml("http://bbs.tianya.cn"+postUrl);
                    Source source = new Source(htmlContent);
                    Post post = new Post();
                    post = setPostContent(post, source);
                    post = setAuthor(post, source);
                    post = setPostReplyMessage(post, source);
                    FileUtils.saveFile(post.getXmlContent(), crawlDataFilePath+"\\" + postUrl + ".xml");
                    ObserveCenter.getInstance().notifyChange(); //ˢ�½���
                    count++;
                }
            }
        }
    }


    protected void stopCrawl(){
        pool.shutdownNow();
    }
}
