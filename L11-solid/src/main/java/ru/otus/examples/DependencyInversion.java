package ru.otus.examples;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class DependencyInversion {

  interface SteeringWheel {

  }

  interface Engine {

  }

  /*Правильное применение принципа*/
  class Car {
    private SteeringWheel steeringWheel;
    private Engine engine;

    public Car(SteeringWheel steeringWheel, Engine engine) {
      this.steeringWheel = steeringWheel;
      this.engine = engine;
    }
  }
}
