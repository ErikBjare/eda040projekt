package client;

import client.camera.Camera;
import client.camera.FrameBuffer;
import common.protocol.NewFrame;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by simon on 2015-11-08.
 */
public class SystemMonitor extends Observable {
    private Camera[] cameraList;
    private FrameBuffer[] frameBuffers;
    private byte[][] currentFrames;
    private Mode mode;
    private SyncMode syncMode;


    public SystemMonitor() {
        mode = Mode.Auto;
        syncMode = SyncMode.Auto;

    }

    public synchronized void animate() {
        try {
            NewFrame next;
            next = frameBuffers[0].removeFirstFrame();
            while (next == null){
                wait();
                next = frameBuffers[0].removeFirstFrame();
            }
            System.out.println("Displaying frame, found a picture in the buffer");
            displayFrame(0, next.getFrame());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void displayFrame(int cameraId, byte[] imageCopy) {
        System.out.println("performing displayFrame");
        currentFrames[cameraId] = imageCopy;
        System.out.println(this.countObservers());
        setChanged();
        notifyObservers(this);
    }

    public synchronized void registerDelay(long captureTime) {

    }

    public synchronized void motionDetected() {

    }

    public synchronized void setSyncMode(SyncMode mode) {
        this.syncMode = mode;
    }

    public synchronized void setMode(Mode mode) {
        this.mode = mode;
    }


    public synchronized int getNrCameras() {
        return cameraList.length;

    }

    public synchronized void init(Camera[] cameras) {
        cameraList = cameras;
        currentFrames = new byte[cameraList.length][];
        this.frameBuffers = new FrameBuffer[cameraList.length];
        for (int i = 0; i < cameraList.length; i++) {
            frameBuffers[i] = cameraList[i].getBuffer();
        }
    }


    public synchronized byte[] getDisplayFrame(int i) {
        return currentFrames[i];
    }

    public synchronized void receivedFrame() {
        notifyAll();
    }
}
