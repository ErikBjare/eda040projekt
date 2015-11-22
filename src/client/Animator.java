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



    public Animator(SystemMonitor system, ArrayList<Camera> cameraList) {
        this.system = system;

//        this.cameraList = cameraList;
        this.frameBuffers = (FrameBuffer[])cameraList.stream().map(c->c.getBuffer()).toArray();
    }
    public void run(){
        //TODO add more cameras
        while(true){
        long timeStamp = frameBuffers[0].getNextTime();
        byte []next = frameBuffers[0].removeFirstFrame();
        if(next != null && timeStamp > -1){
            system.displayFrame(0, next);

        }

    }
    }
}
