package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class Main {
    public static void main(String[] args) {
        ServerSocket sock = null;
        try {
            sock = new ServerSocket(5656);
        try {
            Socket client = sock.accept();
            new CameraServer(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
