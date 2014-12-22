package org.parse;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by lenovo on 2014/12/1.
 */
public class ParseUtils {

    /**
     * 获取网页中的所有超链接
     * @param html   网页文本内容
     * @return
     */
    public static List<Element> getAllHref(String html,String  tag){
        if(html == null)return null;
        final Source source = new Source(html);
        final List<Element> hrefList = source.getAllElements(tag);
        return hrefList;
    }

}
