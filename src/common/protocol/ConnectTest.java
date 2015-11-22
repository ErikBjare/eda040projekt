package common.protocol;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class ConnectTest {


    @Test
    public void sendReceive() throws IOException {
        Message mess = new Connect(System.currentTimeMillis());
        SendReceiveTester.assertSendRecvEqual(mess, ConnectTest::constructor);
    }

    private static Message constructor(Socket socket) {
        try {
            return new Connect(socket);
        } catch (IOException e) {
            throw new RuntimeException("IO error on message reception");
        }
    }

}