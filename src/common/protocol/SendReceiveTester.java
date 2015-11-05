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

/**
 * Created by von on 2015-11-05.
 */
public class SendReceiveTester extends Thread {
    public static void assertSendRecvEqual(Message original, Function<Socket, Message> constructor) throws IOException {
        // Make a socket which stores all output that is sent through it
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(output);

        original.send(socket);

        // Use the bytes sent
        InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        inputStream.read(new byte[1]); // get rid of msgtype byte
        when(socket.getInputStream()).thenReturn(inputStream);

        Message mess = constructor.apply(socket);
        assertEquals(original, mess);
    }
}
