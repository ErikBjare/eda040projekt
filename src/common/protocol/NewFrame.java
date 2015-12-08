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
    public boolean motionDetected;

    public NewFrame(int size, byte[] frame, long timestamp, boolean motionDetected) {
        // TODO: Hardcoded MsgType.newFrame for C compilation debugging.
        super((byte)4);
        fill(size, frame, timestamp, motionDetected);
    }

    public void fill(int size, byte[] frame, long timestamp, boolean motionDetected) {
        this.size = size;
        this.frame = frame;
        this.timestamp = timestamp;
        this.motionDetected = motionDetected;
    }

    public NewFrame() {
        // TODO: Hardcoded MsgType.newFrame for C compilation debugging.
        super((byte) 4);
    }

    public NewFrame(Socket socket) throws IOException {
        // TODO: Hardcoded MsgType.newFrame for C compilation debugging.
        super((byte)4);
        this.decode(socket);
    }


    public NewFrame(int size, byte[] frame, long timestamp) {
        super((byte) 4);
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
    protected void sendPayload(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        NetworkUtil.send(out, size);
        NetworkUtil.send(out, frame, size);
        NetworkUtil.writeBool(out, motionDetected);
        NetworkUtil.send(out, timestamp);
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

    public byte[] getFrameAsBytes() {
        return frame;
    }

////     ARRAYS.HASHCODE DOES NOT COMPILE TO C
//    @Override
//    public int hashCode() {
//        int result = size;
//        result = 31 * result + (frame != null ? Arrays.hashCode(frame) : 0);
//        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
//        result = 31 * result + (motionDetected ? 1 : 0);
//        return result;
//    }
//
//    //     DOES NOT COMPILE TO C
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        NewFrame newFrame = (NewFrame) o;
//
//        if (size != newFrame.size) return false;
//        if (motionDetected != newFrame.motionDetected) return false;
//        if (timestamp != newFrame.timestamp) return false;
//        return Arrays.equals(frame, newFrame.frame);
//
//    }
//
////    DOES NOT COMPILE TO C: Likely due to use of an array method
//    @Override
//    public String toString() {
//        return "NewFrame{" +
//                "size=" + size +
//                ", frame=" + frame.toString() +
//                ", timestamp=" + timestamp +
//                '}';
//    }

}
