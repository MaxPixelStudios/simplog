package cn.maxpixel.simplog.msg;

import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

public final class CallerFinder {
    private static final JavaLangAccess jla = SharedSecrets.getJavaLangAccess();

    public static Caller findCaller(String name) {
        Throwable t = new Throwable();
        int depth = jla.getStackTraceDepth(t);
        boolean reached = false;
        for (int i = 1; i < depth; i++) {
            StackTraceElement ste = jla.getStackTraceElement(t, i);
            String className = ste.getClassName();
            if(className.startsWith("java.lang.reflect.") || className.startsWith("sun.reflect.")) continue;
            if(className.equals(name)) reached = true;
            else if (reached) return new Caller(className, ste.getMethodName());
        }
        throw new RuntimeException("Could not find caller");
    }
}