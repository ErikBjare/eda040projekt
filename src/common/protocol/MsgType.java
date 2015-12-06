package common.protocol;

/**
 * The type of a message. Encoded in the first byte of each message sent.
 */
public class MsgType {
    public static final byte CONNECT = 0;
    public static final byte MODE_CHANGE = 1;
    public static final byte SYNCHRONIZE_CLOCK = 3;
    public static final byte NEW_FRAME = 4;
    public static final byte SHUTDOWN = 5;
}
