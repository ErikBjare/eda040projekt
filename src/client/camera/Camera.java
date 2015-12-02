package client.camera;

import client.SystemMonitor;
import common.LogUtil;
import common.protocol.NewFrame;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by von on 2015-11-05.
 */
public class Camera {
    protected Socket socket;
    protected CameraReceiver receiver;
    protected CameraSender sender;
    protected FrameBuffer buffer;
    protected SystemMonitor system;
    private int id;


    public Camera(SystemMonitor system, String host, int port, int id) throws UnknownHostException, ConnectException {
        this.id = id;
        try {
            this.socket = new Socket(host, port);
        } catch (ConnectException | UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            LogUtil.exception(e);
        }
        this.system = system;
        this.receiver = new CameraReceiver(system, socket, this);
        this.sender = new CameraSender(system, socket, this);
        this.buffer = new FrameBuffer();
        receiver.start();
        sender.start();
    }

    public synchronized void receiveFrame() throws IOException {
//        System.out.println("Current reciever thread " + Thread.currentThread());
        LogUtil.info("Entering recieveframe");
        int resp = -1;
        while (resp == -1) resp = socket.getInputStream().read();
        if (resp == -1) throw new RuntimeException("Socket returned -1");
        byte msgType = (byte) resp;
        LogUtil.info("New message - msgType: " + msgType);
        ImageFrame mess = new ImageFrame(id, new NewFrame(socket));
        system.addImage(mess);
        system.receivedFrame();
    }
    public synchronized void updateCurrentFrame(){

    }
    public FrameBuffer getBuffer(){
        return buffer;
    }

    public void stop() {
        receiver.interrupt();
        sender.interrupt();
    }

    public void join() {
        try {
            receiver.join();
            sender.join();
        } catch (InterruptedException e) {
            LogUtil.exception(e);
        }
    }
}
