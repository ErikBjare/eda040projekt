package client.camera;

import client.SystemMonitor;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class CameraReceiver extends Thread {
    protected SystemMonitor system;
    protected Socket socket;
    protected Camera camera;

    public CameraReceiver(SystemMonitor system, Socket socket, Camera camera) {
        this.system = system;
        this.socket = socket;
        this.camera = camera;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                camera.receiveFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
