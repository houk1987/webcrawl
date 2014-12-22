package org.tools;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.List;

/**
 * Created by lenovo on 2014/12/1.
 */
public class ParseUtils {

    /**
     * ��ȡ��ҳ�е����г�����
     * @param html   ��ҳ�ı�����
     * @return
     */
    public static List<Element> getAllHref(String html,String  tag){
        if(html == null){
            System.out.println("htmlΪ��");
            return null;
        }

        final Source source = new Source(html);
        final List<Element> hrefList = source.getAllElements(tag);
        return hrefList;
    }

    public static Element getTagElement(String html,String  tag){
        if(html == null){
            System.out.println("htmlΪ��");
            return null;
        }
        final Source source = new Source(html);
        final Element element = source.getFirstElement(tag);
        return element;
    }
}
