package me.sa_g6.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static  <T> T getVariable(Object obj, String name){
        return getVariable(obj.getClass(), obj, name);
    }

    public static  <T> T getVariable(Class<?> clazz, Object obj, String name){
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T invoke(Object obj, String name, Class<?>[] classes, Object... params){
        return invoke(obj.getClass(), obj, name, classes, params);
    }

    public static <T> T invoke(Class<?> clazz, Object obj, String name, Class<?>[] classes, Object... params){
        try {
            Method method = clazz.getDeclaredMethod(name,classes);
            method.setAccessible(true);
            return (T) method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
