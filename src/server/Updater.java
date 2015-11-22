package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

/**
 * Created by von on 2015-11-05.
 */
public class Updater extends Thread {
    private Monitor monitor;
    private AxisM3006V hardware;

    public Updater(Monitor monitor, AxisM3006V hardware) {
        this.monitor = monitor;
        this.hardware = hardware;
    }


    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int size = AxisM3006V.IMAGE_BUFFER_SIZE;
            byte[] frame = new byte[size];
            int len = 0;
            try {
                len = hardware.getJPEG(frame, 0);
            } catch (Error e) {
                if (e.getMessage().startsWith("Interrupted")) {
                    // A bullshit error that is really an InterruptedException from a Thread.sleep()
                    break;
                }
            }
            if(len > 0 ){
                monitor.newFrame(System.currentTimeMillis(), hardware.motionDetected(), frame);

            }
        }


    }
}
