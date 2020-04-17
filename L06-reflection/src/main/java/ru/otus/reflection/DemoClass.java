package ru.otus.reflection;

public class DemoClass {

    public int publicFieldForDemo;

    private String value = "initValue";

    public DemoClass(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    private void privateMethod() {
        System.out.println("privateMethod executed");
    }

    @Override
    @SimpleAnnotation
    public String toString() {
        return "DemoClass{" +
                "publicFieldForDemo=" + publicFieldForDemo +
                ", value='" + value + '\'' +
                '}';
    }
}
