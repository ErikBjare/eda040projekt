package server;

import server_util.LogUtil;
import se.lth.cs.eda040.realcamera.AxisM3006V;
//import se.lth.cs.eda040.proxycamera.AxisM3006V;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class Main {
    public static void main(String[] args) {
        String cameraHostname = args[1];
        int cameraPort = Integer.parseInt(args[2]);

        AxisM3006V hardware = new AxisM3006V();
        hardware.init();
        hardware.setProxy(cameraHostname, cameraPort);
        hardware.connect();

        try {
//            JPEGHTTPServer jpeghttpServer = new JPEGHTTPServer(hardware, 6077);
//            jpeghttpServer.start();

            ServerSocket sock = new ServerSocket(Integer.parseInt(args[0]));

            while (!Thread.interrupted()) {
                try {
                    LogUtil.info("Started listening for new client");
                    Socket client = sock.accept();
                    LogUtil.info("Accepted connection");

                    CameraServer cameraServer = new CameraServer(hardware, client);

                    cameraServer.join();
                   LogUtil.info("Finished with client");
                } catch (IOException e) {
                    LogUtil.exception(e);
                }
            }

        } catch (IOException e) {
            LogUtil.exception(e);
        }
    }

}
