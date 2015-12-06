package common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains a bunch of static methods for simplifying socket code.
 */
public class NetworkUtil {
    public static final int INTEGER_SIZE = 4;
    public static final int LONG_SIZE = 8;
    public static final int BITS_IN_BYTE = 8;

    public static void send(OutputStream out, int n) throws IOException {
        byte[] result = new byte[4];
        for (int i = INTEGER_SIZE-1; i >= 0; i--) {
            result[i] = (byte)(n & 0xFF);
            n >>= BITS_IN_BYTE;
        }
        out.write(result);
    }
    public static void send(OutputStream out, long l) throws IOException {
        byte[] result = new byte[LONG_SIZE];
        for (int i = LONG_SIZE-1; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= BITS_IN_BYTE;
        }
        out.write(result);
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
        long l = 0;
        for (int i = 0; i < LONG_SIZE; i++)
        {
             l <<= BITS_IN_BYTE;
             l |= (bytes[i] & 0xFF);
        }
        return l;
    }
    public static int toInt(byte[] bytes){
        int l = 0;
        for (int i = 0; i < INTEGER_SIZE; i++)
        {
            l <<= BITS_IN_BYTE;
            l |= bytes[i] & 0xFF;
        }
        return l;
    }
    public static long readLong(InputStream input) throws IOException {
        byte[] buffer = new byte[LONG_SIZE];
        NetworkUtil.readAll(input, buffer);
        return toLong(buffer);
    }
    public static int readInt(InputStream input) throws IOException {
        byte[] buffer = new byte[INTEGER_SIZE];
        NetworkUtil.readAll(input, buffer);
        return toInt(buffer);
    }
    public static byte readByte(InputStream input) throws IOException {
        int response = input.read();
        if (response == -1) throw new RuntimeException("socket end");
        return (byte)response;
    }

    public static byte[] clone(byte[] arr){
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }
}
