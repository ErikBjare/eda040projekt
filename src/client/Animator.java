package client;

import client.camera.Camera;
import common.protocol.NewFrame;

import java.util.ArrayList;

/**
 * Created by von on 2015-11-08.
 */
public class Animator extends Thread {
    protected SystemMonitor system;


    public Animator(SystemMonitor system) {
        this.system = system;
        setName("Animator");
    }

    public void run() {
        //TODO add more cameras
        try {
            while (true) {
                system.animate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
