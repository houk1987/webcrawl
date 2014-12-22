package org.crawl.tianya;

import org.junit.Test;
import org.protocl.httpclient.HttpConnectionManager;

import static org.junit.Assert.*;

public class TianyaTest {

    @Test
    public void testCrawlNextHtml() throws Exception {
        String crawlUrl = "http://bbs.tianya.cn/list-1089-1.shtml";
        String nextItem = "1089";
        Tianya tianya = new Tianya(new HttpConnectionManager(), crawlUrl, nextItem);
        tianya.crawlNextHtml(crawlUrl);
    }
}