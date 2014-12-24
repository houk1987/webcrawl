package org.ui;

import org.crawl.CrawlHandle;
import org.crawl.sina.SinaBlog;
import org.crawl.tianya.AsiaForum;
import org.crawl.tianya.TaiwanWindAndCloud;
import org.protocl.httpclient.HttpConnectionManager;
import java.util.List;
import java.util.Vector;

/**
 * Created by lenovo on 2014/12/23.
 */
public enum  CrawlType {
    Asia_Forum(new AsiaForum(new HttpConnectionManager()),"亚洲论坛"),
    Taiwan_WindAnd_Cloud(new TaiwanWindAndCloud(new HttpConnectionManager()),"台湾风云"),
    Sina_Blog(new SinaBlog(new HttpConnectionManager()),"新浪博客");
    private static Vector<CrawlType> crawlTypeList = new Vector<CrawlType>();
    private String text;
    private CrawlHandle crawlHandle;

    private CrawlType(CrawlHandle crawlHandle,String text) {
        this.text = text;
        this.crawlHandle = crawlHandle;
    }

    public String getText() {
        return text;
    }

    public CrawlHandle getCrawlHandle() {
        return crawlHandle;
    }

    public static List<CrawlType> getAllCrawlType(){
        crawlTypeList.add(Asia_Forum);
        crawlTypeList.add(Taiwan_WindAnd_Cloud);
        crawlTypeList.add(Sina_Blog);
        return crawlTypeList;
    }


    @Override
    public String toString() {
        return text;
    }
}
