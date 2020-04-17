package ru.otus.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author sergey
 * created on 30.11.18.
 */
public class ReflectionMethod {
    public static void main(String[] args) throws Exception {

        Class<DemoClass> clazz = DemoClass.class;
        System.out.println("Class Name:" + clazz.getSimpleName());

        Method method = clazz.getMethod("toString");

        System.out.println("--- annotations:");
        Annotation[] annotations = method.getDeclaredAnnotations();
        System.out.println(Arrays.toString(annotations));

        System.out.println("--- modifiers:");
        int modifiers = method.getModifiers();
        System.out.println("isPublic:" + Modifier.isPublic(modifiers));
        System.out.println("isFinal:" + Modifier.isFinal(modifiers));
        System.out.println("isStatic:" + Modifier.isStatic(modifiers));

        System.out.println("--- execution:");
        var result = method.invoke(new DemoClass("demoVal"));
        System.out.println("result:" + result);
    }
}
