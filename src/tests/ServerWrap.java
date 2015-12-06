import common.Constants;
import server.CameraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

/**
 * Created by von on 2015-11-25.
 */
public class ServerWrap extends Thread {
    boolean crash = false;
    CameraServer cameraServer;
    Function<Socket, CameraServer> cameraServerConstructor;

    public ServerWrap(Function<Socket, CameraServer> cameraServerConstructor) {
        this.cameraServerConstructor = cameraServerConstructor;
    }

    public ServerWrap() {
        this.cameraServerConstructor = s -> new CameraServer(Constants.HOST, Constants.PORT,s);
    }

    public void run() {
        ServerSocket sock = null;
        try {
            sock = new ServerSocket(5656);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            sock.setSoTimeout(1000);
            System.out.println("Started server, waiting for connection");
            Socket client;
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    client = sock.accept();
                    System.out.println("Accepted connection");
                    cameraServer = cameraServerConstructor.apply(client);
                    break;
                } catch (java.net.SocketTimeoutException e) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(cameraServer != null) {
            cameraServer.stop();
            cameraServer.join();
        }
        System.out.println("ServerWrap has been stopped");
    }
}
