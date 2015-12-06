package server;

//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;
//import se.lth.cs.eda040.realcamera.AxisM3006V;
import server_util.LogUtil;

public class Updater extends Thread {
    private final Monitor monitor;
    private AxisM3006V hardware;

    public Updater(Monitor monitor, AxisM3006V hardware) {
        this.monitor = monitor;
        this.hardware = hardware;
//        setName("Updater");
    }


    public void run() {
        boolean cameraOffline = false;
        try {
            while (!Thread.currentThread().isInterrupted() && !cameraOffline) {
                int size = AxisM3006V.IMAGE_BUFFER_SIZE;
                byte[] frame = new byte[size];
                int len;
                synchronized (monitor) {
                    len = hardware.getJPEG(frame, 0);
                }
                try {
                    if (len > 0) {
//                    boolean motion = false;
//                    if(System.currentTimeMillis()%50==0) motion = true;

//                    monitor.newFrame(System.currentTimeMillis(),  motion , frame);
                        monitor.newFrame(System.currentTimeMillis(), hardware.motionDetected(), frame);

//                    System.out.println("MOtion detected:: " + hardware.motionDetected());
                    } else {
                        LogUtil.error("Camera disconnected (received image of len 0, might mean something else?)");
                        cameraOffline = true;
                    }
                } catch (ShutdownException e) {
                    e.printStackTrace();
                    LogUtil.exception(e);

                }
            }
        }finally{

            monitor.shutdown();
        }
    }
}
