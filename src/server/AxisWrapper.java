package server;

public class AxisWrapper {
    se.lth.cs.eda040.proxycamera.AxisM3006V proxycam;
    se.lth.cs.eda040.realcamera.AxisM3006V realcam;

    public AxisWrapper(String hostname, int port) {
        // Creates a proxy camera
        proxycam = new se.lth.cs.eda040.proxycamera.AxisM3006V();
        proxycam.setProxy(hostname, port);
    }

    public AxisWrapper() {
        // Creates a real camera
        realcam = new se.lth.cs.eda040.realcamera.AxisM3006V();
    }

    private boolean isReal() {
        return realcam != null;
    }

    public boolean connect() {
        if(isReal()) {
            return realcam.connect();
        } else {
            return proxycam.connect();
        }
    }

    public boolean motionDetected() {
        if(isReal()) {
            return realcam.motionDetected();
        } else {
            return proxycam.motionDetected();
        }
    }

    public int getJPEG(byte[] target, int offset) {
        if(isReal()) {
            return realcam.getJPEG(target, offset);
        } else {
            return proxycam.getJPEG(target, offset);
        }
    }

    public void init() {
        if(isReal()) {
            realcam.init();
        } else {
            proxycam.init();
        }
    }
    public void close() {
        if(isReal()) {
            realcam.close();
        } else {
            proxycam.close();
        }
    }
    public void destroy() {
        if(isReal()) {
            realcam.destroy();
        } else {
            proxycam.destroy();
        }
    }
}
