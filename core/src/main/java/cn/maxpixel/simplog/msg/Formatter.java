package cn.maxpixel.simplog.msg;

import cn.maxpixel.simplog.Level;
import cn.maxpixel.simplog.config.LogConfig;
import cn.maxpixel.simplog.config.Style;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.Instant;
import java.util.Objects;

public final class Formatter {
    /**
     * Message
     */
    private static final byte TYPE_MSG = 0;
    /**
     * Level
     */
    private static final byte TYPE_LEVEL = 1;
    /**
     * Logger name
     */
    private static final byte TYPE_NAME = 2;
    /**
     * Time when the log is started to process
     */
    private static final byte TYPE_TIME = 3;
    /**
     * Class of the method that attempts to log this message
     */
    private static final byte TYPE_SOURCE_CLASS = 4;
    /**
     * Method that attempts to log this message
     */
    private static final byte TYPE_SOURCE_METHOD = 5;
    private static final byte TYPE_STRING = 6;
    /**
     * Class that created this logger
     */
    private static final byte TYPE_CREATOR = 7;

    private static final ObjectArrayList<String> strings = new ObjectArrayList<>();
    private static final ByteArrayList components = new ByteArrayList();

    private static Style style;

    static {
        LogConfig.init();
    }

    public static void reload() {
        strings.clear();
        components.clear();
        style = LogConfig.get().style;
        String formatString = LogConfig.get().format;
        int index = formatString.indexOf('%');
        if (index == -1) { // No variables
            components.add(TYPE_STRING);
            strings.add(formatString);
            return;
        }
        { // First
            int next = formatString.indexOf('%', index + 1);
            components.add(TYPE_STRING);
            int j = next - index; // Calculated length contains a % char
            if (j > 3 && j <= 14) { // May contain a variable("msg".length() == 3, "source_method".length() == 13)
                strings.add(formatString.substring(0, index));
                identifyVariable(formatString, index, next);
                index = next;
            } else { // No variables
                strings.add(formatString);
                return;
            }
        }
        for (int i = formatString.indexOf('%', index + 1); i != -1;
             i = formatString.indexOf('%', i + 1)) { // Rest
            int next = formatString.indexOf('%', i + 1);
            components.add(TYPE_STRING);
            int j = next - i;
            if (j > 3 && j <= 14) {
                strings.add(formatString.substring(index + 1, i));
                identifyVariable(formatString, i, next);
            } else {
                if (next == -1) {
                    strings.add(formatString.substring(index + 1));
                    return;
                } else strings.add(formatString.substring(index + 1, next + 1));
            }
            i = next;
            index = next;
        }
        components.add(TYPE_STRING);
        strings.add(formatString.substring(index + 1));
    }

    private static void identifyVariable(String formatString, int index, int next) {
        switch (formatString.substring(index + 1, next)) {
            case "msg":
                components.add(TYPE_MSG);
                break;
            case "level":
                components.add(TYPE_LEVEL);
                break;
            case "name":
                components.add(TYPE_NAME);
                break;
            case "time":
                components.add(TYPE_TIME);
                break;
            case "source_class":
                components.add(TYPE_SOURCE_CLASS);
                break;
            case "source_method":
                components.add(TYPE_SOURCE_METHOD);
                break;
            case "creator":
                components.add(TYPE_CREATOR);
            default:
                components.add(TYPE_STRING);
                strings.add(formatString.substring(index, next + 1));
                break;
        }
    }

    public static String format(Instant instant, Caller caller, String name, String creator, Level level, String msg) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        Objects.requireNonNull(msg);
        Objects.requireNonNull(level);
        Objects.requireNonNull(name);
        Objects.requireNonNull(caller);
        for (byte component : components) {
            switch (component) {
                case TYPE_MSG:
                    sb.append(msg);
                    break;
                case TYPE_LEVEL:
                    sb.append(level.name);
                    break;
                case TYPE_NAME:
                    sb.append(name);
                    break;
                case TYPE_TIME:
                    style.time.formatTo(instant, sb);
                    break;
                case TYPE_SOURCE_CLASS:
                    sb.append(caller.className);
                    break;
                case TYPE_SOURCE_METHOD:
                    sb.append(caller.methodName);
                    break;
                case TYPE_STRING:
                    sb.append(strings.get(index++));
                    break;
                case TYPE_CREATOR:
                    sb.append(creator);
                    break;
            }
        }
        return sb.toString();
    }

    public static String replaceParams(String msg, Object[] params, int paramCount) {
        if (!msg.contains("{}") || paramCount <= 0) return msg;
        if (msg.equals("{}")) return params[0].toString();
        if (msg.equals("\\{}")) return msg;
        if (msg.equals("\\\\{}")) return params[0].toString();
        StringBuilder sb = new StringBuilder();
        for (int i = msg.indexOf("{}"), index = 0, j = 0; i < msg.length(); i = msg.indexOf("{}", i + 1)) {
            if (index == paramCount) throw new IndexOutOfBoundsException("Param count mismatch");
            if (msg.charAt(i - 1) == '\\') {
                sb.append(msg, j, i - 1);
                if (msg.charAt(i - 2) == '\\') {
                    sb.append(params[index++]);
                } else sb.append('{').append('}');
            } else {
                sb.append(msg, j, i);
                sb.append(params[index++]);
            }
            j = i + 2;
        }
        return sb.toString();
    }
}