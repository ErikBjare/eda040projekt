package server;

import se.lth.cs.eda040.proxycamera.AxisM3006V;

import java.net.Socket;

//import se.lth.cs.eda040.fakecamera.AxisM3006V;
//import se.lth.cs.eda040.realcamera.AxisM3006V;

public class CameraServer {
    Receiver receiver;
    Sender sender;
    Updater updater;
    AxisM3006V hardware;
    Monitor monitor;

    public CameraServer(AxisM3006V hardware, Monitor monitor, Socket socket) {
        this.hardware = hardware;
        this.monitor = monitor;
        receiver = new Receiver(monitor, socket);
        sender = new Sender(monitor, socket);
        updater = new Updater(monitor, hardware);
        receiver.start();
        sender.start();
        updater.start();
    }


    public void stop() {
        receiver.interrupt();
        sender.interrupt();
        updater.interrupt();
    }

    public void join() throws InterruptedException {
        monitor.waitForShutdown();
    }
}
