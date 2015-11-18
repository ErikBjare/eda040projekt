package server;

import client.Mode;
import client.SystemMonitor;
import common.protocol.Constants;
import common.protocol.Message;
import common.protocol.NewFrame;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

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
    Mode mode;
    private long lastSentFrameTime;

    public Monitor(Socket sendSocket, AxisM3006V hardware){
        this.sendSocket = sendSocket;
        this.hardware = hardware;
    }

    public synchronized void newFrame(long time, boolean motion, byte[] frame){
        //System.out.println("New frame from hardware");
        newPicArrived = true; //A new picture available
        lastFrame = frame.clone();
        timeStamp = time;
        motionDetected = motion;
    }
    /* Connects the monitor to the camera */
    public synchronized boolean connect(){
        return  hardware.connect();
    }
    /* Initiates camera shutdown sequence */
    public synchronized void shutdown(){
        //TODO define whether or not the camera should be destroyed on shutdown
        hardware.close();
        hardware.destroy();
    }
    public synchronized void sendNext(){
        try {
            System.out.print("Getting ready to send");
            getReadyToSend();
            System.out.println("Creating message");
            Message mess = new NewFrame(lastFrame.length, lastFrame, timeStamp);
            System.out.println("Sending message");
            mess.send(sendSocket);
            newPicArrived = false;
            System.out.println("Finished sending");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        lastSentFrameTime = System.currentTimeMillis();


    }
    public synchronized void setMode(Mode newMode){

    }
    private void getReadyToSend() throws InterruptedException {
        long frameMsInterval;
        if (mode == Mode.Idle || mode == Mode.ForceIdle) {
            frameMsInterval = 1000 / Constants.IDLE_FRAMERATE;
        } else {
            frameMsInterval = 1000 / Constants.MOVIE_FRAMERATE;
        }
        long timeDiff = System.currentTimeMillis() - lastSentFrameTime;

        if (timeDiff < frameMsInterval) {
            wait(frameMsInterval - timeDiff);
        }

        long now = System.currentTimeMillis();
        long wakeup = lastSentFrameTime + frameMsInterval;
        while (!newPicArrived || now < wakeup) {
            now = System.currentTimeMillis();
            long timeLeft = wakeup - now;
            System.out.print(".");
            if (timeLeft > 0) wait(timeLeft);
            else wait();
        }
        System.out.println("");

    }
}