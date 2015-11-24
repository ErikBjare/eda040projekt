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
    private FrameBuffer[] frameBuffers;


    public Animator(SystemMonitor system, Camera[] cameraList) {
        this.system = system;
        System.out.println(cameraList.length);
        this.frameBuffers = new FrameBuffer[cameraList.length];
        System.out.println("FrameBuffer constructor over");
        for (int i = 0; i < cameraList.length; i++) {
            frameBuffers[i] = cameraList[i].getBuffer();
        }
        System.out.println("Last check in animator");
    }

    public void run() {
        //TODO add more cameras
        while (true) {
            NewFrame next;
            next = frameBuffers[0].removeFirstFrame();

            if (next != null) {
                System.out.println("Displaying frame, found a picture in the buffer");
                system.displayFrame(0, next.getFrame());
            }
        }
    }
}
