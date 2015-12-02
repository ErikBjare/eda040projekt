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
    Mode newMode;
    long timestamp;

    public ModeChange(Mode newMode, long timestamp) {
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
        out.write(NetworkUtil.toBytes(newMode.ordinal()));
        out.write(NetworkUtil.toBytes(timestamp));
    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        newMode = Mode.fromInteger(NetworkUtil.readInt(input));
        timestamp = NetworkUtil.readLong(input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModeChange that = (ModeChange) o;

        if (timestamp != that.timestamp) return false;
        return newMode == that.newMode;

    }

    @Override
    public int hashCode() {
        int result = newMode != null ? newMode.hashCode() : 0;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
