package org.ui;

import org.crawl.CrawlHandle;

import javax.swing.*;

/**
 * Created by lenovo on 2014/12/23.
 */
public class CrawlWork extends SwingWorker {
    private CrawlHandle crawlHandle;

    public CrawlWork(CrawlType crawlType) {
        this.crawlHandle = crawlType.getCrawlHandle();
    }

    @Override
    protected Object doInBackground() throws Exception {
        System.out.println("��ʼץȡ");
       crawlHandle.crawl(Crawl.getCrawlDataSavePath());
//        while (true){
//            Thread.sleep(1000);
//            System.out.println("1111");
//        }
       return null;
    }

    @Override
    protected void done() {
        super.done();
        if(isCancelled()){
            System.out.println("ȡ��ץȡ");
            crawlHandle.cancelCrawl();
        }
    }
}
