package org.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * @author houk
 * ץȡ��
 * �û������Ľ�����
 * Created by lenovo on 2014/12/23.
 */
public class Crawl extends JDialog{

    /**
     * ��������
     */
    private static Crawl crawl = new Crawl();

    private JComboBox crawlTypeSelected; //ץȡ����ѡ��������
    private JButton crawlButton; //ץȡ��ť
    private JButton cancelButton;//ȡ����ť
    private static String crawlDataSavePath; //ץȡ���ݵı���·��
    private CrawlType crawlType;
    private CrawlWork crawlWork; //ץȡ


    /**
     * ���캯��
     */
    public Crawl() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //����Ϊϵͳ�����ʽ
            setLayout(null);//���ò���ģʽ
            initComponent(); //��ʼ�������е����
            addListener(); //��Ӽ���
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }


    /**
     * ��ȡ����
     * @return
     */
    public static Crawl getInstance() {
        return crawl;
    }

    public static String getCrawlDataSavePath() {
        return crawlDataSavePath;
    }

    /**
     * ��д���ڳ�ʼ������
     * ���ô��ڴ�С
     * ���ô�����ʾλ�ã�������ʾ��
     */
    @Override
    protected void dialogInit() {
        super.dialogInit();
        setSize(500,200); //���ô��ڴ�С
        setLocationRelativeTo(null); //���ô��ھ�����ʾ
    }

    /**
     * ��д�رմ��ڵĺ���
     * �ڹرմ��ڵ�ͬʱ����������
     */
    @Override
    public void dispose() {
        super.dispose();  //�رմ���
        System.exit(0);  //��������
    }

    /**
     * ��Ӵ��ڼ���
     * ����˴��ڼ���
     */
    private void addListener(){
        //��Ӵ��ڼ���
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();  //�رմ���
            }
        });

        crawlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * ����ץȡ֮ǰ��Ҫ�û�ѡ���ץȡ����λ��
                 */
                JFileChooser jfc=new JFileChooser();//�ļ�ѡ����
                jfc.setFileSelectionMode(1);//�趨ֻ��ѡ���ļ���
                jfc.setDialogTitle("ѡ�񱣴����ݵ��ļ���");
                int state=jfc.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
                if(state==1){
                    return;//�����򷵻�
                }else{
                    File f=jfc.getSelectedFile();//fΪѡ�񵽵�Ŀ¼
                    crawlDataSavePath = f.getAbsolutePath();
                    System.out.println("�ļ������·��"+crawlDataSavePath);
                    crawlWork = new CrawlWork(crawlType);
                    crawlWork.execute();
                    crawlButton.setEnabled(false);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!crawlWork.isCancelled()){
                    crawlWork.cancel(true);
                }
                crawlWork = null;
                crawlButton.setEnabled(true);
            }
        });
    }

    /**
     * ��ʼ�������е����
     */
    private void initComponent(){
       JLabel crawlTypeLabel = new JLabel("ѡ������:");
       crawlTypeLabel.setBounds(10, 10, 100, 21); //����λ�ô�С
       add(crawlTypeLabel);

        /**
         * ����ѡ��������
         */
       crawlTypeSelected = new JComboBox((java.util.Vector) CrawlType.getAllCrawlType());
       crawlTypeSelected.setBounds(70,10,100,20);
       add(crawlTypeSelected);
       crawlType = (CrawlType) crawlTypeSelected.getItemAt(0);

        /**
         * �ײ���ť���
         */
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,0));
       buttonPanel.setBounds(0,this.getHeight()-100,this.getWidth(),100);
       add(buttonPanel);

        /**
         * ץȡ��ť
         */
       crawlButton = new JButton("ץȡ");
       buttonPanel.add(crawlButton);

        /**
         * ȡ����ť
         */
       cancelButton = new JButton("ȡ��");
       buttonPanel.add(cancelButton);
    }


    /**
     * ����ץȡ����
     * ��ʾץȡ�Ĵ���
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Crawl.getInstance().setVisible(true);
            }
        });
    }
}
