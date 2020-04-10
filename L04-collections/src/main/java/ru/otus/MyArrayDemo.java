package ru.otus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey
 * created on 23.07.18.
 */

public class MyArrayDemo {

    public static void main(String[] args) throws Exception {

        int arraySizeMax = 1_000_000;
        int arraySizeInit = 10;
        ///////////
        long summ1 = 0;
        try (MyArrayInt myArr = new MyArrayInt(arraySizeInit)) {
            long begin = System.currentTimeMillis();

            for (int idx = 0; idx < arraySizeMax; idx++) {
                myArr.setValue(idx, idx);
            }

            for (int idx = 0; idx < arraySizeMax; idx++) {
                summ1 += myArr.getValue(idx);
            }
            System.out.println("myArr time:" + (System.currentTimeMillis() - begin));
        }
        ////
        System.out.println("-----");
        long summ2 = 0;
        List<Integer> arrayList = new ArrayList<>(arraySizeInit);
        long begin = System.currentTimeMillis();

        for (int idx = 0; idx < arraySizeMax; idx++) {
            arrayList.add(idx, idx);
        }

        for (int idx = 0; idx < arraySizeMax; idx++) {
            summ2 += arrayList.get(idx);
        }
        System.out.println("ArrayList time:" + (System.currentTimeMillis() - begin));
        System.out.println("summ1:" + summ1 + ", summ2:" + summ2);
    }
}
