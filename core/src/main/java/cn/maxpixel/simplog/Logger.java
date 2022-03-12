package cn.maxpixel.simplog;

import cn.maxpixel.simplog.config.LogConfig;
import cn.maxpixel.simplog.msg.Caller;
import cn.maxpixel.simplog.msg.CallerFinder;
import cn.maxpixel.simplog.msg.Message;
import cn.maxpixel.simplog.msg.MessageFactory;
import cn.maxpixel.simplog.msg.publish.MessagePublisher;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

public final class Logger {
    private static final String FQCN = Logger.class.getTypeName();
    private static final Clock CLOCK = Clock.systemDefaultZone();
    private final String name;
    private final String creator;
    private short priority;
    private boolean fetchCaller;
    private MessagePublisher[] publishers;
    private Filter[] filters;

    Logger(String name, String creator) {
        this.name = name;
        this.creator = creator;
        reload();
    }

    void reload() {
        this.priority = LogConfig.get().level.priority;
        this.fetchCaller = LogConfig.get().functions.fetchCaller;
        this.publishers = LogManager.getPublishers();
        this.filters = LogManager.getFilters();
    }

    private Caller fetchCaller() {
        return fetchCaller ? CallerFinder.findCaller(FQCN) : null;
    }

    // Core methods

    public boolean isLoggable(Level level) {
        return priority >= level.priority;
    }

    private void log(Level level, String msg, Message message) {
        try {
            for (Filter filter : filters) {
                if (!filter.isLoggable(level, name, msg)) return;
            }
            for (MessagePublisher publisher : publishers) {
                publisher.publish(level, message.format(name, creator));
            }
        } catch (IOException e) {
            System.err.println("Publisher failed to publish the message");
            e.printStackTrace();
        } finally {
            message.ready();
        }
    }

