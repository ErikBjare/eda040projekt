package client.camera;

import client.SystemMonitor;
import client_util.LogUtil;
import common.protocol.MsgType;
import common.protocol.NewFrame;

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
        setName("CameraReceiver"+getName().substring(getName().indexOf('-')));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
//                LogUtil.info("Entering recieveframe");
                int resp = -1;
                while (resp == -1) resp = socket.getInputStream().read();
                if (resp == -1) throw new RuntimeException("Socket returned -1");
                byte msgType = (byte) resp;
                if (msgType != MsgType.newFrame) throw new RuntimeException("Invalid message type!");
//                LogUtil.info("New message - msgType: " + msgType);
                ImageFrame mess = new ImageFrame(camera.id, new NewFrame(socket));
                system.addImage(mess);
            } catch (IOException e) {
                LogUtil.exception(e);
            }
        }
    }
}
