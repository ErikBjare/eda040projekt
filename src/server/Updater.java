package server;

//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;
//import se.lth.cs.eda040.realcamera.AxisM3006V;
import server_util.LogUtil;

import java.io.IOException;
import java.net.SocketException;

public class Updater extends Thread {
    private Monitor monitor;
    private AxisM3006V hardware;

    public Updater(Monitor monitor, AxisM3006V hardware) {
        this.monitor = monitor;
        this.hardware = hardware;
//        setName("Updater");
    }


    public void run() {
        try {
        while (!Thread.currentThread().isInterrupted()) {
            int size = AxisM3006V.IMAGE_BUFFER_SIZE;
            byte[] frame = new byte[size];
            int len = 0;

                len = hardware.getJPEG(frame, 0);

            try {
                if (len > 0) {
//                    boolean motion = false;
//                    if(System.currentTimeMillis()%50==0) motion = true;

//                    monitor.newFrame(System.currentTimeMillis(),  motion , frame);
                    monitor.newFrame(System.currentTimeMillis(),  hardware.motionDetected() , frame);

//                    System.out.println("MOtion detected:: " + hardware.motionDetected());
                }
            } catch (ShutdownException e) {
                break;
            }
        }
        }catch (Exception e){
            LogUtil.exception(e);
        }finally {

            monitor.shutdown();
        }

    }
}
