package client.camera;

import client.SystemMonitor;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class CameraSender extends Thread {
    protected SystemMonitor system;
    protected Socket socket;
    protected Camera camera;

    public CameraSender(SystemMonitor system, Socket socket, Camera camera) {
        this.system = system;
        this.socket = socket;
        this.camera = camera;
    }
}
