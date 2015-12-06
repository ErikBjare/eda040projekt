package server;

import se.lth.cs.eda040.realcamera.AxisM3006V;

/**
 * Created by von on 2015-12-06.
 */
public class RealAxis implements AxisWrapper{
    se.lth.cs.eda040.realcamera.AxisM3006V realcam;

    public RealAxis() {
        realcam = new AxisM3006V();
    }

    public boolean connect() {
        return realcam.connect();
    }

    public boolean motionDetected() {
        return realcam.motionDetected();
    }

    public int getJPEG(byte[] target, int offset) {
        return realcam.getJPEG(target, offset);
    }

    public void init() {
        realcam.init();
    }

    public void close() {
        realcam.close();
    }

    public void destroy() {
        realcam.destroy();
    }
}
