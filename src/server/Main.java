package server;

import server_util.LogUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void start(int port, AxisWrapper hardware){
        hardware.init();
        hardware.connect();

        try {
            Monitor monitor = new Monitor();
            Updater updater = new Updater(monitor, hardware);

            JPEGHTTPServer jpeghttpServer = new JPEGHTTPServer(hardware, port+1000, monitor);
            jpeghttpServer.start();
            updater.start();

            ServerSocket sock = new ServerSocket(port);

            while (!Thread.interrupted()) {
                try {
                    LogUtil.info("Started listening for new client");
                    Socket client = sock.accept();
                    LogUtil.info("Accepted connection");

                    CameraServer cameraServer = new CameraServer(hardware, monitor, client);

                    cameraServer.join();
                    LogUtil.info("Finished with client");
                } catch (IOException e) {
                    LogUtil.exception(e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            LogUtil.exception(e);
        }
    }


}
