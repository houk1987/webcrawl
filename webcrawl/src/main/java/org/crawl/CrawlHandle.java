package org.crawl;

/**
 * Created by lenovo on 2014/12/16.
 */
public interface CrawlHandle {
    void crawl(String crawlDataFilePath); //ץȡ
    void cancelCrawl();    //ȡ��ץȡ
    int crawlCount();
    boolean over();
}
