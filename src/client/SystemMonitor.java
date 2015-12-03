package client;

import client.camera.Camera;
import client.camera.FrameBuffer;
import client.camera.ImageFrame;
import common.Constants;
import common.LogUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by simon on 2015-11-08.
 */
public class SystemMonitor extends Observable {
    private Camera[] cameraList;
    private FrameBuffer[] frameBuffers;
    private HashMap<Integer, byte[]> currentFrames;
    private PriorityQueue<ImageFrame> images;
    private Mode mode;
    private SyncMode syncMode;
    private long currentlyShownFrameTimeStamp;
    private long timeOfUpdate;
    private long diff;

    public SystemMonitor() {

        mode = Mode.Auto;
        syncMode = SyncMode.Auto;
        images = new PriorityQueue<ImageFrame>(10, new Comparator<ImageFrame>() {
            @Override
            public int compare(ImageFrame o1, ImageFrame o2) {
                if (o1.getFrame().timestamp == (o2.getFrame().timestamp)) return 0;
                return (o1.getFrame().timestamp > o2.getFrame().timestamp) ? 1 : -1;
            }
        });

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

//                if(timeLeftToDisplay > Constants.TIME_WINDOW && syncMode == SyncMode.Sync){
//                    syncMode = SyncMode.Async;
//                }
                if (timeLeftToDisplay <= 0) {

                    next = images.poll(); // Remove frame from priorityqueue
                    LogUtil.info("Displaying frame from camera " + next.getCamera() + " , found a picture in the buffer");

                    checkSynchronization(now);

                    displayFrame(next.getCamera(), next.getFrame().getFrameAsBytes());
                    currentlyShownFrameTimeStamp = next.getFrame().timestamp;
                } else {
                    wait(timeLeftToDisplay);
                }

            }
//            while()
        



    }
    private long calcSyncDelay(){
        if(syncMode == SyncMode.Async || syncMode == SyncMode.ForceAsync){
            return 0;
        }else {
            return  Constants.SYNC_DELAY;
        }
    }

    private synchronized long timeSinceLastUpdate() {
        return System.currentTimeMillis() - timeOfUpdate;
    }

    public synchronized void displayFrame(int cameraId, byte[] imageCopy) {
//        LogUtil.info("Byteimage: " + imageCopy);
        currentFrames.put(cameraId, imageCopy);
        setChanged();
        notifyObservers(this);


    }
    private void checkSynchronization(long now){

        if(now-currentlyShownFrameTimeStamp > Constants.TIME_WINDOW && syncMode == SyncMode.Sync){
            syncMode = SyncMode.Async;
        }else if(now-currentlyShownFrameTimeStamp < Constants.TIME_WINDOW && syncMode == SyncMode.Async){
            syncMode = SyncMode.Sync;
        }
    }

    public synchronized void addImage(ImageFrame image) {
        images.add(image);
        notifyAll();

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

    public synchronized Set<Integer> getCameraIds() {
        return Arrays.stream(cameraList)
                .map(c -> c.id)
                .collect(Collectors.toSet());
    }

    public synchronized int getNrCameras() {
        return cameraList.length;

    }

    public synchronized void init(Camera[] cameras) {
        cameraList = cameras;
        currentFrames = new HashMap<Integer, byte[]>(8);
    }


    public synchronized byte[] getDisplayFrame(int i) {
        return currentFrames.get(i);
    }

}
