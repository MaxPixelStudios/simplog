package cn.maxpixel.simplog.config;

import cn.maxpixel.simplog.Level;
import cn.maxpixel.simplog.LogManager;
import cn.maxpixel.simplog.msg.Formatter;
import cn.maxpixel.simplog.msg.publish.MessagePublisher;
import cn.maxpixel.simplog.msg.publish.OutputStreamMessagePublisher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public final class LogConfig {
    private static final LogConfig DEFAULT = new LogConfig();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Level.class, new LevelSerializer())
            .registerTypeAdapter(Style.class, new Style.Deserializer())
            .registerTypeAdapter(Style.Color.class, new Style.Color.Serializer())
            .registerTypeAdapter(DateTimeFormatter.class, new DateTimeFormatterSerializer())
            .registerTypeAdapter(MessagePublisher.class, new MessagePublisher.Serializer())
            .setPrettyPrinting()
            .setVersion(0.1)
            .create();

    static {
        reload();
    }
    public static void init() {}

    private static String CONFIG_PATH;
    private static boolean GENERATE_CONFIG;

    private static volatile LogConfig CONFIG;

    @Since(0.1) public Level level = Level.INFO;
    @Since(0.1) public Functions functions = new Functions();
    @Since(0.1) public Style style = new Style();
    @Since(0.1) public String format = "[%time%] [%source_class%/%source_method%] [%name%] [%level%] %msg%\n";
    @Since(0.1) public MessagePublisher[] publishers = new MessagePublisher[] {OutputStreamMessagePublisher.STDOUT};

    public static final class Functions {
        @Since(0.1) @SerializedName("enable_string_format") public boolean stringFormat = false; // String.format
        @Since(0.1) @SerializedName("enable_message_format") public boolean messageFormat = false; // MessageFormat.format
        @Since(0.1) @SerializedName("enable_curly_brackets_format") public boolean curlyBracketsFormat = true; // {}
        @Since(0.1) @SerializedName("fetch_caller") public boolean fetchCaller = true;

        private Functions() {}

        private Functions(Functions functions) {
            this.stringFormat = functions.stringFormat;
            this.messageFormat = functions.messageFormat;
            this.curlyBracketsFormat = functions.curlyBracketsFormat;
            this.fetchCaller = functions.fetchCaller;
        }
    }

    private LogConfig() {}

    private LogConfig(LogConfig config) {
        this.level = config.level;
        this.functions = new Functions(config.functions);
        this.style = new Style(config.style);
        this.format = config.format;
        this.publishers = config.publishers;
    }

    public static LogConfig get() {
        return CONFIG;
    }

    public static void reload() {
        CONFIG_PATH = System.getProperty("simplog.config.path", "simplog-config.json");
        GENERATE_CONFIG = Boolean.getBoolean("simplog.config.generate_if_absent");
        CONFIG = load();
        Formatter.reload();
        LogManager.reload();
    }

    private static LogConfig load() {
        try (InputStream is = LogConfig.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (is != null) {
                try(InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    return GSON.fromJson(isr, LogConfig.class);
                }
            }
        } catch (IOException e) {
            // Keep going
        }
        File f = new File(CONFIG_PATH);
        if (f.exists()) {
            if (f.isDirectory()) throw new IllegalArgumentException("Not a file: " + CONFIG_PATH);
            try (FileReader reader = new FileReader(CONFIG_PATH)) {
                return GSON.fromJson(reader, LogConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to load config file: " + CONFIG_PATH + ", using default config.");
            }
        }
        if (GENERATE_CONFIG) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                try (FileWriter writer = new FileWriter(f)) {
                    GSON.toJson(DEFAULT, writer);
                }
            } catch (IOException e) {
                System.err.println("Failed to write default config to " + CONFIG_PATH);
                e.printStackTrace();
            }
        }
        return new LogConfig(DEFAULT);
    }
}