    public void log(Level level, String msg) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, (Throwable) null));
    }

    public void log(Level level, String msg, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t));
    }

    public void log(Level level, String msg, Object arg0, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, arg0));
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, arg0, arg1));
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, arg0, arg1, arg2));
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, arg0, arg1, arg2, arg3));
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, arg0, arg1, arg2, arg3, arg4));
    }

    public void log(Level level, String msg, Object arg0) {
        log(level, msg, arg0, null);
    }

    public void log(Level level, String msg, Object arg0, Object arg1) {
        log(level, msg, arg0, arg1, null);
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2) {
        log(level, msg, arg0, arg1, arg2, null);
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(level, msg, arg0, arg1, arg2, arg3, null);
    }

    public void log(Level level, String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(level, msg, arg0, arg1, arg2, arg3, arg4, null);
    }

    public void log(Level level, String msg, Object... args) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, args));
    }

    public void log(Level level, String msg, Throwable t, Object... args) {
        if(isLoggable(level)) log(level, msg, MessageFactory.getMessage().init(Instant.now(CLOCK), fetchCaller(), level, msg, t, args));
    }

    // Convenience methods

    public boolean isTraceLoggable() {
        return isLoggable(Level.TRACE);
    }

    public void trace(String msg) {
        log(Level.TRACE, msg);
    }

    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    public void trace(String msg, Object arg0, Throwable t) {
        log(Level.TRACE, msg, arg0, t);
    }

    public void trace(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.TRACE, msg, arg0, arg1, t);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.TRACE, msg, arg0, arg1, arg2, t);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.TRACE, msg, arg0, arg1, arg2, arg3, t);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.TRACE, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void trace(String msg, Object arg0) {
        log(Level.TRACE, msg, arg0);
    }

    public void trace(String msg, Object arg0, Object arg1) {
        log(Level.TRACE, msg, arg0, arg1);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.TRACE, msg, arg0, arg1, arg2);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.TRACE, msg, arg0, arg1, arg2, arg3);
    }

    public void trace(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.TRACE, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void trace(String msg, Object... args) {
        log(Level.TRACE, msg, args);
    }

    public void trace(String msg, Throwable t, Object... args) {
        log(Level.TRACE, msg, t, args);
    }

    public boolean isDebugLoggable() {
        return isLoggable(Level.DEBUG);
    }

    public void debug(String msg) {
        log(Level.DEBUG, msg);
    }

    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
    }

    public void debug(String msg, Object arg0, Throwable t) {
        log(Level.DEBUG, msg, arg0, t);
    }

    public void debug(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.DEBUG, msg, arg0, arg1, t);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.DEBUG, msg, arg0, arg1, arg2, t);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.DEBUG, msg, arg0, arg1, arg2, arg3, t);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.DEBUG, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void debug(String msg, Object arg0) {
        log(Level.DEBUG, msg, arg0);
    }

    public void debug(String msg, Object arg0, Object arg1) {
        log(Level.DEBUG, msg, arg0, arg1);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.DEBUG, msg, arg0, arg1, arg2);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.DEBUG, msg, arg0, arg1, arg2, arg3);
    }

    public void debug(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.DEBUG, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void debug(String msg, Object... args) {
        log(Level.DEBUG, msg, args);
    }

    public void debug(String msg, Throwable t, Object... args) {
        log(Level.DEBUG, msg, t, args);
    }

    public boolean isInfoLoggable() {
        return isLoggable(Level.INFO);
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    public void info(String msg, Object arg0, Throwable t) {
        log(Level.INFO, msg, arg0, t);
    }

    public void info(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.INFO, msg, arg0, arg1, t);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.INFO, msg, arg0, arg1, arg2, t);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.INFO, msg, arg0, arg1, arg2, arg3, t);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.INFO, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void info(String msg, Object arg0) {
        log(Level.INFO, msg, arg0);
    }

    public void info(String msg, Object arg0, Object arg1) {
        log(Level.INFO, msg, arg0, arg1);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.INFO, msg, arg0, arg1, arg2);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.INFO, msg, arg0, arg1, arg2, arg3);
    }

    public void info(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.INFO, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    public void info(String msg, Throwable t, Object... args) {
        log(Level.INFO, msg, t, args);
    }

    public boolean isWarnLoggable() {
        return isLoggable(Level.WARN);
    }

    public void warn(String msg) {
        log(Level.WARN, msg);
    }

    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    public void warn(String msg, Object arg0, Throwable t) {
        log(Level.WARN, msg, arg0, t);
    }

    public void warn(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.WARN, msg, arg0, arg1, t);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.WARN, msg, arg0, arg1, arg2, t);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.WARN, msg, arg0, arg1, arg2, arg3, t);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.WARN, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void warn(String msg, Object arg0) {
        log(Level.WARN, msg, arg0);
    }

    public void warn(String msg, Object arg0, Object arg1) {
        log(Level.WARN, msg, arg0, arg1);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.WARN, msg, arg0, arg1, arg2);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.WARN, msg, arg0, arg1, arg2, arg3);
    }

    public void warn(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.WARN, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void warn(String msg, Object... args) {
        log(Level.WARN, msg, args);
    }

    public void warn(String msg, Throwable t, Object... args) {
        log(Level.WARN, msg, t, args);
    }

    public boolean isErrorLoggable() {
        return isLoggable(Level.ERROR);
    }

    public void error(String msg) {
        log(Level.ERROR, msg);
    }

    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    public void error(String msg, Object arg0, Throwable t) {
        log(Level.ERROR, msg, arg0, t);
    }

    public void error(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.ERROR, msg, arg0, arg1, t);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.ERROR, msg, arg0, arg1, arg2, t);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.ERROR, msg, arg0, arg1, arg2, arg3, t);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.ERROR, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void error(String msg, Object arg0) {
        log(Level.ERROR, msg, arg0);
    }

    public void error(String msg, Object arg0, Object arg1) {
        log(Level.ERROR, msg, arg0, arg1);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.ERROR, msg, arg0, arg1, arg2);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.ERROR, msg, arg0, arg1, arg2, arg3);
    }

    public void error(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.ERROR, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void error(String msg, Object... args) {
        log(Level.ERROR, msg, args);
    }

    public void error(String msg, Throwable t, Object... args) {
        log(Level.ERROR, msg, t, args);
    }

    public boolean isFatalLoggable() {
        return isLoggable(Level.FATAL);
    }

    public void fatal(String msg) {
        log(Level.FATAL, msg);
    }

    public void fatal(String msg, Throwable t) {
        log(Level.FATAL, msg, t);
    }

    public void fatal(String msg, Object arg0, Throwable t) {
        log(Level.FATAL, msg, arg0, t);
    }

    public void fatal(String msg, Object arg0, Object arg1, Throwable t) {
        log(Level.FATAL, msg, arg0, arg1, t);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2, Throwable t) {
        log(Level.FATAL, msg, arg0, arg1, arg2, t);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Throwable t) {
        log(Level.FATAL, msg, arg0, arg1, arg2, arg3, t);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Throwable t) {
        log(Level.FATAL, msg, arg0, arg1, arg2, arg3, arg4, t);
    }

    public void fatal(String msg, Object arg0) {
        log(Level.FATAL, msg, arg0);
    }

    public void fatal(String msg, Object arg0, Object arg1) {
        log(Level.FATAL, msg, arg0, arg1);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2) {
        log(Level.FATAL, msg, arg0, arg1, arg2);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2, Object arg3) {
        log(Level.FATAL, msg, arg0, arg1, arg2, arg3);
    }

    public void fatal(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        log(Level.FATAL, msg, arg0, arg1, arg2, arg3, arg4);
    }

    public void fatal(String msg, Object... args) {
        log(Level.FATAL, msg, args);
    }

    public void fatal(String msg, Throwable t, Object... args) {
        log(Level.FATAL, msg, t, args);
    }
}