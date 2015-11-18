package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class CameraServer {
    private final AxisM3006V hardware;

    public CameraServer(Socket socket) throws InterruptedException {
        this.hardware = new AxisM3006V();
        hardware.init();
        hardware.connect();
        monitor = new Monitor(socket,hardware);
        receiver = new Receiver(monitor, socket);
        sender = new Sender(monitor);
        updater = new Updater(monitor,hardware);
        receiver.start();
        sender.start();
        updater.start();

        receiver.join();
        sender.join();
        updater.join();
    }

    Monitor monitor;
    Receiver receiver;
    Sender sender;
    Updater updater;


}
