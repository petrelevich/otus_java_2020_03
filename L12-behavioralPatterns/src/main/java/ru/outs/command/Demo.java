package ru.outs.command;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class Demo {
  public static void main(String[] args) {
    Executor executor = new Executor();

    executor.addCommand(new AdderABC());
    executor.addCommand(new Echo());
    executor.addCommand(new AdderABC());

    executor.addCommand(String::toUpperCase);

    executor.executeCommands();
  }
}
