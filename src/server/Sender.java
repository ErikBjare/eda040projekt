package server;

import server_util.LogUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Sender extends Thread {
    private Monitor monitor;
    private Socket socket;



    public Sender(Monitor monitor) {
        this.monitor = monitor;
        setName("Sender");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                monitor.sendNext();
              //  sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.exception("Sender was interrupted", e);
                break;
            } catch (IOException e) {
                LogUtil.exception("Socket closed due to IOException.", e);
                break;
            } catch (ShutdownException e) {
                this.interrupt();
                break;
            }
        }
    }


}
