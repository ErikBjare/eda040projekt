package common.protocol;

import client.Mode;
import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

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
        out.write(newMode.ordinal());
        out.write(NetworkUtil.toBytes(timestamp));
    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        newMode = Mode.fromInteger(NetworkUtil.readInt(input));
        timestamp = NetworkUtil.readLong(input);
    }
}
