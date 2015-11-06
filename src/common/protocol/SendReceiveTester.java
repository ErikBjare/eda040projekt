package common.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendReceiveTester extends Thread {
    /**
     * Helper method for testing that a message can be written to the network and correctly reconstructed.
     */
    public static void assertSendRecvEqual(Message original, Function<Socket, Message> constructor) throws IOException {
        // Make a socket which stores all output that is sent through it
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(output);

        original.send(socket);

        // Use the bytes sent
        InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        inputStream.read(new byte[1]); // get rid of msgtype byte, since decode() assumes that it has been consumed already
        when(socket.getInputStream()).thenReturn(inputStream);

        Message mess = constructor.apply(socket);
        assertEquals(original, mess);
    }
}
