package server;

import client.Mode;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    AxisM3006V camera;
    byte[] lastFrame;
    boolean motionDetected;
    long timeStamp;
    Mode mode;


    public synchronized void newFrame(long time, boolean motion, byte[] frame){

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

    }
    public synchronized void setMode(Mode newMode){

    }
}
