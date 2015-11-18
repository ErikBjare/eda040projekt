package server;

import client.Mode;
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
    AxisM3006V camera;
    byte[] lastFrame;
    boolean newPicArrived;
    boolean motionDetected;
    long timeStamp;
    Mode mode;
    private long lastSentFrameTime;

    public Monitor(Socket sendSocket){


        this.sendSocket = sendSocket;
    }

    public synchronized void newFrame(long time, boolean motion, byte[] frame){
        newPicArrived = true; //A new picture available
    }
    /* Connects the monitor to the camera */
    public synchronized boolean connect(){
        return  camera.connect();
    }
    /* Initiates camera shutdown sequence */
    public synchronized void shutdown(){
        //TODO define whether or not the camera should be destroyed on shutdown
        camera.connect();
        camera.destroy();
    }
    public synchronized void sendNext(){
        try {
            getReadyToSend();
            Message mess = new NewFrame(lastFrame.length, lastFrame, timeStamp);
            mess.send(sendSocket);
            newPicArrived = false;

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

    }
}
