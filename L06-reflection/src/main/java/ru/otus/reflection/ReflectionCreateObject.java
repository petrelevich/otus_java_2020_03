package ru.otus.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author sergey
 * created on 30.11.18.
 */
public class ReflectionCreateObject {
    public static void main(String[] args) throws Exception {

        Class<DemoClass> clazz = DemoClass.class;
        System.out.println("Class Name:" + clazz.getSimpleName());

        Constructor<?>[] constructors = clazz.getConstructors();
        System.out.println("--- constructors:");
        System.out.println(Arrays.toString(constructors));

        System.out.println("--- creating new object:");
        Constructor<DemoClass> constructor = clazz.getConstructor(String.class);
        DemoClass object = constructor.newInstance("testVal");
        System.out.println("value:" + object.getValue());

    }
}
