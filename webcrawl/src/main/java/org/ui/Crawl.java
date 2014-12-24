package org.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 * @author houk
 * ץȡ��
 * �û������Ľ�����
 * Created by lenovo on 2014/12/23.
 */
public class Crawl extends JDialog implements Observer,ActionListener {

    /**
     * ��������
     */
    private static Crawl crawl = new Crawl();

    private JComboBox crawlTypeSelected; //ץȡ����ѡ��������
    private JButton crawlButton; //ץȡ��ť
    private JButton cancelButton;//ȡ����ť
    private JLabel crawlCountLable; //ץȡ����
    private static String crawlDataSavePath; //ץȡ���ݵı���·��
    private CrawlType crawlType;
    private CrawlWork crawlWork; //ץȡ
    private JLabel crawledtime_onsumingLable; //ץȡ��ʱ
    private Timer tmr = new Timer(1000, this);
    private int time;

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

    /**
     * ���캯��
     */
    public Crawl() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //����Ϊϵͳ�����ʽ
            setLayout(null);//���ò���ģʽ
            initComponent(); //��ʼ�������е����
            addListener(); //��Ӽ���
            ObserveCenter.getInstance().addObserver(this); //���ˢ���������۲���
            tmr = new Timer(1000, this);
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
        crawlButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    /**
     * ��ʼ�������е����
     */
    private void initComponent(){
       JLabel crawlTypeLabel = new JLabel("ѡ�����ͣ�");
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
         * ץȡ����
         */
        crawlCountLable = new JLabel("ץȡ������");
        crawlCountLable.setBounds(10, 40,100,21);
        add(crawlCountLable);

        /**
         * ץȡ��ʱ
         */
        crawledtime_onsumingLable = new JLabel("��ʱ��");
        crawledtime_onsumingLable.setBounds(10, 60,200,21);
        add(crawledtime_onsumingLable);
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


    @Override
    public void update(Observable o, Object arg) {
        //ˢ�½���
        System.out.println(crawlWork.getCount());
        crawlCountLable.setText("ץȡ������"+crawlWork.getCount());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(crawlButton)){
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
                tmr.start();
            }
        }else if(e.getSource().equals(cancelButton)){
            if(!crawlWork.isCancelled()){
                crawlWork.cancelWork();
                tmr.stop();
            }
            crawlWork = null;
            crawlButton.setEnabled(true);
            crawledtime_onsumingLable.setText("��ʱ��");
        }else {
            crawledtime_onsumingLable.setText("��ʱ��"+change(time++));
        }
    }

    public static String change(int second){
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second%3600;
        if(second>3600){
            h= second/3600;
            if(temp!=0){
                if(temp>60){
                    d = temp/60;
                    if(temp%60!=0){
                        s = temp%60;
                    }
                }else{
                    s = temp;
                }
            }
        }else{
            d = second/60;
            if(second%60!=0){
                s = second%60;
            }
        }

        return h+"ʱ"+d+"��"+s+"��";
    }
}
