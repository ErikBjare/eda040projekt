package client;

import client.camera.Camera;
import client.camera.ImageFrame;
import common.Constants;
import common.LogUtil;
import common.protocol.ModeChange;
import common.protocol.NewFrame;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by simon on 2015-11-08.
 */
public class SystemMonitor extends Observable {
    private List<Camera> cameraList;
    private HashMap<Integer, ImageFrame> currentFrames;
    private PriorityQueue<ImageFrame> images;
    private Mode mode;
    private SyncMode syncMode;
    private long currentlyShownFrameTimeStamp;
    private long lastModeChangeTime;

    private int motionCamera;

    public SystemMonitor() {

        mode = Mode.Idle;
        syncMode = SyncMode.Sync;
        images = new PriorityQueue<ImageFrame>(10, new Comparator<ImageFrame>() {
            @Override
            public int compare(ImageFrame o1, ImageFrame o2) {
                if (o1.getFrame().timestamp == (o2.getFrame().timestamp)) return 0;
                return (o1.getFrame().timestamp > o2.getFrame().timestamp) ? 1 : -1;
            }
        });
        motionCamera = -1;
    }
    public synchronized long getTimeStampFromLastFrame(){
        //TODO must hand thread safety
        return currentlyShownFrameTimeStamp;
    }
    public Mode getMode(){
        return mode;
    }
    public synchronized void animate() throws InterruptedException {


            //TODO Implement for more cameras aswell as synchronization

            ImageFrame next = images.peek();
            //TODO adding correct timedifference with wait


            while (true) {
                next = images.peek();
                if (next == null) {
                    wait();
                    continue;
                }

                long now = System.currentTimeMillis();
                long movieTime = now -calcSyncDelay();
                long timeLeftToDisplay = next.getFrame().timestamp - movieTime;

                if (timeLeftToDisplay <= 0) {

                    next = images.poll(); // Remove frame from priorityqueue
//                    LogUtil.info("Displaying frame from camera " + next.getCamera() + " , found a picture in the buffer");

                    checkSynchronization(next.getFrame().timestamp);
                    displayFrame(next.getCamera(), next);
                    currentlyShownFrameTimeStamp = next.getFrame().timestamp;
                } else {
                    wait(timeLeftToDisplay);
                }

            }




    }
    private long calcSyncDelay(){
        if(syncMode == SyncMode.Async || syncMode == SyncMode.ForceAsync){
            return 0;
        }else {
            return  Constants.SYNC_DELAY;
        }
    }


    public synchronized void displayFrame(int cameraId, ImageFrame imageCopy) {
//        LogUtil.info("Byteimage: " + imageCopy);
        currentFrames.put(cameraId, imageCopy);
        setChanged();
        notifyObservers(GUIUpdate.FrameUpdate);


    }
    private void checkSynchronization(long now){
//        LogUtil.info("" + (now-currentlyShownFrameTimeStamp));
//        LogUtil.info("Current delay between two shown pictures " + (now-currentlyShownFrameTimeStamp));

        if(now-currentlyShownFrameTimeStamp > Constants.TIME_WINDOW && syncMode == SyncMode.Sync){
            setSyncMode(SyncMode.Async);
        }else if(now-currentlyShownFrameTimeStamp < Constants.TIME_WINDOW && syncMode == SyncMode.Async){
            setSyncMode(SyncMode.Sync);
        }
    }

    public synchronized void addImage(ImageFrame image) {
//        LogUtil.info("Priority queue size : " + images.size());
        if(image.getFrame().motionDetected && mode == Mode.Idle){
           motionDetected(image.getCamera());
            //TODO Alert which camera recieved the motion-detection
        }
        images.add(image);
        notifyAll();

    }

    public synchronized void motionDetected(int id) {
        motionCamera = id;
        setMode(Mode.Movie);



    }
    public synchronized int getMotionCamera(){
        return motionCamera;
    }

    public synchronized void setSyncMode(SyncMode mode) {
        if(System.currentTimeMillis() - lastModeChangeTime >  Constants.RETARDEDNESS){
        this.syncMode = mode;
        lastModeChangeTime = System.currentTimeMillis();
        setChanged();
        notifyObservers(GUIUpdate.SyncModeUpdate);
        }
    }

    public synchronized void setMode(Mode mode) {
//        LogUtil.info("Setting mode to: " + mode);
        this.mode = mode;
        broadcastMode(mode);
    }

    private synchronized void broadcastMode(Mode mode) {
        for (Camera camera : cameraList) {
//            LogUtil.info("Adding mode change to mailbox: " + camera.toString());
            camera.addMessage(new ModeChange(mode, System.currentTimeMillis()));
        }
        setChanged();
        notifyObservers(GUIUpdate.ModeUpdate);
    }

    public synchronized Set<Integer> getCameraIds() {
        return cameraList.stream()
                .map(c -> c.id)
                .collect(Collectors.toSet());
    }

    public synchronized int getNrCameras() {
        return cameraList.size();

    }

    public synchronized void init(List<Camera> cameras) {
        cameraList = cameras;
        currentFrames = new HashMap<Integer, ImageFrame>(8);
    }


    public synchronized ImageFrame getDisplayFrame(int i) {
        return currentFrames.get(i);
    }

    public SyncMode getSyncMode() {
        return syncMode;
    }
}
