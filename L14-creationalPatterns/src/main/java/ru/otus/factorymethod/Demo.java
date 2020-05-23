package ru.otus.factorymethod;

/**
 * @author sergey
 * created on 19.09.18.
 */
public class Demo {
  public static void main(String[] args) {
    Configuration config;

    config = ConfigurationFactory.getConfiguration("file");
    System.out.println(config.params());

    config = ConfigurationFactory.getConfiguration("db");
    System.out.println(config.params());

  }
}
