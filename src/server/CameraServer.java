package server;

import java.net.Socket;

public class CameraServer {
    Receiver receiver;
    Sender sender;
    AxisWrapper hardware;
    Monitor monitor;

    public CameraServer(AxisWrapper hardware, Monitor monitor, Socket socket) {
        this.hardware = hardware;
        this.monitor = monitor;
        receiver = new Receiver(monitor, socket);
        sender = new Sender(monitor, socket);
        receiver.start();
        sender.start();
    }


    public void stop() {
        receiver.interrupt();
        sender.interrupt();
    }

    public void join() throws InterruptedException {
         monitor.waitForShutdown();
    }
}
