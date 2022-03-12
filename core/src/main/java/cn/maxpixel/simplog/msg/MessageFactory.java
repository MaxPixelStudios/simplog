package cn.maxpixel.simplog.msg;

public final class MessageFactory {
    private static final ThreadLocal<Message> MSG_HOLDER = ThreadLocal.withInitial(Message::new);

    private MessageFactory() {}

    public static Message getMessage() {
        Message msg = MSG_HOLDER.get();
        if(!msg.isReady()) msg = new Message();// Why this isn't ready?
        return msg;
    }
}