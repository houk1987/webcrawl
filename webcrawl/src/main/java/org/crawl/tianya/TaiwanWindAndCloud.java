package org.crawl.tianya;

import org.protocl.httpclient.HttpConnectionManager;

/**
 * Ã®ÕÂ∑Á‘∆
 * Created by lenovo on 2014/12/16.
 */
public class TaiwanWindAndCloud extends Tianya{
    protected TaiwanWindAndCloud(HttpConnectionManager httpConnectionManager) {
        super(httpConnectionManager, "http://bbs.tianya.cn/list-333-1.shtml", "333");
    }

    public void crawl(){
        crawlHtml(this.crawlUrl);
    }
}
