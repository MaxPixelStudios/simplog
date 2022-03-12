package cn.maxpixel.simplog.msg;

public final class Caller {
    public final String className;
    public final String methodName;

    Caller(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }
}