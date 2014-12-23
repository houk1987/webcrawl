package org.ui;

import java.util.Observable;

/**
 * ����֪ͨ����ˢ��ץȡ��������
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
     * ���ͱ����Ϣ, �൱��{@link #notifyChange(null)}
     *
     * @author lihzh
     * @date 2012-3-17 ����9:58:19
     */
    public void notifyChange() {
        notifyChange(null);
    }

    /**
     * ���ͱ����Ϣ
     *
     * @param obj
     * @author lihzh
     * @date 2012-3-17 ����10:01:50
     */
    public void notifyChange(Object obj) {
        setChanged();
        notifyObservers(obj);
    }
}
