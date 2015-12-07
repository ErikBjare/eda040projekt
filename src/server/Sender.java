package server;

import server_util.LogUtil;

import java.io.IOException;
import java.net.Socket;

public class Sender extends Thread {
    private Monitor monitor;
    private Socket socket;

    public Sender(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
//        setName("Sender");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                monitor.sendNext(socket);
              //  sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.exception("Sender was interrupted", e);
                break;
            } catch (IOException e) {
                LogUtil.exception("Socket closed due to IOException.", e);
                break;
            } catch (ShutdownException e) {
                this.interrupt();
                LogUtil.info("Sender shutdown.");
                break;
            }
        }
    }


}
