package cn.maxpixel.simplog;

import cn.maxpixel.simplog.config.LogConfig;
import cn.maxpixel.simplog.msg.Caller;
import cn.maxpixel.simplog.msg.CallerFinder;
import cn.maxpixel.simplog.msg.publish.MessagePublisher;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Objects;

public class LogManager {
    static {
        LogConfig.init();
    }

    private static final String FQCN = LogManager.class.getTypeName();
    private static final Object2ObjectMap<String, Logger> LOGGERS = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    private static MessagePublisher[] publishers;

    public static Logger getLogger() {
        Caller caller = CallerFinder.findCaller(FQCN);
        return getLogger(caller.className, caller);
    }

    public static Logger getLogger(String name) {
        return getLogger(name, CallerFinder.findCaller(FQCN));
    }

    private static Logger getLogger(String name, Caller creator) {
        Logger logger = LOGGERS.get(Objects.requireNonNull(name));
        if (logger == null) {
            LOGGERS.putIfAbsent(name, new Logger(name, creator.className));
            return LOGGERS.get(name);
        }
        return logger;
    }

    public static void reload() {
        publishers = LogConfig.get().publishers;
        LOGGERS.forEach((name, logger) -> logger.reload());
    }

    static MessagePublisher[] getPublishers() {
        return publishers;
    }

    static Filter[] getFilters() {
        return null;// TODO
    }
}