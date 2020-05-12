package ru.otus.poly;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class Create implements Operation {
  @Override
  public void action(String data) {
    System.out.println("create, data:" + data);
  }
}
