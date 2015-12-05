package common.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.*;

public class NewFrameTest {

    @Test
    public void sendReceive() throws IOException {
        Random random = new Random();
        byte[] frame = new byte[1000];
        random.nextBytes(frame);
        Message mess = new NewFrame(1000, frame, System.currentTimeMillis(), false);
        SendReceiveTester.assertSendRecvEqual(mess, NewFrameTest::getSocketMessageFunction);
    }

    private static Message getSocketMessageFunction(Socket socket) {
        try {
            return new NewFrame(socket);
        } catch (IOException e) {
            throw new RuntimeException("IO error on message reception");
        }
    }

}