package ru.outs.memento;

/**
 * @author sergey
 * created on 11.09.18.
 */
class Memento {
  private final State state;

  Memento(State state) {
    this.state = new State(state);
  }

  State getState() {
    return state;
  }
}
