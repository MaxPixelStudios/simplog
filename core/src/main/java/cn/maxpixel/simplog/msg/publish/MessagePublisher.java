package cn.maxpixel.simplog.msg.publish;

import cn.maxpixel.simplog.Level;
import cn.maxpixel.simplog.config.LogConfig;
import com.google.gson.*;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.lang.reflect.Type;

public interface MessagePublisher {
    void publish(Level level, String msg) throws IOException;

    default String formatColor(Level level, String msg) {
        return LogConfig.get().style.color.get(level).applyColor(Ansi.ansi(msg.length()).a(msg)).toString();
    }

    class Serializer implements JsonSerializer<MessagePublisher>, JsonDeserializer<MessagePublisher> {
        @Override
        public MessagePublisher deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            switch (json.getAsString()) {
                case "STDOUT":
                    return OutputStreamMessagePublisher.STDOUT;
                case "STDERR":
                    return OutputStreamMessagePublisher.STDERR;
            }
            throw new JsonParseException("Currently unsupported");
        }

        @Override
        public JsonElement serialize(MessagePublisher src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == OutputStreamMessagePublisher.STDOUT) return new JsonPrimitive("STDOUT");
            throw new UnsupportedOperationException(); // This doesn't serialize anything other than STDOUT in theory
        }
    }
}