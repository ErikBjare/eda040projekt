package common.protocol;

/**
 * The type of a message. Encoded in the first byte of each message sent.
 */
public enum MsgType {
    connect, modeChange,synchronizeClock, newFrame, shutdown
}
