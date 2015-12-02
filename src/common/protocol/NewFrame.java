package common.protocol;

import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Message containing a new captured frame.
 */
public class NewFrame extends Message {
    public int size;
    byte[] frame;
    public long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        NewFrame newFrame = (NewFrame) o;

        if (size != newFrame.size) return false;
        if (timestamp != newFrame.timestamp) return false;
        return Arrays.equals(frame, newFrame.frame);

    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (frame != null ? frame.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    public NewFrame(int size, byte[] frame, long timestamp) {
        super(MsgType.newFrame);
        this.size = size;
        this.frame = frame;
        this.timestamp = timestamp;
    }

    public byte[] getFrame() {
        return frame;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "NewFrame{" +
                "size=" + size +
                ", frame=" + frame.toString() +
                ", timestamp=" + timestamp +
                '}';
    }

    public NewFrame(Socket socket) throws IOException {
        super(MsgType.newFrame);
        this.decode(socket);
    }

    @Override
    protected void sendPayload(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(NetworkUtil.toBytes(size));
        out.write(frame);
        out.write(NetworkUtil.toBytes(timestamp));
    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        size = NetworkUtil.readInt(input);
        frame = new byte[size];
        NetworkUtil.readAll(input, frame);
        timestamp = NetworkUtil.readLong(input);
    }
}
