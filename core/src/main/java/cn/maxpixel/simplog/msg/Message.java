package cn.maxpixel.simplog.msg;

import cn.maxpixel.simplog.Level;
import cn.maxpixel.simplog.config.LogConfig;
import org.fusesource.jansi.Ansi;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;

public final class Message {
    private volatile Instant instant;
    private volatile Caller caller;
    private volatile Level level;
    private volatile String msg;
    private final Object[] args = new Object[5];
    private volatile int argLength;
    private volatile Throwable ex;
    private volatile Object[] arguments;

    private volatile boolean ready = true;

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.argLength = 0;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object arg0) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.args[0] = arg0;
        this.argLength = 1;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object arg0, Object arg1) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.args[0] = arg0;
        this.args[1] = arg1;
        this.argLength = 2;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object arg0, Object arg1, Object arg2) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.args[0] = arg0;
        this.args[1] = arg1;
        this.args[2] = arg2;
        this.argLength = 3;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object arg0, Object arg1, Object arg2, Object arg3) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.args[0] = arg0;
        this.args[1] = arg1;
        this.args[2] = arg2;
        this.args[3] = arg3;
        this.argLength = 4;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.args[0] = arg0;
        this.args[1] = arg1;
        this.args[2] = arg2;
        this.args[3] = arg3;
        this.args[4] = arg4;
        this.argLength = 5;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Throwable ex, Object[] arguments) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.ex = ex;
        this.arguments = arguments;
        this.argLength = arguments.length;
        return this;
    }

    public Message init(Instant instant, Caller caller, Level level, String msg, Object[] arguments) {
        if (!ready) throw new IllegalStateException("Message is not ready");
        this.ready = false;
        this.instant = instant;
        this.caller = caller;
        this.level = level;
        this.msg = msg;
        this.arguments = arguments;
        if (arguments[arguments.length - 1] instanceof Throwable) {
            this.ex = (Throwable) arguments[arguments.length - 1];
            this.argLength = arguments.length - 1;
        } else this.argLength = arguments.length;
        return this;
    }

    public boolean isReady() {
        return ready;
    }

    public void ready() {
        this.argLength = -1;
        this.arguments = null;
        this.ready = true;
    }

    public String format(String name, String creator) {
        if (ready) throw new IllegalStateException("Message is not initialized");
        if (argLength == -1) throw new IllegalStateException("Message initialization is not completed");
        LogConfig.Functions functions = LogConfig.get().functions;
        String formatted = argLength == 0 || !functions.curlyBracketsFormat ? msg :
                Formatter.replaceParams(msg, argLength <= 5 && arguments == null ? args : arguments, argLength);
        boolean stringFormat = functions.stringFormat, messageFormat = functions.messageFormat;
        if (stringFormat || messageFormat) {
            Object[] o = arguments == null ? Arrays.copyOf(args, argLength) : Arrays.copyOf(arguments, argLength);
            if (stringFormat) formatted = String.format(formatted, o);
            if (messageFormat) formatted = MessageFormat.format(formatted, o);
        }
        formatted = Formatter.format(instant, caller, name, creator, level, formatted);
        return formatted;
    }
}