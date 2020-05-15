package ru.outs.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author sergey
 * created on 11.09.18.
 */
class Originator {
  //Фактически, это stack
  private final Deque<Memento> stack = new ArrayDeque<>();

  void saveState(State state) {
    stack.push(new Memento(state));
  }

  State restoreState() {
    return stack.pop().getState();
  }
}
