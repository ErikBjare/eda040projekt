package common.protocol;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public abstract class Message {
    byte msgType;

    public Message(MsgType msgType) {
        this.msgType = (byte)msgType.ordinal();
    }

    public void send(Socket socket) throws IOException {
        socket.getOutputStream().write(msgType);
        sendPayload(socket);
    }

    protected abstract void sendPayload(Socket socket) throws IOException;
    protected abstract void decode(Socket socket) throws IOException;
}
