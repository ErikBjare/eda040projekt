package common.protocol;

import client.Mode;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class ModeChangeTest {
    @Test
    public void sendReceive() throws IOException {
        Message mess = new ModeChange(Mode.ForceMovie,System.currentTimeMillis());
        SendReceiveTester.assertSendRecvEqual(mess, ModeChangeTest::constructor);
    }

    private static Message constructor(Socket socket) {
        try {
            return new ModeChange(socket);
        } catch (IOException e) {
            throw new RuntimeException("IO error on message reception");
        }
    }

}