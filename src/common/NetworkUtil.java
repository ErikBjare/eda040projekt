package common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Contains a bunch of static methods for simplifying socket code.
 */
public class NetworkUtil {
    public static final int INTEGER_SIZE = 4*8;
    public static final int BYTE_SIZE = 1*8;
    public static final int LONG_SIZE = 8*8;

    public static byte[] toBytes(int i){
        return ByteBuffer.allocate(INTEGER_SIZE/ BYTE_SIZE).putInt(i).array();
    }
    public static byte[] toBytes(long l){
        return ByteBuffer.allocate(LONG_SIZE / BYTE_SIZE).putLong(l).array();
    }
    public static void readAll(InputStream input, byte[] buffer) throws IOException {
        int  read = 0;
        int result = 0;

        while (read<buffer.length && result != -1) {
            result = input.read(buffer,read,buffer.length - read);
            if (result!= -1) read = read+result;
        }
    }
    public static long toLong(byte[] bytes){
        return ByteBuffer.wrap(bytes).getLong();
    }
    public static int toInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }
    public static long readLong(InputStream input) throws IOException {
        byte[] buffer = new byte[8];
        NetworkUtil.readAll(input, buffer);
        return toLong(buffer);
    }
    public static int readInt(InputStream input) throws IOException {
        byte[] buffer = new byte[4];
        NetworkUtil.readAll(input, buffer);
        return toInt(buffer);
    }
    public static byte readByte(InputStream input) throws IOException {
        int response = input.read();
        if (response == -1) throw new RuntimeException("socket end");
        return (byte)response;
    }
}
