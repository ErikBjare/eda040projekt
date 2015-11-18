package server;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Sender extends Thread {
    private Monitor monitor;
    private Socket socket;

    public Sender(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    public void run(){

    }
}
