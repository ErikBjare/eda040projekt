package common.protocol;

/**
 * The type of a message. Encoded in the first byte of each message sent.
 */
public class MsgType {
    public static final byte connect = 0;
    public static final byte modeChange = 1;
    public static final byte synchronizeClock = 2;
    public static final byte newFrame = 3;
    public static final byte shutdown = 4;
}
