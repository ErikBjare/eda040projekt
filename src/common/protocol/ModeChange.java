package common.protocol;

import common.Mode;
import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Message containing a new captured frame.
 */
public class ModeChange extends Message {
    int newMode;
    long timestamp;

    public ModeChange(int newMode, long timestamp) {
        super(MsgType.modeChange);
        this.newMode = newMode;
        this.timestamp = timestamp;
    }

    public ModeChange(Socket socket) throws IOException {
        super(MsgType.modeChange);
        this.decode(socket);
    }

    @Override
    protected void sendPayload(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(NetworkUtil.toBytes(newMode));
        out.write(NetworkUtil.toBytes(timestamp));
    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        newMode = NetworkUtil.readInt(input);
        timestamp = NetworkUtil.readLong(input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        ModeChange that = (ModeChange) o;

        if (timestamp != that.timestamp) return false;
        return newMode == that.newMode;

    }

}
