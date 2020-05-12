package ru.otus.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class OpenClosed {

  //Плохой пример
  //Эту функцию без модификации не получится использовать, например, с TeeSet и другим алгоритмом
  private void messageProcessing(ArrayList<Message> messageList) {
    messageList.forEach(msg -> System.out.println(msg.toString()));
  }

  //Хороший пример
  private void messageProcessing(Collection<Message> messageList, Processor<Message> processor) {
    messageList.forEach(processor::action);
  }

  //применение хорошего примера
  //messageProcessing можно использовать без изменений
  void good() {

    //использование 1 (вызов, например, из другого класса)
    messageProcessing(new HashSet<>(), msg -> System.out.println(msg.toString()));

    //использование 2 (вызов, например, из другого класса)
    messageProcessing(new ArrayList<>(), msg -> System.out.println(msg.toString() + "R"));
  }

  @FunctionalInterface
  private interface Processor<T> {
    void action(T msg);
  }

  private class Message {
  }
}
