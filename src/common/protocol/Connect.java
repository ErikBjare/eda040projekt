package common.protocol;

import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Message containing a new captured frame.
 */
public class Connect extends Message {
    long timestamp;

    public Connect(long timestamp) {
        super(MsgType.connect);
        this.timestamp = timestamp;
    }

    public Connect(Socket socket) throws IOException {
        super(MsgType.connect);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Connect connect = (Connect) o;

        return timestamp == connect.timestamp;

    }

    @Override
    public int hashCode() {
        return (int) (timestamp ^ (timestamp >>> 32));
    }
}
