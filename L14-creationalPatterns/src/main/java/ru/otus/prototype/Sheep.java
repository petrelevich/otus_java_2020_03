package ru.otus.prototype;

import java.util.Objects;

/**
 * @author sergey
 * created on 19.09.18.
 */
class Sheep implements Cloneable {
  private String name;

  Sheep(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Sheep{" +
        "name='" + name + '\'' +
        '}';
  }

  @Override
  public Sheep clone() {
    return new Sheep(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sheep sheep = (Sheep) o;
    return Objects.equals(name, sheep.name);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name);
  }
}
