import common.protocol.Message;
import common.protocol.NewFrame;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class NewFrameTest {

    @Test
    public void sendReceive() throws IOException {
        Random random = new Random();
        byte[] frame = new byte[1000];
        random.nextBytes(frame);
        Message mess = new NewFrame(1000, frame, System.currentTimeMillis());
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