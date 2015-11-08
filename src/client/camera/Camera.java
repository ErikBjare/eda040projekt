package client.camera;

import client.SystemMonitor;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Camera {
    protected CameraReceiver receiver;
    protected CameraSender sender;
    protected FrameBuffer buffer;

    public Camera(SystemMonitor system, String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        this.receiver = new CameraReceiver(system, socket, this);
        this.sender = new CameraSender(system, socket, this);
        this.buffer = new FrameBuffer();
    }


}
