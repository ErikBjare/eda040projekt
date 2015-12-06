package server;

import java.net.Socket;

public class CameraServer {
    Receiver receiver;
    Sender sender;
    Updater updater;
    AxisWrapper hardware;
    Monitor monitor;

    public CameraServer(AxisWrapper hardware, Monitor monitor, Socket socket) {
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
