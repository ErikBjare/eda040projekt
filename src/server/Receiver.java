package server;

import common.protocol.*;
import server_util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by von on 2015-11-05.
 */
public class Receiver extends Thread {
    private Monitor monitor;
    private Socket socket;

    public Receiver(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
        // setName not available in C compiler
//        setName("Receiver");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                InputStream s = socket.getInputStream();

                // This is necessary in order to be able to stop and then join the thread when
                // there are is no incoming data since s.read() is otherwise blocking.

                // setSoTimeout is not available in the java to c compiler
                //socket.setSoTimeout(1000);

                int firstByte = s.read(); //Reads the first byte
//                LogUtil.info("Received message: "+firstByte);
                switch (firstByte) {
                    case 0: //Connect
//                        LogUtil.info("Receiving message: Connect");
                        Connect c = new Connect(socket);
                        LogUtil.info("Received message: Connect");
                        monitor.connect();
                        break;
                    case 1: //Change Mode
//                        LogUtil.info("Receiving message: ModeChange");
                        ModeChange mc = new ModeChange(socket);
                        LogUtil.info("Received message: ModeChange");
                        monitor.setMode(mc.newMode);
                        break;
                    case 5: //Shutdown message
//                        LogUtil.info("Receiving message: Shutdown");
                        Shutdown sd = new Shutdown(socket);
                        LogUtil.info("Received message: Shutdown");
                        monitor.shutdown();
                        break;
                    default:  //Wrong message type.
                        break;
                }
            } catch (java.net.SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                monitor.shutdown();
                break;
            } catch (ShutdownException e) {
                break;
            }
        }
    }
}
