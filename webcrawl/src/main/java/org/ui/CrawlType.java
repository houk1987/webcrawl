package org.ui;

import org.crawl.CrawlHandle;
import org.crawl.tianya.AsiaForum;
import org.protocl.httpclient.HttpConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by lenovo on 2014/12/23.
 */
public enum  CrawlType {
    Asia_Forum(new AsiaForum(new HttpConnectionManager()),"ÑÇÖÞÂÛÌ³");
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
        return crawlTypeList;
    }


    @Override
    public String toString() {
        return text;
    }
}
