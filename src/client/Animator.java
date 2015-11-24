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


    public Animator(SystemMonitor system) {
        this.system = system;
    }

    public void run() {
        //TODO add more cameras
        while (true) {
            system.animate();
        }
    }

}
