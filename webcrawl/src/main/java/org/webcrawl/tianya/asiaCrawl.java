package org.webcrawl.tianya;

import net.htmlparser.jericho.Element;
import org.parse.ParseUtils;
import org.protocl.httpclient.HttpConnectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2014/12/1.
 */
public class asiaCrawl {

    public final static String url = "http://bbs.tianya.cn/list-1089-1.shtml";
    private final static HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
    private static int count;
    public static void tianyaCrawl(String url){
        final String html = httpConnectionManager.getHtml(url);
        count++;
        List<Element> getPostAllHref = ParseUtils.getAllHref(html,"a href=\"/post-1089");
        crawlpostHrefList(getPostAllHref);


        List<Element> getNextHref = ParseUtils.getAllHref(html,"a href=\"/list.jsp?item=1089&nextid=");
        for(Element element : getNextHref){
            net.htmlparser.jericho.Attributes childs =  element.getAttributes();
            final String nextUrl = "http://bbs.tianya.cn"+childs.getValue("href");
            System.out.println(nextUrl);
            tianyaCrawl(nextUrl);
        }
    }

    private static void crawlpostHrefList(List<Element> hrefList){
        if(hrefList!=null && hrefList.size()>0) {
            for (Element element : hrefList) {
                net.htmlparser.jericho.Attributes childs =  element.getAttributes();
                final String url = "http://bbs.tianya.cn"+childs.getValue("href");
                System.out.println("地址："+url);
                final String html = httpConnectionManager.getHtml(url);
                count++;
                System.out.println(count);
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("爬取总数"+count+"地址："+url);
//                        List<Element> getAllHref = ParseUtils.getAllHref(html);
//                        crawlpostHrefList(getAllHref);
//                    }
//                });
//                thread.start();
            }
        }
    }
}
