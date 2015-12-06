package server;

import server_util.LogUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void start(AxisWrapper hardware){
//        hardware.init();
        hardware.connect();

        try {
            Monitor monitor = new Monitor(hardware);

//            JPEGHTTPServer jpeghttpServer = new JPEGHTTPServer(hardware, 6077, monitor);
//            jpeghttpServer.start();

            ServerSocket sock = new ServerSocket(9191);

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
