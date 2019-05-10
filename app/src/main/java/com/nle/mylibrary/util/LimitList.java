package com.nle.mylibrary.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date:2018/11/1
 * create by pengxl
 */
public class LimitList<E> {
    private List<E> dataRep = new ArrayList<>();
    private int capacity;

    public LimitList(int capacity) {
        this.capacity = capacity;
    }

    public void remove(E e) {
        dataRep.remove(e);
    }

    public int size() {
        return dataRep.size();
    }
    public void add(E e) {
        if (dataRep.size() == capacity) {
            removeHalf();
        }
        dataRep.add(e);
    }

    public void addAll(Collection<E> collection) {
        for (E e : collection) {
            add(e);
        }
    }

    public List<E> get() {
        return dataRep;
    }
    public E pop() {
        if (dataRep.size() != 0) {
            int lastIndex = dataRep.size() - 1;
            E e = dataRep.get(lastIndex);
            dataRep.remove(lastIndex);
            return e;
        }
        return null;
    }

    private void removeHalf() {
        int halfAbove = capacity / 2 + 1;
        for (int i = 0; i < halfAbove; i++) {
            dataRep.remove(0);
        }
    }


}
