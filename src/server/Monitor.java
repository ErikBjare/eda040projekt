package server;

import common.Mode;
import common.NetworkUtil;
import server_util.LogUtil;
import common.protocol.*;
//import se.lth.cs.eda040.proxycamera.AxisM3006V;
//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.realcamera.AxisM3006V;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    private final Socket sendSocket;
    private final AxisM3006V hardware;
    byte[] lastFrame;
    boolean newPicArrived;
    boolean motionDetected;
    long timeStamp;
    int mode;
    private long lastSentFrameTime;
    private boolean isShutdown = false;

    public Monitor(Socket sendSocket, AxisM3006V hardware) {
        this.sendSocket = sendSocket;
        this.hardware = hardware;
    }

    public synchronized void newFrame(long time, boolean motion, byte[] frame) throws ShutdownException {
        if (isShutdown) throw new ShutdownException();
        //System.out.println("New frame from hardware");
        newPicArrived = true; //A new picture available
        // TODO: How necessary is it to clone
        lastFrame = NetworkUtil.clone(frame);
        timeStamp = time;
        motionDetected = motion;
        notifyAll();
    }

    /* Connects the monitor to the camera */
    public synchronized boolean connect() throws ShutdownException {
        if (isShutdown) throw new ShutdownException();
        notifyAll();
        return hardware.connect();
    }

    /* Initiates camera shutdown sequence */
    public synchronized void shutdown() {
        //TODO define whether or not the camera should be destroyed on shutdown
        isShutdown = true;
        hardware.close();
        hardware.destroy();
        notifyAll();
    }

    public synchronized void sendNext() throws InterruptedException, IOException, ShutdownException {
        if (isShutdown) throw new ShutdownException();
        getReadyToSend();
        Message mess = new NewFrame(lastFrame.length, lastFrame, timeStamp, motionDetected);
        mess.send(sendSocket);
        newPicArrived = false;
        LogUtil.info("Sent new message");
        lastSentFrameTime = System.currentTimeMillis();
    }

    public synchronized void setMode(int newMode) {
        this.mode = newMode;
    }

    private void getReadyToSend() throws InterruptedException, ShutdownException {
        if (isShutdown) throw new ShutdownException();
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
            if (timeLeft > 0) wait(timeLeft);
            else wait();
        }
        notifyAll();
    }
}
