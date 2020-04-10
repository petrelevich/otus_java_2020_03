package ru.otus;

/**
 * @author sergey
 * created on 24.07.18.
 */
public class MyMapInt {

    private final int size;

    private final String[] entriesKey;
    private final int[] entriesValue;
    private final boolean[] entriesOcupied;

    public MyMapInt(int size) {
        this.size = size;
        entriesKey = new String[this.size];
        entriesValue = new int[this.size];
        entriesOcupied = new boolean[this.size];
    }

    public void put(String key, int value) {
        int index = getIndex(key);

        this.entriesKey[index] = key;
        this.entriesValue[index] = value;
        this.entriesOcupied[index] = true;
    }

    public int get(String key) {
        int indexBase = getBaseIndex(key);

        for (int idx = indexBase; idx < entriesKey.length; idx++) {
            if (key.equals(entriesKey[idx])) {
                return entriesValue[idx];
            }
        }
        throw new RuntimeException("Value not found, key:" + key);
    }

    private int getBaseIndex(String key) {
        return (key.hashCode() & 0x7fffffff) % this.size;
    }

    private int getIndex(String key) {
        int index = getBaseIndex(key);

        while (index < this.size && entriesOcupied[index]) {
            index++;
        }

        if (index == this.size) {
            throw new RuntimeException("Map is full, key:" + key);
        }
        return index;
    }
}
