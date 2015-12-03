package client.camera;

import client.SystemMonitor;
import common.LogUtil;
import common.protocol.Message;
import common.protocol.NewFrame;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by von on 2015-11-05.
 */
public class Camera {
    protected Socket socket;
    protected CameraReceiver receiver;
    protected CameraSender sender;
    protected FrameBuffer buffer;
    protected SystemMonitor system;
    public int id;

    Queue<Message> mailbox;


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
        this.mailbox = new ConcurrentLinkedQueue<>();
        receiver.start();
        sender.start();
    }

    public synchronized void receiveFrame() throws IOException {
//        System.out.println("Current reciever thread " + Thread.currentThread());
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

    public synchronized void addMessage(Message mess){
        mailbox.add(mess);
        notifyAll();
    }

    public synchronized Message getNextMessage() throws InterruptedException {
        while (mailbox.isEmpty()) wait();
        notifyAll();
        return mailbox.poll();
    }
}
