package server;

public interface AxisWrapper {
    public boolean connect();
    public boolean motionDetected();
    public int getJPEG(byte[] target, int offset);
    public void init();
    public void close();
    public void destroy();
}
