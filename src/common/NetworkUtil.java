package common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Contains a bunch of static methods for simplifying socket code.
 */
public class NetworkUtil {
    public static byte[] toBytes(int i){
        return ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(i).array();
    }
    public static byte[] toBytes(long l){
        return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(l).array();
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
}
