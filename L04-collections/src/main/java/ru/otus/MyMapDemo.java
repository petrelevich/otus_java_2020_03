package ru.otus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sergey
 * created on 24.07.18.
 */
public class MyMapDemo {

    public static void main(String[] args) {

        int mapSize = 200_000;
        String keyStr = "k";
        ///////////
        long summ1 = 0;
        MyMapInt myMap = new MyMapInt(mapSize * 2);
        long begin = System.currentTimeMillis();

        for (int idx = 0; idx < mapSize; idx++) {
            myMap.put(keyStr + idx, idx);
        }

        for (int idx = 0; idx < mapSize; idx++) {
            summ1 += myMap.get(keyStr + idx);
        }
        System.out.println("MyMapInt time:" + (System.currentTimeMillis() - begin));
        ////
        System.out.println("-----");
        long summ2 = 0;
        Map<String, Integer> hashMap = new HashMap<>(mapSize);
        begin = System.currentTimeMillis();

        for (int idx = 0; idx < mapSize; idx++) {
            hashMap.put(keyStr + idx, idx);
        }

        for (int idx = 0; idx < mapSize; idx++) {
            summ2 += hashMap.get(keyStr + idx);
        }
        System.out.println("HashMap time:" + (System.currentTimeMillis() - begin));
        System.out.println("summ1:" + summ1 + ", summ2:" + summ2);
    }
}
