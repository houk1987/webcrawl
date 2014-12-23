package org.crawl;

/**
 * Created by lenovo on 2014/12/16.
 */
public interface CrawlHandle {
    void crawl(String crawlDataFilePath); //抓取
    void cancelCrawl();    //取消抓取
    int crawlCount();
    boolean over();
}
