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

            while (!Thread.currentThread().isInterrupted() && !cameraOffline) {
                int size = 131072;
                byte[] frame = new byte[size];
                int len;
//                synchronized (monitor) {
                    len = hardware.getJPEG(frame, 0);
 //               }
                try {
                    if (len > 0) {
//                    boolean motion = false;
//                    if(System.currentTimeMillis()%50==0) motion = true;

//                    monitor.newFrame(System.currentTimeMillis(),  motion , frame);
                        monitor.newFrame(System.currentTimeMillis(), hardware.motionDetected(), frame);

//                    System.out.println("MOtion detected:: " + hardware.motionDetected());
                    }

                } catch (ShutdownException e) {
                    e.printStackTrace();
                    LogUtil.exception(e);

                }
            }


            monitor.shutdown();
        }
    }

