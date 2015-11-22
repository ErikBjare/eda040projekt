package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class CameraServer {
    private final AxisM3006V hardware;

    Monitor monitor;
    Receiver receiver;
    Sender sender;
    Updater updater;

    public CameraServer(Socket socket) {
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
    }

    public void stop() {
        receiver.interrupt();
        sender.interrupt();
        updater.interrupt();
    }

    public void join() {
        try {
            receiver.join();
            System.out.println("Receiver thread joined");
            sender.join();
            System.out.println("Sender thread joined");
            updater.join();
            System.out.println("Updater thread joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
