package cn.maxpixel.simplog.config;

import cn.maxpixel.simplog.Level;
import com.google.gson.*;
import com.google.gson.annotations.Since;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.fusesource.jansi.Ansi;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class Style {
    @Since(0.1) public Map<Level, Color> color;
    @Since(0.1) public DateTimeFormatter time = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Style() {
        color = new Object2ObjectOpenHashMap<>();
        color.put(Level.TRACE, new Color(Ansi.Color.BLACK, true));
        color.put(Level.DEBUG, new Color(Ansi.Color.WHITE, false));
        color.put(Level.INFO, new Color(Ansi.Color.WHITE, true));
        color.put(Level.WARN, new Color(Ansi.Color.YELLOW, false));
        color.put(Level.ERROR, new Color(Ansi.Color.RED, false));
        color.put(Level.FATAL, new Color(Ansi.Color.RED, true));
    }

    public Style(Style style) {
        this.color = style.color;
        this.time = style.time;
    }

    public static class Deserializer implements JsonDeserializer<Style> {
        @Override
        public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Style style = new Style();
            style.color.putAll(context.deserialize(obj.get("color"), Map.class));
            style.time = context.deserialize(obj.get("time"), DateTimeFormatter.class);
            return style;
        }
    }

    public static final class Color {
        public enum Type {
            COLOR_8,
            COLOR_256,
            TRUE_COLOR
        }

        public final Type type;
        public final int value;

        private final Ansi.Color val;
        public final boolean bright;

        private Color(Type type, int value) {
            if (value < 0) throw new IllegalArgumentException("value must be positive");
            this.type = type;
            this.value = value;
            this.val = null;
            this.bright = false;
        }

        private Color(Ansi.Color val, boolean bright) {
            this.type = Type.COLOR_8;
            this.value = -1;
            this.val = val;
            this.bright = bright;
        }

        public Ansi applyColor(Ansi ansi) {
            switch (type) {
                case COLOR_8:
                    if (bright) ansi.fgBright(val);
                    else ansi.fg(val);
                    break;
                case COLOR_256:
                    ansi.fg(value);
                    break;
                case TRUE_COLOR:
                    ansi.fgRgb(value);
                    break;
            }
            return ansi;
        }

        public static final class Serializer implements JsonSerializer<Color>, JsonDeserializer<Color> {
            @Override
            public Color deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String[] sa = json.getAsString().split(":");
                if (sa.length != 2) throw new JsonParseException("Invalid color format");
                Type type;
                switch (sa[0]) {
                    case "8":
                        type = Type.COLOR_8;
                        break;
                    case "256":
                        type = Type.COLOR_256;
                        break;
                    case "truecolor":
                        type = Type.TRUE_COLOR;
                        break;
                    default: throw new JsonParseException("Invalid color type");
                }
                switch (type) {
                    case COLOR_8:
                        String[] sa1 = sa[1].split(" ");
                        if (sa1.length == 2 && sa1[0].equalsIgnoreCase("bright")) {
                            return new Color(Ansi.Color.valueOf(sa1[1].toUpperCase()), true);
                        } else return new Color(Ansi.Color.valueOf(sa1[0].toUpperCase()), false);
                    case COLOR_256:
                    case TRUE_COLOR:
                        return new Color(type, Integer.parseInt(sa[1]));
                    default: throw new JsonParseException("Should not get here");
                }
            }

            @Override
            public JsonElement serialize(Color src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                String ret;
                switch (src.type) {
                    case COLOR_8:
                        ret = "8:" + (src.bright ? "BRIGHT " + src.val : src.val);
                        break;
                    case COLOR_256:
                        ret = "256:" + src.value;
                        break;
                    case TRUE_COLOR:
                        ret = "truecolor:" + src.value;
                        break;
                    default: throw new JsonParseException("Unknown color type");
                }
                return new JsonPrimitive(ret);
            }
        }
    }
}