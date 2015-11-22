package client;

import client.camera.Camera;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by simon on 2015-11-08.
 */
public class SystemMonitor extends Observable {
    private Camera[] cameraList;
    private byte[][] currentFrames;
    private Mode mode;
    private SyncMode syncMode;


    public SystemMonitor(){
        mode = Mode.Auto;
        syncMode = SyncMode.Auto;

    }


    public synchronized void displayFrame(int cameraId, byte[] imageCopy){
        currentFrames[cameraId] = imageCopy;
        notifyObservers();
    }

    public synchronized void registerDelay(long captureTime){

    }

    public synchronized void motionDetected(){

    }

    public synchronized void setSyncMode(SyncMode mode){
        this.syncMode = mode;
    }

    public synchronized void setMode(Mode mode){
        this.mode = mode;
    }


    public synchronized int getNrCameras(){
        return cameraList.length;

    }

    public synchronized void init(Camera[] cameras){
        cameraList = cameras;
        currentFrames = new byte[cameraList.length][];
    }


    public synchronized byte[] getDisplayFrame(int i) {
        return currentFrames[i];
    }
}
