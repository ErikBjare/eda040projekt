package server;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Receiver extends Thread {
    public Receiver(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    private Monitor monitor;
    private Socket socket;
    public void run(){

    }
}
