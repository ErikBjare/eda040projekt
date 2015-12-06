package server;

import server_util.LogUtil;
//import se.lth.cs.eda040.realcamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int listeningPort = Integer.parseInt(args[0]);

        AxisM3006V hardware = new AxisM3006V();
        hardware.init();
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9191;
        if (args.length > 1) {
            String cameraHostname = args[1];
            int cameraPort = Integer.parseInt(args[2]);
            hardware.setProxy(cameraHostname, cameraPort);
        }
        hardware.connect();

        try {
            Monitor monitor = new Monitor(hardware);

            JPEGHTTPServer jpeghttpServer = new JPEGHTTPServer(hardware, 6077, monitor);
            jpeghttpServer.start();

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
