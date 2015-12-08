package server;

import common.Mode;
import common.NetworkUtil;
import common.protocol.Constants;
import common.protocol.NewFrame;
import server_util.LogUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    byte[] lastFrame = new byte[131072];
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
        recalcFrameRate();
        long now = System.currentTimeMillis();
        long wakeup = lastSentFrameTime + frameMsInterval;
        while (now < wakeup) {
            long timeLeft = wakeup-now;
//            LogUtil.info("sleeping for timeLeft: " + timeLeft + "("+newPicArrived+")");
            wait(timeLeft);
            now = System.currentTimeMillis();
        }
    }

    public void recalcFrameRate() {
        if (mode == Mode.Idle || mode == Mode.ForceIdle) {
            frameMsInterval = 1000 / Constants.IDLE_FRAMERATE;
        } else {
            frameMsInterval = 1000 / Constants.MOVIE_FRAMERATE;
        }
    }

    public synchronized void setCurrentFrame(byte[] tmp, boolean motion, long timeStamp) {
        newPicArrived = true; //A new picture available
        NetworkUtil.cloneTo(tmp, lastFrame);
        this.timeStamp = timeStamp;
        motionDetected = motion;
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
            wait();
//            LogUtil.info("sendNext woken!");
        }
        mess.fill(lastFrame.length, lastFrame, timeStamp, motionDetected);
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
