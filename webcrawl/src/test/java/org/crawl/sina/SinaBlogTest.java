package org.crawl.sina;

import org.junit.Test;
import org.protocl.httpclient.HttpConnectionManager;

import static org.junit.Assert.*;

public class SinaBlogTest {

    @Test
    public void testCrawl() throws Exception {
        SinaBlog sinaBlog = new SinaBlog(new HttpConnectionManager());
        sinaBlog.crawl();
    }
}