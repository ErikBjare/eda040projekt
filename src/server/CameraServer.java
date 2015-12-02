package server;

import common.LogUtil;
//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;

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
        hardware.setProxy("argus-2.student.lth.se", 9191);
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
            LogUtil.info("Receiver thread joined");
            sender.join();
            LogUtil.info("Sender thread joined");
            updater.join();
            LogUtil.info("Updater thread joined");
        } catch (InterruptedException e) {
            LogUtil.exception(e);
        }
    }


}
