package org.ui;

import java.util.Observable;

/**
 * 用于通知界面刷新抓取的总数量
 * Created by lenovo on 2014/12/23.
 */
public class ObserveCenter extends Observable {
    private static ObserveCenter observeCenter;
    private ObserveCenter(){}

    public static ObserveCenter getInstance() {
        if (observeCenter == null) {
            observeCenter = new ObserveCenter();
        }
        return observeCenter;
    }

    /**
     * 发送变更消息, 相当于{@link #notifyChange(null)}
     *
     * @author lihzh
     * @date 2012-3-17 下午9:58:19
     */
    public void notifyChange() {
        notifyChange(null);
    }

    /**
     * 发送变更信息
     *
     * @param obj
     * @author lihzh
     * @date 2012-3-17 下午10:01:50
     */
    public void notifyChange(Object obj) {
        setChanged();
        notifyObservers(obj);
    }
}
