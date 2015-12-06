package server;

import server_util.LogUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void start(AxisWrapper hardware){
        System.out.println(hardware);
        System.out.println("before connect");
        hardware.connect();
        System.out.println("after connect");
        System.out.println("before init");
        hardware.init();
        System.out.println("after init");

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
