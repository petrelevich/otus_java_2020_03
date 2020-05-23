package ru.otus.prototype;

import java.util.Calendar;

/**
 * @author sergey
 * created on 19.09.18.
 */
public class Demo {
  public static void main(String[] args) {
    Sheep original = new Sheep("unknown");
    System.out.println(original);

    Sheep cloned = original.clone();

    System.out.println(original.equals(cloned));

    cloned.setName("Dolly");
    System.out.println(cloned);

    System.out.println(original.equals(cloned));
  }
}
