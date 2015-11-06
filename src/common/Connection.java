package common;

import java.net.Socket;

/**
 * Wraps a socket, hiding the implementation details of the communication protocol.
 *
 * Makes extending the application to use UDP simpler.
 */
public class Connection {
    /**
     * Create a new connection to the given host/port
     * @param host hostname of the camera
     * @param port port number of the camera
     */
    public Connection(String host, int port){

    }
    /**
     * Create a new connection using an existing socket.
     */
    public Connection(Socket socket){

    }
}
