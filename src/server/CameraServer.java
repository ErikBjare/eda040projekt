package server;

import server_util.LogUtil;
//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;
//import se.lth.cs.eda040.realcamera.AxisM3006V;

import java.net.Socket;

public class CameraServer {
    Receiver receiver;
    Sender sender;
    Updater updater;
    AxisM3006V hardware;

    public CameraServer(AxisM3006V hardware, Monitor monitor, Socket socket) {
        this.hardware = hardware;
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
        finally {
        hardware.close();
        hardware.destroy();
        }
    }


}
