package org.ui;

import org.crawl.CrawlHandle;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by lenovo on 2014/12/23.
 */
public class CrawlWork extends SwingWorker<Boolean,Void> {
    private CrawlHandle crawlHandle;

    public CrawlWork(CrawlType crawlType) {
        this.crawlHandle = crawlType.getCrawlHandle();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        System.out.println("开始抓取");
        crawlHandle.crawl(Crawl.getCrawlDataSavePath());
        return true;
}

    @Override
    protected void done() {
        super.done();
        try {
            
            System.out.println("标志"+get());
            System.out.println("完成"+crawlHandle.over());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(isCancelled() ||crawlHandle.over() ){
            System.out.println("取消抓取");
            crawlHandle.cancelCrawl();
        }
    }

    public int getCount(){
       return  crawlHandle.crawlCount();
    }

    public void cancelWork(){
        crawlHandle.cancelCrawl();
        cancel(true);
    }
}
