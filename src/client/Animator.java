package client;

import client.camera.Camera;

import java.util.ArrayList;

/**
 * Created by von on 2015-11-08.
 */
public class Animator extends Thread {
    protected SystemMonitor system;
    protected ArrayList<Camera> cameraList;


    public Animator(SystemMonitor system, ArrayList<Camera> cameraList) {
        this.system = system;
        this.cameraList = cameraList;
    }
    public void run(){


    }
}
