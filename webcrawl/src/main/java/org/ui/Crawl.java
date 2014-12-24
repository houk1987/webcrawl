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
 * 抓取类
 * 用户操作的界面类
 * Created by lenovo on 2014/12/23.
 */
public class Crawl extends JDialog implements Observer,ActionListener {

    /**
     * 单例对象
     */
    private static Crawl crawl = new Crawl();

    private JComboBox crawlTypeSelected; //抓取类型选择下拉框
    private JButton crawlButton; //抓取按钮
    private JButton cancelButton;//取消按钮
    private JLabel crawlCountLable; //抓取总数
    private static String crawlDataSavePath; //抓取数据的保存路径
    private CrawlType crawlType;
    private CrawlWork crawlWork; //抓取
    private JLabel crawledtime_onsumingLable; //抓取耗时
    private Timer tmr = new Timer(1000, this);
    private int time;

    /**
     * 启动抓取程序
     * 显示抓取的窗口
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
     * 构造函数
     */
    public Crawl() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //设置为系统外观样式
            setLayout(null);//设置布局模式
            initComponent(); //初始化窗口中的组件
            addListener(); //添加监听
            ObserveCenter.getInstance().addObserver(this); //添加刷新总数量观察者
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
     * 获取单例
     * @return
     */
    public static Crawl getInstance() {
        return crawl;
    }

    public static String getCrawlDataSavePath() {
        return crawlDataSavePath;
    }

    /**
     * 重写窗口初始化函数
     * 设置窗口大小
     * 设置窗口显示位置（居中显示）
     */
    @Override
    protected void dialogInit() {
        super.dialogInit();
        setSize(500,200); //设置窗口大小
        setLocationRelativeTo(null); //设置窗口居中显示
    }

    /**
     * 重写关闭窗口的函数
     * 在关闭窗口的同时，结束程序
     */
    @Override
    public void dispose() {
        super.dispose();  //关闭窗口
        System.exit(0);  //结束程序
    }

    /**
     * 添加窗口监听
     * 添加了窗口监听
     */
    private void addListener(){
        //添加窗口监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();  //关闭窗口
            }
        });
        crawlButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    /**
     * 初始化窗口中的组件
     */
    private void initComponent(){
       JLabel crawlTypeLabel = new JLabel("选择类型：");
       crawlTypeLabel.setBounds(10, 10, 100, 21); //设置位置大小
       add(crawlTypeLabel);

        /**
         * 类型选择下拉框
         */
       crawlTypeSelected = new JComboBox((java.util.Vector) CrawlType.getAllCrawlType());
       crawlTypeSelected.setBounds(70,10,100,20);
       add(crawlTypeSelected);
       crawlType = (CrawlType) crawlTypeSelected.getItemAt(0);

        /**
         * 抓取总数
         */
        crawlCountLable = new JLabel("抓取数量：");
        crawlCountLable.setBounds(10, 40,100,21);
        add(crawlCountLable);

        /**
         * 抓取耗时
         */
        crawledtime_onsumingLable = new JLabel("耗时：");
        crawledtime_onsumingLable.setBounds(10, 60,200,21);
        add(crawledtime_onsumingLable);
        /**
         * 底部按钮面板
         */
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,0));
       buttonPanel.setBounds(0,this.getHeight()-100,this.getWidth(),100);
       add(buttonPanel);

        /**
         * 抓取按钮
         */
       crawlButton = new JButton("抓取");
       buttonPanel.add(crawlButton);

        /**
         * 取消按钮
         */
       cancelButton = new JButton("取消");
       buttonPanel.add(cancelButton);
    }


    @Override
    public void update(Observable o, Object arg) {
        //刷新界面
        System.out.println(crawlWork.getCount());
        crawlCountLable.setText("抓取数量："+crawlWork.getCount());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(crawlButton)){
            /**
             * 启动抓取之前需要用户选项保存抓取数据位置
             */
            JFileChooser jfc=new JFileChooser();//文件选择器
            jfc.setFileSelectionMode(1);//设定只能选择到文件夹
            jfc.setDialogTitle("选择保存数据的文件夹");
            int state=jfc.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
            if(state==1){
                return;//撤销则返回
            }else{
                File f=jfc.getSelectedFile();//f为选择到的目录
                crawlDataSavePath = f.getAbsolutePath();
                System.out.println("文件保存的路径"+crawlDataSavePath);
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
            crawledtime_onsumingLable.setText("耗时：");
        }else {
            crawledtime_onsumingLable.setText("耗时："+change(time++));
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

        return h+"时"+d+"分"+s+"秒";
    }
}
