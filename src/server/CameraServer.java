package server;

import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class CameraServer {
    public CameraServer(Socket socket) {
        monitor = new Monitor();
        receiver = new Receiver(monitor, socket);
        sender = new Sender(monitor, socket);
        updater = new Updater(monitor);
    }

    Monitor monitor;
    Receiver receiver;
    Sender sender;
    Updater updater;


}