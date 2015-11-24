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
            while (true) {
                System.out.println("Started listening for new client");
                Socket client = sock.accept();
                System.out.println("Accepted connection");
                CameraServer cameraServer = new CameraServer(client);
                cameraServer.join();
                System.out.println("Finished with client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
