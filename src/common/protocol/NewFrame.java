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
    public boolean motionDetected;

    public NewFrame(int size, byte[] frame, long timestamp, boolean motionDetected) {
        super(MsgType.newFrame);
        this.size = size;
        this.frame = frame;
        this.timestamp = timestamp;
        this.motionDetected = motionDetected;
    }

    public NewFrame(Socket socket) throws IOException {
        super(MsgType.newFrame);
        this.decode(socket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewFrame newFrame = (NewFrame) o;

        if (motionDetected != newFrame.motionDetected) return false;
        if (size != newFrame.size) return false;
        if (timestamp != newFrame.timestamp) return false;
        if (!Arrays.equals(frame, newFrame.frame)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "NewFrame{" +
                "size=" + size +
                ", frame=" + Arrays.toString(frame) +
                ", timestamp=" + timestamp +
                ", motionDetected=" + motionDetected +
                '}';
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (frame != null ? Arrays.hashCode(frame) : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (motionDetected ? 1 : 0);
        return result;
    }

    public byte[] getFrameAsBytes() {
        return frame;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSize() {
        return size;
    }



    @Override
    protected void sendPayload(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(NetworkUtil.toBytes(size));
        out.write(frame);
        NetworkUtil.writeBool(out, motionDetected);
        out.write(NetworkUtil.toBytes(timestamp));

    }

    @Override
    protected void decode(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        size = NetworkUtil.readInt(input);
        frame = new byte[size];
        NetworkUtil.readAll(input, frame);
        motionDetected = NetworkUtil.readBool(input);
        timestamp = NetworkUtil.readLong(input);
    }
}
