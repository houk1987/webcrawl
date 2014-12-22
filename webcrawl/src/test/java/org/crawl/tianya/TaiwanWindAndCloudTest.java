package org.crawl.tianya;

import org.junit.Test;
import org.protocl.httpclient.HttpConnectionManager;

import static org.junit.Assert.*;

public class TaiwanWindAndCloudTest {

    @Test
    public void testCrawl() throws Exception {
        TaiwanWindAndCloud taiwanWindAndCloud = new TaiwanWindAndCloud(new HttpConnectionManager());
        taiwanWindAndCloud.crawl();
    }
}