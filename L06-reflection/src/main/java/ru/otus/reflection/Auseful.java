package ru.otus.reflection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Auseful {
    public static void main(String[] args) throws NoSuchMethodException {

        var primitiveString = String.class.isPrimitive();
        var primitiveInt = int.class.isPrimitive();
        System.out.println("primitiveString:" + primitiveString + ", primitiveInt:" + primitiveInt);

        int[] arr = {1, 2};
        var isArray = arr.getClass().isArray();
        var componentArr = arr.getClass().getComponentType();
        System.out.println("isArray:" + isArray + ",  componentArr:" + componentArr);

        List<Integer> list = Arrays.asList(1, 2, 3);
        var isIterable = list.getClass().isAssignableFrom(Collections.class);
        var componentList = list.getClass().isAssignableFrom(Iterable.class);
        System.out.println("isIterable:" + isIterable + ", componentList:" + componentList);

        var hasAnnotation = DemoClass.class.getMethod("toString").isAnnotationPresent(SimpleAnnotation.class);
        System.out.println("hasAnnotation:" + hasAnnotation);
    }
}
