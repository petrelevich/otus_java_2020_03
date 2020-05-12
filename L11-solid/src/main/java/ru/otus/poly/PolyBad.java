package ru.otus.poly;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class PolyBad {
  public static void main(String[] args) {
    new PolyBad().go("BAD1", "create");
    new PolyBad().go("BAD2", "update");
  }

  private void go(String data, String command) {
    switch (command) {
      case "create":
        new Create().action(data);
        break;
      case "update":
        new Update().action(data);
        break;
      default:
        throw new IllegalArgumentException("Unknown command:" + command);
    }
  }


}
