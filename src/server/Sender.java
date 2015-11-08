package server;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Sender extends Thread {
    public Sender(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    private Monitor monitor;
    private Socket socket;
    public void run(){

    }
}
