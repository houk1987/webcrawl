package org.crawl.tianya;

import org.crawl.CrawlHandle;
import org.protocl.httpclient.HttpConnectionManager;

/**
 * ������̳
 * Created by lenovo on 2014/12/1.
 */
public class AsiaForum extends Tianya implements CrawlHandle{
    public AsiaForum(HttpConnectionManager httpConnectionManager) {
        super(httpConnectionManager, "http://bbs.tianya.cn/list-1089-1.shtml", "1089");
    }

    @Override
    public void crawl(String crawlDataFilePath) {
        setCrawlDataFilePath(crawlDataFilePath);
        crawlHtml(this.crawlUrl);

    }

    @Override
    public void cancelCrawl() {
        stopCrawl();
    }

    @Override
    public int crawlCount() {
        return getCrawlCount();
    }

    @Override
    public boolean over() {
        return isover();
    }
}
