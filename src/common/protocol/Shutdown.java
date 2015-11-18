package common.protocol;

import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Message containing a new captured frame.
 */
public class Shutdown extends Message {
    long timestamp;

    public Shutdown(long timestamp) {
        super(MsgType.shutdown);
        this.timestamp = timestamp;
    }

    public Shutdown(Socket socket) throws IOException {
        super(MsgType.shutdown);
        this.decode(socket);
    }

    @Override
    protected void sendPayload(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(NetworkUtil.toBytes(timestamp));
    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        timestamp = NetworkUtil.readLong(input);
    }
}