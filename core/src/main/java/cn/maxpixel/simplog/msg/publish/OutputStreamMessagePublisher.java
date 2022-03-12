package cn.maxpixel.simplog.msg.publish;

import cn.maxpixel.simplog.Level;
import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class OutputStreamMessagePublisher implements MessagePublisher {
    public static final OutputStreamMessagePublisher STDOUT = new OutputStreamMessagePublisher(AnsiConsole.out(), true);
    public static final OutputStreamMessagePublisher STDERR = new OutputStreamMessagePublisher(AnsiConsole.err(), true);

    private final boolean supportColor;
    private final OutputStream outputStream;

    public OutputStreamMessagePublisher(OutputStream outputStream, boolean supportColor) {
        this(outputStream, supportColor, false);
    }

    public OutputStreamMessagePublisher(OutputStream outputStream, boolean supportColor, boolean buffered) {
        this.supportColor = supportColor;
        this.outputStream = buffered && !(Objects.requireNonNull(outputStream) instanceof BufferedOutputStream) ?
                new BufferedOutputStream(outputStream) : outputStream;
    }

    @Override
    public void publish(Level level, String msg) throws IOException {
        if (supportColor) msg = formatColor(level, msg);
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
    }
}