package client;

import client.camera.Camera;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by simon on 2015-11-08.
 */
public class SystemMonitor extends Observable {
    private ArrayList<Camera> cameraList;
    private byte[][] currentFrames;
    private Mode mode;
    private SyncMode syncMode;
    public SystemMonitor(){


    }

    public synchronized void displayFrame(int cameraId, byte[] imageCopy){

    }

    public synchronized void registerDelay(long captureTime){

    }

    public synchronized void motionDetected(){

    }

    public synchronized void setSyncMode(SyncMode mode){

    }

    public synchronized void setMode(Mode mode){

    }


    public synchronized void getNrCameras(){

    }

    public synchronized void setCameraList(Camera[] cameras){

    }


}
