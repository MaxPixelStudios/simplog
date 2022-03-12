package cn.maxpixel.simplog;

public interface Filter {
    boolean isLoggable(Level level, String name, String msg);
}