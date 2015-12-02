package client;

import client.camera.Camera;
import client.camera.FrameBuffer;
import client.camera.ImageFrame;
import common.LogUtil;

import java.util.*;

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

    public synchronized void animate() {
        try {
            //TODO Implement for more cameras aswell as synchronization

           ImageFrame next = images.poll();
            //TODO adding correct timedifference with wait
            while (next == null){
                wait();
                next = images.poll();

            }
            displayFrame(next.getCamera(), next.getFrame().getFrameAsBytes());
            long timeDiff = next.getFrame().timestamp -currentlyShownFrameTimeStamp;
            long before = System.currentTimeMillis();
            if(timeSinceLastFrame() < timeDiff){
                System.out.println(timeDiff - timeSinceLastFrame()-diff);
                System.out.println(diff);
                wait(timeDiff - timeSinceLastFrame()-diff);
            }
            long diff = System.currentTimeMillis() - before;
            diff = diff - (timeDiff-timeSinceLastFrame());


            timeOfUpdate = System.currentTimeMillis();


            LogUtil.info("Displaying frame from camera " + next.getCamera() + " , found a picture in the buffer");

            currentlyShownFrameTimeStamp = next.getFrame().timestamp;




        } catch (InterruptedException e) {
            LogUtil.exception(e);
        }
    }
    private synchronized long timeSinceLastFrame(){
        return System.currentTimeMillis() - timeOfUpdate;
    }

    public synchronized void displayFrame(int cameraId, byte[] imageCopy) {
        currentFrames.put(cameraId, imageCopy);
        setChanged();
        notifyObservers(this);


    }

    public synchronized void addImage(ImageFrame image){
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

    public synchronized Set<Integer> getCameraIds(){

        return currentFrames.keySet();
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

    public synchronized void receivedFrame() {
        notifyAll();
    }
}
