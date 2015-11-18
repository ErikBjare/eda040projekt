package server;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
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


    public void run(){
        while (true) {
            int size = AxisM3006V.IMAGE_BUFFER_SIZE;
            byte[] frame = new byte[size];
            int len = hardware.getJPEG(frame,0);
            monitor.newFrame(System.currentTimeMillis(), hardware.motionDetected(), frame);
        }


    }
}
