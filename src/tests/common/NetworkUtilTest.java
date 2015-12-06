package common;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by von on 2015-12-05.
 */
public class NetworkUtilTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }
    public void testSend() throws Exception {

    }

    public void testSend1() throws Exception {

    }

    public void testToLong() throws Exception {

    }
    @Test
    public void testLongMax() throws Exception {
        sendRecv(Long.MAX_VALUE);
    }
    @Test
    public void testLongBig() throws Exception {
        sendRecv(190000L);
    }
    @Test
    public void testLong() throws Exception {
        sendRecv(1L);
    }
    @Test
    public void testIntMax() throws Exception {
        sendRecv(Integer.MAX_VALUE);
    }
    @Test
    public void testIntBig() throws Exception {
        sendRecv(190000);
    }
    @Test
    public void testInt() throws Exception {
        sendRecv(1);
    }
    public void sendRecv(long i) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Socket s = mock(Socket.class);
        when(s.getOutputStream()).thenReturn(output);
        NetworkUtil.send(output, i);
        assertEquals(i, NetworkUtil.toLong(output.toByteArray()));
    }

    public void sendRecv(int i) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Socket s = mock(Socket.class);
        when(s.getOutputStream()).thenReturn(output);
        NetworkUtil.send(output, i);
        assertEquals(i, NetworkUtil.toInt(output.toByteArray()));
    }
}