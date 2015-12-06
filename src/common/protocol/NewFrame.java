package common.protocol;

import common.NetworkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Message containing a new captured frame.
 */
public class NewFrame extends Message {
    public int size;
    byte[] frame;
    public long timestamp;

    // DOES NOT COMPILE TO C
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        NewFrame newFrame = (NewFrame) o;
//
//        if (size != newFrame.size) return false;
//        if (timestamp != newFrame.timestamp) return false;
//        return Arrays.equals(frame, newFrame.frame);
//
//    }

    // ARRAYS.HASHCODE DOES NOT COMPILE TO C
//    @Override
//    public int hashCode() {
//        int result = size;
//        result = 31 * result + (frame != null ? Arrays.hashCode(frame) : 0);
//        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
//        return result;
//    }

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
        NetworkUtil.send(out, size);
        out.write(frame);
        NetworkUtil.send(out, timestamp);
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
