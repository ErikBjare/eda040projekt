package client.camera;

import client.SystemMonitor;
import common.NetworkUtil;
import common.protocol.Message;
import common.protocol.NewFrame;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Camera {
    protected Socket socket;
    protected CameraReceiver receiver;
    protected CameraSender sender;
    protected FrameBuffer buffer;

    public Camera(SystemMonitor system, String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.receiver = new CameraReceiver(system, socket, this);
        this.sender = new CameraSender(system, socket, this);
        this.buffer = new FrameBuffer();
        receiver.start();
        sender.start();
    }

    public void receiveFrame() throws IOException {
        int resp = -1;
        while (resp == -1) resp = socket.getInputStream().read();
        if (resp == -1) throw new RuntimeException("Socket returned -1");
        byte msgType = (byte) resp;
        System.out.println("New message - msgType: "+msgType);
        NewFrame mess = new NewFrame(socket);
        System.out.println("frame size: "+mess.size);
        System.out.println("timestamp: "+mess.timestamp);
    }
}
