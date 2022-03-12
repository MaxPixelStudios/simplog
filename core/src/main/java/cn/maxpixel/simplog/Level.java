package cn.maxpixel.simplog;

import java.util.Objects;

public final class Level {
    public static final Level OFF = new Level("OFF", (short) -1);

    public static final Level TRACE = new Level("TRACE", (short) 0);

    public static final Level DEBUG = new Level("DEBUG", (short) 1500);

    public static final Level INFO = new Level("INFO", (short) 3000);

    public static final Level WARN = new Level("WARN", (short) 4500);

    public static final Level ERROR = new Level("ERROR", (short) 6000);

    public static final Level FATAL = new Level("FATAL", (short) 7500);

    public static final Level ALL = new Level("ALL", (short) 10000);

    private static final Level[] PRE_DEFINED = new Level[] {OFF, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, ALL};

    public static Level[] getPreDefinedLevels() {
        Level[] levels = new Level[PRE_DEFINED.length];
        System.arraycopy(PRE_DEFINED, 0, levels, 0, PRE_DEFINED.length);
        return levels;
    }

    public final String name;
    public final short priority;

    public Level(String name, short priority) {
        if(priority < -1 || priority > 10000) throw new IllegalArgumentException();
        this.name = Objects.requireNonNull(name);
        this.priority = priority;
    }
}