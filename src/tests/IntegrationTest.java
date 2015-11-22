import client.SystemMonitor;
import client.camera.Camera;
import org.junit.Test;
import server.CameraServer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by erb on 2015-11-22.
 */
public class IntegrationTest {

    @Test
    public void startThenStopClient() {
        Client client = new Client();
        client.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.cancel();
    }

    @Test
    public void startThenStopServer() {
        Server server = new Server();
        server.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.cancel();
    }

    @Test
    public void startAndConnectThenStopBoth() {
        // TODO: Actually check if things go right and not just terminate...
        Server server = new Server();
        server.start();
        Client client = new Client();
        client.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO: Check if connection was successful and if the proper data was transmitted

        client.cancel();
        server.cancel();


    }

}

class Server extends Thread {
    boolean crash = false;
    CameraServer cameraServer;

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
                    cameraServer = new CameraServer(client);
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
        System.out.println("Server has been stopped");
    }
}

class Client extends Thread {
    boolean crash = false;
    Camera camera;

    public void run() {
        SystemMonitor monitor = new SystemMonitor();
        System.out.println("Trying to connect...");
        while(!Thread.currentThread().isInterrupted()) {
            try {
                camera = new Camera(monitor, "localhost", 5656);
                System.out.println("Connected to client");
                break;
            } catch (ConnectException e) {
                continue;
            } catch (UnknownHostException e) {
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
        if (camera != null) {
            camera.stop();
            camera.join();
        }
        System.out.println("Client has been stopped");
    }
}
