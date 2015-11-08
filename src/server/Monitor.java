package server;

import client.Mode;

/**
 * Created by von on 2015-11-05.
 */
public class Monitor {
    byte[] lastFrame;
    boolean motionDetected;
    long timeStamp;
    Mode mode;

    public synchronized void newFrame(long time, boolean motion, byte[] frame){

    }

    public synchronized void sendNext(){

    }
    public synchronized void setMode(Mode newMode){

    }
}
