package org.crawl.tianya;

import org.crawl.CrawlHandle;
import org.protocl.httpclient.HttpConnectionManager;

/**
 * Ã®ÕÂ∑Á‘∆
 * Created by lenovo on 2014/12/16.
 */
public class TaiwanWindAndCloud extends Tianya implements CrawlHandle{
    public TaiwanWindAndCloud(HttpConnectionManager httpConnectionManager) {
        super(httpConnectionManager, "http://bbs.tianya.cn/list-333-1.shtml", "333");
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
