package org.crawl.sina;

import javafx.geometry.Pos;
import net.htmlparser.jericho.Element;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.crawl.tianya.vo.Post;
import org.crawl.tianya.vo.PostReply;
import org.tools.FileUtils;
import org.tools.HtmlParser;
import org.tools.ParseUtils;
import org.protocl.httpclient.HttpConnectionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.ץȡ���˲�����ҳ�������ӵ�html
 * 2.�����е�html ����ҳ���ļ���ʽ�����ڱ���
 * Created by lenovo on 2014/12/15.
 */
public class SinaBlog {

    //���˲��͵���ҳ��ַ
    private final String SINA_BOLG_URL = "http://blog.sina.com.cn/";
    private ExecutorService pool;
    private String crawlDataFilePath; //ץȡ�����ļ������ַ(�ļ���)
    private HttpConnectionManager httpConnectionManager;

    /**
     * ���캯��
     *
     * @param httpConnectionManager
     */
    public SinaBlog(HttpConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
        pool = Executors.newCachedThreadPool();
    }

    public void setCrawlDataFilePath(String crawlDataFilePath) {
        this.crawlDataFilePath = crawlDataFilePath;
    }

    /**
     * ץȡ���� html ����
     */
    public void crawl() {
        /**
         * ץȡ���˲�����ҳ�����еĲ�������
         */
        List<String> urlList = getUrlList(this.httpConnectionManager.getHtml(SINA_BOLG_URL), "a href=\"http://blog.sina.com.cn/s/blog");
        for (String url : urlList) {
           final String html = this.httpConnectionManager.getHtml(url);
            final String id = getID(url);
            Post post = getPost(id, html);
            saveHtml(post.getXmlContent(),crawlDataFilePath+"\\"+url+".xml");
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    getBlogArticlelist(id, getUid(html)); //��ȡ����Ŀ¼�����еĲ���
                }
            });
        }
    }

    private String getID(String url){
        String id = url.replaceAll("http://blog.sina.com.cn/s/blog_","");
        return id.substring(0,id.indexOf("."));
    }

    private String getUid(String html) {
        List<Element> profileList = ParseUtils.getAllHref(html, "a  href=\"http://blog.sina.com.cn/s/profile_");
        for (Element element : profileList) {
            String url = element.getAttributes().getValue("href");
            String uid = url.split("_")[1];
            uid = uid.replaceAll(".html", "");
            return uid;
        }
        return "";
    }

    private int getCount(String html) {
        Element element = ParseUtils.getTagElement(html, "span style='color:#888888;'");
        String content = element.getContent().toString();
        content = content.replaceAll("��", "");
        content = content.replaceAll("ҳ", "");
        return Integer.valueOf(content);
    }

    /**
     * ��ȡĿ¼�б��еĲ���
     */
    private void getBlogArticlelist(String id,String uid) {
        //��ȡ���е�ҳ��
        String firstArticlelistUrl = getArticlelistUrl(uid, 1); //Ĭ�ϻ�ȡ��һҳ�����б�
        String firstArticlelistHtml = httpConnectionManager.getHtml(firstArticlelistUrl);
        int count = getCount(firstArticlelistHtml);
        for (int i = 0; i < count; i++) {
            String temp = httpConnectionManager.getHtml(getArticlelistUrl(uid, i + 1));
            List<String> urlList = getUrlList(temp, "a");
            for (String url : urlList) {
                String html = httpConnectionManager.getHtml(url);
                getPost(id,html);
            }
        }
    }

    private String getArticlelistUrl(String uid, int index) {
        return "http://blog.sina.com.cn/s/articlelist_" + uid + "_0_" + index + ".html";
    }

    private List<String> getUrlList(String html, String rex) {
        List<Element> allBlogHref = ParseUtils.getAllHref(html, rex);
        List<String> urlList = new ArrayList<String>();
        if (allBlogHref != null && allBlogHref.size() > 0) {
            for (Element element : allBlogHref) {
                String url = element.getAttributes().getValue("href");
                if (url.contains("http://blog.sina.com.cn/s/blog")) {
                    if (url.contains("#")) {
                        url = url.substring(0, url.indexOf("#"));
                    }
                    urlList.add(url);
                }
            }
        }
        return urlList;
    }

    private Post getPost(String id,String html) {
        Post post = new Post();
        post.setAuthor(getUid(html));
        post.setName(getAuthor(html));
        post.setTime(getSendTime(html));
        post.setTitle(getTitle(html));
        post.setMessage(getMessage(html));
        //�������
        LinkedList<PostReply> list = getReply(id);
        post.setPostReplyList(list);
        return post;
    }

    private String getAuthor(String html) {
        Element element = ParseUtils.getTagElement(html, "strong id=\"ownernick\"");
        return element.getContent().toString();
    }

    private String getTitle(String html) {
        Element element = ParseUtils.getTagElement(html, "title");
        System.out.println();
        return element.getContent().toString();
    }

    private String getSendTime(String html) {
        Element element = ParseUtils.getTagElement(html, "span class=\"time SG_txtc\"");
        System.out.println(element.getContent().toString());
        return element.getContent().toString();
    }

    private String getMessage(String html) {
        Element element = ParseUtils.getTagElement(html, "div id=\"sina_keyword_ad_area2\" class=\"articalContent   newfont_family\"");
        return HtmlParser.Html2Text(element.getContent().toString());
    }


    /**
     * ��html��ʽ���ļ�����ץȡ��html����
     */
    private void saveHtml(String htmlContent, String filePath) {
        FileUtils.saveFile(htmlContent, filePath);
    }


    private LinkedList<PostReply> getReply(String id){
        String url = "http://blog.sina.com.cn/s/comment_"+id+"_1.html?comment_v=articlenew";
        HttpConnectionManager httpConnectionManager1 = new HttpConnectionManager();
        String contentttt = httpConnectionManager1.getHtml(url);
        LinkedList<PostReply> postReplies = new LinkedList<PostReply>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(contentttt);
            ReplyJson replyJson = (ReplyJson) JSONObject.toBean(jsonObject, ReplyJson.class);
            JSONData jsonDatas = replyJson.getData();
            List<Object> dataInfos = jsonDatas.getComment_data();
            for (Object dataInfo : dataInfos) {
                MorphDynaBean morphDynaBean = (MorphDynaBean) dataInfo;
                String content = morphDynaBean.get("cms_body").toString();
                content = URLDecoder.decode(content, "UTF-8");
                PostReply postReply = new PostReply();
                postReply.setUid(morphDynaBean.get("comm_uid").toString());
                postReply.setName(morphDynaBean.get("uname").toString());
                postReply.setMessage(content);
                postReply.setTime(morphDynaBean.get("cms_pubdate").toString());
                postReplies.add(postReply);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postReplies;
    }
}
