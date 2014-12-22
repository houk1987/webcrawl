package org.protocl.httpclient;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * 使用htmlUnit实现AJAX页面的抓取
 * Created by lenovo on 2014/12/2.
 */
public class WebClientManager {

    public String getHtml(String url){
        String html= "";
        WebClient webClient = createWebClient();
        try {
            final HtmlPage htmlPage = webClient.getPage(url);

            String pageXml = htmlPage.asXml(); //以xml的形式获取响应文本
            System.out.println(pageXml);
            return htmlPage.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public WebClient createWebClient(){
        WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(true); //开启JS 解析
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setTimeout(35000);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setActiveXNative(false);
        webClient.setCssEnabled(true);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        return webClient;
    }

    public static void main(String[] args) {
        WebClientManager webClientManager = new WebClientManager();
        webClientManager.getHtml("https://twitter.com/McCanElleGrande");
    }
}
