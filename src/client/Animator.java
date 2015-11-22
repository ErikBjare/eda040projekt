package client;

import client.camera.Camera;
import client.camera.FrameBuffer;
import common.protocol.NewFrame;

import java.util.ArrayList;

/**
 * Created by von on 2015-11-08.
 */
public class Animator extends Thread {
    protected SystemMonitor system;
//    protected ArrayList<Camera> cameraList;
    private FrameBuffer[] frameBuffers;



    public Animator(SystemMonitor system, Camera[] cameraList) {
        this.system = system;

//        this.cameraList = cameraList;
        this.frameBuffers = new FrameBuffer[cameraList.length];

        for (int i = 0; i < cameraList.length; i++) {
            frameBuffers[i] = cameraList[i].getBuffer();
        }

    }
    public void run(){
        //TODO add more cameras
        while(true){
        long timeStamp = frameBuffers[0].getNextTime();
        byte []next = frameBuffers[0].removeFirstFrame();
        if(next != null && timeStamp > -1){
            System.out.println("Displaying frame, found a picture in the buffer");
            system.displayFrame(0, next);

        }

    }
    }
}
