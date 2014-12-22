package org.tools;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by lenovo on 2014/12/16.
 */
public class FileUtils {

    /**
     *
     * @param content  ������ļ��ı�����
     * @param path     ������ļ�·��
     */
    public static void saveFile(String content, String path) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            fw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                   // System.out.println("����ɹ�");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
