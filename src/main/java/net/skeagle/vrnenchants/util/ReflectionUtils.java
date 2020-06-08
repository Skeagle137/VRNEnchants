package net.skeagle.vrnenchants.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Field getDeclaredField(Class<?> clazz, String field) {
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        if (f == null) {
            throw new NullPointerException("Could not get declared field " + field + " from " + clazz.getName() + ".");
        }
        return f;
    }

    public static void setAccessable(Class<?> clazz, String field) {
        Field f = getDeclaredField(clazz, field);
        f.setAccessible(true);
        try {
            f.set(null, true);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldContent(Class<?> clazz, String field, String content) {
        Field f = getDeclaredField(clazz, field);
        f.setAccessible(true);
        Object o = null;
        try {
            o = f.get(content);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        if (o == null) {
            throw new NullPointerException("Could not get field content from field " + field + ".");
        }
        return o;
    }
}
