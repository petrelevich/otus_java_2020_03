package var;

public class AnonymInspector {

  public static void main(String... __) {
    //SimpleClass myCl = new SimpleClass() {
    var myCl = new SimpleClass() {
      @Override
      public void print() {
        System.out.print("!!!!!!!");
        this.
        newMethod();
      }

      public void newMethod() {
        System.out.println("New method");
      }
    };

    SimpleClass myCl2 = new SimpleClass();

    myCl.print(); // "!!!!!!!New method"
        myCl.newMethod(); // Error
    myCl2.print(); // "Hello, World!"
  }

  public void doSmth(){
    //SimpleClass myCl = new SimpleClass() {
    var myCl = new SimpleClass() {
      @Override
      public void print() {
        System.out.print("!!!!!!!");

                newMethod();
      }

      public void newMethod() {
        System.out.println("New method");
      }
    };
  }

  public void doSmth1(){
    ///
  }
}


class SimpleClass {
  void print() {
    System.out.println("Hello, World!");
  }
}
