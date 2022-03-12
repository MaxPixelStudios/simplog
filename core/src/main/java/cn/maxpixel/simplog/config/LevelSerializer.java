package cn.maxpixel.simplog.config;

import cn.maxpixel.simplog.Level;
import com.google.gson.*;

import java.lang.reflect.Type;

public final class LevelSerializer implements JsonSerializer<Level>, JsonDeserializer<Level> {
    private static final Level[] LEVELS = Level.getPreDefinedLevels();

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            try {
                return new Level(object.get("name").getAsString(), object.get("priority").getAsShort());
            } catch (Exception e) {
                throw new JsonParseException("Invalid level object: " + json, e);
            }
        }
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if(primitive.isNumber()) {
                short priority = json.getAsShort();// NumberFormatException shouldn't happen here
                return new Level("CUSTOM", priority);
            }
            if(primitive.isString()) {
                String name = json.getAsString();
                for (Level level : LEVELS) {
                    if(level.name.equals(name)) return level;
                }
            }
        }
        throw new JsonParseException("Invalid level object: " + json);
    }

    @Override
    public JsonElement serialize(Level src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.name.equals("CUSTOM")) {
            return new JsonPrimitive(src.priority);
        }
        for (Level level : LEVELS) {
            if(level == src) return new JsonPrimitive(src.name);
        }
        JsonObject object = new JsonObject();
        object.addProperty("name", src.name);
        object.addProperty("priority", src.priority);
        return object;
    }
}