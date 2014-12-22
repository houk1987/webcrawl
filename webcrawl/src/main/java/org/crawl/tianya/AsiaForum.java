package org.crawl.tianya;

import net.htmlparser.jericho.Element;
import org.tools.FileUtils;
import org.tools.ParseUtils;
import org.protocl.httpclient.HttpConnectionManager;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ÑÇÖÞÂÛÌ³
 * Created by lenovo on 2014/12/1.
 */
public class AsiaForum extends Tianya{
    public AsiaForum(HttpConnectionManager httpConnectionManager) {
        super(httpConnectionManager, "http://bbs.tianya.cn/list-1089-1.shtml", "1089");
    }

    public void crawl(){
        crawlNextHtml(this.crawlUrl);
    }
}
