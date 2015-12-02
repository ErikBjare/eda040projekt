package server;

import common.LogUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by von on 2015-11-08.
 */
public class Main {
    public static void main(String[] args) {
        ServerSocket sock = null;

        try {
            sock = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {
                LogUtil.info("Started listening for new client");
                Socket client = sock.accept();
                LogUtil.info("Accepted connection");
                CameraServer cameraServer = new CameraServer(args[1], Integer.parseInt(args[2]), client);
                cameraServer.join();
                LogUtil.info("Finished with client");
            }
        } catch (IOException e) {
            LogUtil.exception(e);
        }
    }
}
