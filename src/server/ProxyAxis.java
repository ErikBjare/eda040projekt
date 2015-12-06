package server;

/**
 * Created by von on 2015-12-06.
 */
public class ProxyAxis implements AxisWrapper {
    se.lth.cs.eda040.proxycamera.AxisM3006V proxycam;

    public ProxyAxis(String hostname, int port) {
        // Creates a proxy camera
        proxycam = new se.lth.cs.eda040.proxycamera.AxisM3006V();
        proxycam.setProxy(hostname, port);
    }

    public boolean connect() {
        return proxycam.connect();
    }

    public boolean motionDetected() {
        return proxycam.motionDetected();
    }

    public int getJPEG(byte[] target, int offset) {
        return proxycam.getJPEG(target, offset);
    }

    public void init() {
        proxycam.init();
    }

    public void close() {
        proxycam.close();
    }

    public void destroy() {
        proxycam.destroy();
    }
}
