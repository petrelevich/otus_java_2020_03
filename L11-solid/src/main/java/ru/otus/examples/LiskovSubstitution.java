package ru.otus.examples;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class LiskovSubstitution {

    /*
    Классический пример нарушения принципа -
        квадрат наследует прямоугольник
     */


  public static void main(String[] args) {
    new LiskovSubstitution().apply();
  }

  private void apply() {
    double height = 2.0;
    double width = 3.0;

    Rectangle rectangle = new Rectangle();
    rectangle.setHeight(height);
    rectangle.setWidth(width);

    System.out.println("height:" + height + " rectangle.height:" + rectangle.height);
    System.out.println("width:" + width + " rectangle.width:" + rectangle.width);

    Rectangle rectangleStrange = new Square();
    rectangleStrange.setHeight(height);
    rectangleStrange.setWidth(width);

    System.out.println("height:" + height + " rectangleStrange.height:" + rectangleStrange.height);
    System.out.println("width:" + width + " rectangleStrange.width:" + rectangleStrange.width);
  }

  private static class Rectangle {
    private double height;
    private double width;

    public double area() {
      return this.height * this.width;
    }

    public void setHeight(double height) {
      this.height = height;
    }

    public void setWidth(double width) {
      this.width = width;
    }
  }

  private static class Square extends Rectangle {
    @Override
    public void setHeight(double height) {
      super.setHeight(height);
      super.setWidth(height);
    }

    @Override
    public void setWidth(double width) {
      this.setHeight(width);
    }
  }
}
