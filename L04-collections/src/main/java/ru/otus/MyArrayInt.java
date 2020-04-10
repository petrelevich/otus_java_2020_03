package ru.otus;

import java.lang.reflect.Constructor;
import java.util.Objects;

import sun.misc.Unsafe;


/**
 * @author sergey
 * created on 22.07.18.
 */
public class MyArrayInt implements AutoCloseable {
    private final Unsafe unsafe;
    private final long elementSizeBytes;
    private int size;
    private long arrayBeginIdx;

    public MyArrayInt(int size) throws Exception {
        this.size = size;

        Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        unsafe = unsafeConstructor.newInstance();

        elementSizeBytes = Integer.SIZE / 8;
        arrayBeginIdx = unsafe.allocateMemory(this.size * elementSizeBytes);
    }

    public void setValue(long index, int value) {
        if (index == size) {
            this.size *= 2;
            arrayBeginIdx = unsafe.reallocateMemory(arrayBeginIdx, this.size * elementSizeBytes);
        }
        unsafe.putInt(index(index), value);
    }

    public int getValue(long index) {
        return unsafe.getInt(index(index));
    }

    private long index(long offset) {
        return arrayBeginIdx + offset * this.elementSizeBytes;
    }

    @Override
    public void close() {
        unsafe.freeMemory(arrayBeginIdx);
    }
}
