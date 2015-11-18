package server;

import client.Mode;
import common.protocol.Constants;

import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Sender extends Thread {
    private Monitor monitor;
    private Socket socket;
    private long lastSentFrameTime;


    public Sender(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    public void run() {

        try {
            getReadyToSend(monitor.mode);
            monitor.sendNext();
            lastSentFrameTime = System.currentTimeMillis();
            
            } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getReadyToSend(Mode mode) throws InterruptedException {
        long frameMsInterval;
        if (mode == Mode.Idle || mode == Mode.ForceIdle) {
            frameMsInterval = 1000 / Constants.IDLE_FRAMERATE;
        } else {
            frameMsInterval = 1000 / Constants.MOVIE_FRAMERATE;
        }
        long timeDiff = System.currentTimeMillis() - lastSentFrameTime;

        if (timeDiff < frameMsInterval) {
            wait(frameMsInterval - timeDiff);
        }

    }
}
