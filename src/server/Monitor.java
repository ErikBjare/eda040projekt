package server;

import common.Mode;
import common.NetworkUtil;
import server_util.LogUtil;
import common.protocol.*;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    private final AxisWrapper hardware;
    byte[] lastFrame = new byte[131072];
    byte[] tempFrame = new byte[131072];
    boolean newPicArrived;
    boolean motionDetected;
    long timeStamp;
    int mode;
    private long lastSentFrameTime;
    private boolean isShutdown = false;

    public Monitor(AxisWrapper hardware) {
        this.hardware = hardware;
        this.mode = Mode.Idle;
    }



    public synchronized void newFrame(AxisWrapper hardware) throws ShutdownException, InterruptedException {
        if (isShutdown) throw new ShutdownException();
        LogUtil.info("entered newFrame");
        int len = 0;
        while (len <= 0){
            len = hardware.getJPEG(tempFrame, 0);
            LogUtil.info("hardware returned length: "+len);
        }
        newPicArrived = true; //A new picture available
        // TODO: How necessary is it to clone
        NetworkUtil.cloneTo(tempFrame, lastFrame);
        timeStamp = System.currentTimeMillis();
        motionDetected = hardware.motionDetected();
        notifyAll();
        LogUtil.info("about to exit newFrame");
    }

    /* Connects the monitor to the camera */
    public synchronized boolean connect() throws ShutdownException {
        if (isShutdown) throw new ShutdownException();
        notifyAll();
        // return hardware.connect();
        return true;
    }

    /* Initiates camera shutdown sequence */
    public synchronized void shutdown() {
        //TODO define whether or not the camera should be destroyed on shutdown
        isShutdown = true;
        hardware.close();
        hardware.destroy();
        notifyAll();
    }

    public synchronized void sendNext(Socket socket) throws InterruptedException, IOException, ShutdownException {
        if (isShutdown) throw new ShutdownException();
        LogUtil.info("entered sendNext");
        long frameMsInterval;
        if (mode == Mode.Idle || mode == Mode.ForceIdle) {
            frameMsInterval = 1000 / Constants.IDLE_FRAMERATE;
        } else {
            frameMsInterval = 1000 / Constants.MOVIE_FRAMERATE;
        }
        long now = System.currentTimeMillis();
        long wakeup = lastSentFrameTime + frameMsInterval;
        while (!newPicArrived || now < wakeup || isShutdown) {
            if (isShutdown) throw new ShutdownException();
            now = System.currentTimeMillis();
            long timeLeft = wakeup - now;
            LogUtil.info("sleeping for timeLeft: " + timeLeft + "("+newPicArrived+")");
            if (timeLeft > 0) wait(timeLeft);
            else if (!newPicArrived) wait();
        }
        LogUtil.info("sendNext passed getReadyToSend");
        Message mess = new NewFrame(lastFrame.length, lastFrame, timeStamp, motionDetected);
        mess.send(socket);
        LogUtil.info("sentNext sent new message");
        newPicArrived = false;
//        LogUtil.info("Sent message: " + mess.getClass().toString());
        lastSentFrameTime = System.currentTimeMillis();
        notifyAll();
    }

    public synchronized void setMode(int newMode) throws ShutdownException {
        if (isShutdown) throw new ShutdownException();
        this.mode = newMode;
    }

    public synchronized void waitForShutdown() throws InterruptedException {
        while (!isShutdown) wait();
    }
}
