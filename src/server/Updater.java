package server;

import server_util.LogUtil;

public class Updater extends Thread {
    private final Monitor monitor;
    private AxisWrapper hardware;

    public Updater(Monitor monitor, AxisWrapper hardware) {
        this.monitor = monitor;
        this.hardware = hardware;
//        setName("Updater");
    }


    public void run() {
        boolean cameraOffline = false;
        try {
            while (!Thread.currentThread().isInterrupted() && !cameraOffline) {
                monitor.newFrame(hardware);
            }
        } catch (ShutdownException e) {
            LogUtil.exception(e);

        } catch (InterruptedException e) {
            LogUtil.exception(e);
        }


        monitor.shutdown();
    }
}

