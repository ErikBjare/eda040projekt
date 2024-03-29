package server;

import server_util.LogUtil;

public class Updater extends Thread {
    private final Monitor monitor;
    private AxisWrapper hardware;
    byte[] tempFrame = new byte[131072];

    public Updater(Monitor monitor, AxisWrapper hardware) {
        this.monitor = monitor;
        this.hardware = hardware;
//        setName("Updater");
    }


    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

//            LogUtil.info("Updater loop entered");
            try {
                monitor.waitForNextFrame();
            } catch (InterruptedException e) {
                LogUtil.exception(e);
            }
            int len = 0;
            while (len <= 0){
                len = hardware.getJPEG(tempFrame, 0);
//                LogUtil.info("hardware returned length: "+len);
            }
//            LogUtil.info("about to enter setCurrentFrame");
            monitor.setCurrentFrame(len , tempFrame, hardware.motionDetected(), System.currentTimeMillis());
        }
        monitor.shutdown();
    }
}

