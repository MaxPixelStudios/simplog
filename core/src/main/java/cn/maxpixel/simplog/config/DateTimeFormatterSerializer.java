package cn.maxpixel.simplog.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatterSerializer implements JsonSerializer<DateTimeFormatter>, JsonDeserializer<DateTimeFormatter> {
    @Override
    public DateTimeFormatter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.getAsJsonPrimitive().isString()) throw new JsonParseException("Incompatible type(expected String)");
        String s = json.getAsString();
        switch (s) {
            case "BASIC_ISO_DATE":
                return DateTimeFormatter.BASIC_ISO_DATE;
            case "ISO_DATE":
                return DateTimeFormatter.ISO_DATE;
            case "ISO_DATE_TIME":
                return DateTimeFormatter.ISO_DATE_TIME;
            case "ISO_INSTANT_TIME":
                return DateTimeFormatter.ISO_INSTANT;
            case "ISO_LOCAL_DATE":
                return DateTimeFormatter.ISO_LOCAL_DATE;
            case "ISO_LOCAL_DATE_TIME":
                return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            case "ISO_LOCAL_TIME":
                return DateTimeFormatter.ISO_LOCAL_TIME;
            case "ISO_OFFSET_DATE":
                return DateTimeFormatter.ISO_OFFSET_DATE;
            case "ISO_OFFSET_DATE_TIME":
                return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            case "ISO_OFFSET_TIME":
                return DateTimeFormatter.ISO_OFFSET_TIME;
            case "ISO_ORDINAL_DATE":
                return DateTimeFormatter.ISO_ORDINAL_DATE;
            case "ISO_TIME":
                return DateTimeFormatter.ISO_TIME;
            case "ISO_WEEK_DATE":
                return DateTimeFormatter.ISO_WEEK_DATE;
            case "ISO_ZONED_DATE_TIME":
                return DateTimeFormatter.ISO_ZONED_DATE_TIME;
            case "RFC_1123_DATE_TIME":
                return DateTimeFormatter.RFC_1123_DATE_TIME;
            default:
                return DateTimeFormatter.ofPattern(s);
        }
    }

    @Override
    public JsonElement serialize(DateTimeFormatter src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == DateTimeFormatter.ISO_LOCAL_DATE_TIME) return new JsonPrimitive("ISO_LOCAL_DATE_TIME");
        throw new UnsupportedOperationException(); // This doesn't serialize anything other than ISO_LOCAL_DATE_TIME in theory
    }
}