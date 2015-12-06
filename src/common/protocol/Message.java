package common.protocol;

import java.io.IOException;
import java.net.Socket;

/**
 * Represents a single message sent between a client and server. Capable of writing itself to and repopulating its fields from a socket.
 */
public abstract class Message {
    byte msgType;

    public Message(byte msgType) {
        this.msgType = msgType;
    }

    /**
     * Sends the message's contents over the socket.
     * @param socket
     * @throws IOException
     */
    public void send(Socket socket) throws IOException {
        socket.getOutputStream().write(msgType);
        sendPayload(socket);
    }

    /**
     * Sends the message type specific contents of the message.
     * @param socket
     * @throws IOException
     */
    protected abstract void sendPayload(Socket socket) throws IOException;

    /**
     * Given a socket, populates the fields of the current message object with the values read from the socket.
     *
     * To be used *after* a MsgType has been received and used to instantiate the correct type of object.
     * @param socket
     * @throws IOException
     */
    protected abstract void decode(Socket socket) throws IOException;
}
