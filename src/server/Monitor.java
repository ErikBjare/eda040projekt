package server;

import common.Mode;
import common.NetworkUtil;
import common.protocol.NewFrame;
import server_util.LogUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    byte[] lastFrame = new byte[131072];
    private int lastLength;
    boolean newPicArrived;
    boolean motionDetected;
    long timeStamp;
    int mode;
    private long lastSentFrameTime;
    private boolean isShutdown = false;
    NewFrame mess;
    long frameMsInterval;

    public Monitor() {
        this.mode = Mode.Idle;
        mess = new NewFrame();
        recalcFrameRate();
    }

    public synchronized int cloneFrame(byte[] target){
        NetworkUtil.cloneTo(lastFrame, target);
        return lastFrame.length;
    }

    public synchronized void waitForNextFrame() throws InterruptedException {
        long now = System.currentTimeMillis();
        long wakeup = lastSentFrameTime + frameMsInterval;
        while (now < wakeup) {
            long timeLeft = wakeup-now;
//            LogUtil.info("updater sleeping for timeLeft: " + timeLeft + "("+newPicArrived+")");
            wait(timeLeft);
            recalcFrameRate();
            now = System.currentTimeMillis();
            wakeup = lastSentFrameTime + frameMsInterval;
        }
    }

    public void recalcFrameRate() {
        if (mode == Mode.Idle || mode == Mode.ForceIdle) {
            frameMsInterval = 1000 / common.Constants.IDLE_FRAMERATE;
        } else {
            frameMsInterval = 1000 / common.Constants.MOVIE_FRAMERATE;
        }
    }

    public synchronized void setCurrentFrame(int length, byte[] tmp, boolean motion, long timeStamp) {
//        LogUtil.info("entered setCurrentFrame");
        newPicArrived = true; //A new picture available
        NetworkUtil.cloneTo(tmp, lastFrame, 0,length);
        this.lastLength = length;
        this.timeStamp = timeStamp;
        this.motionDetected = motion;
        notifyAll();
//        LogUtil.info("about to exit setCurrentFrame");
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
        if (isShutdown) return;
        isShutdown = true;
        //TODO define whether or not the camera should be destroyed on shutdown
//        hardware.close();
//        hardware.destroy();
        notifyAll();
    }

    public synchronized void sendNext(Socket socket) throws InterruptedException, IOException, ShutdownException {
        if (isShutdown) throw new ShutdownException();
//        LogUtil.info("entered sendNext");
        while(!newPicArrived) {
//            LogUtil.info("sendNext sleeping until woken");
            wait(50);
//            LogUtil.info("sendNext woken!");
        }
        mess.fill(lastLength, lastFrame, timeStamp, motionDetected);
        mess.send(socket);
//        LogUtil.info("sentNext sent new message");
        newPicArrived = false;
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
