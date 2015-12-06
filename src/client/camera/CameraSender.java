package client.camera;

import client.SystemMonitor;
import client_util.LogUtil;
import common.protocol.Message;

import java.io.IOException;
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
        setName("CameraSender" + getName().substring(getName().indexOf('-')));
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Message mess = camera.getNextMessage();
                LogUtil.info("Got message to send.");
                try {
                    mess.send(socket);
                    LogUtil.info("Sent message.");
                } catch (IOException e) {
                    LogUtil.exception(e);
                }
            }
        } catch (InterruptedException e) {
            LogUtil.exception(e);
        }
    }
}
