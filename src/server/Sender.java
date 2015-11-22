package server;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Sender extends Thread {
    private Monitor monitor;
    private Socket socket;



    public Sender(Monitor monitor) {
        this.monitor = monitor;

    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                monitor.sendNext();
            } catch (InterruptedException e) {
                System.out.println("Sender was interrupted");
                break;
            }
        }
    }


}
