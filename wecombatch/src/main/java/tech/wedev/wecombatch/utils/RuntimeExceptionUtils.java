package tech.wedev.wecombatch.utils;

public class RuntimeExceptionUtils {
    private RuntimeExceptionUtils() {

    }
    public static void isTrue (boolean express, String msg) {
        if (express) {
            throw new RuntimeException (msg);
        }
    }

    public static void isFalse (boolean express, String msg) {
        if (!express) {
            throw new RuntimeException(msg);
        }
    }
    public static <T> void isNull(T t, String msg) {
        if (t == null) {
            throw new RuntimeException(msg);
        }
    }
    public static <T> void isNotNull(T t, String msg) {
        if (t != null) {
            throw new RuntimeException (msg);
        }
    }
}
