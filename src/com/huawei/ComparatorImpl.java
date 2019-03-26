package com.huawei;

import java.util.Comparator;

public class ComparatorImpl implements Comparator<Car> {


    //这里是升序排序
    @Override
    public int compare(Car o1, Car o2) {
        if (null != o1 && null != o2) {
            if (o1.getInStartTime() > o2.getInStartTime()) {
                return 1;
            } else if (o1.getInStartTime() == o2.getInStartTime()) {
                return 0;
            }
        }
        return -1;
    }
}
