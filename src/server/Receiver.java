package server;

import common.Mode;

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
                switch (firstByte) {
                    case 0: //Connect
                        monitor.connect();
                        break;
                    case 3: //Change Mode
                        int nextByte = s.read();
                        if (nextByte == 0) monitor.setMode(Mode.ForceIdle);
                        else if (nextByte == 1) monitor.setMode(Mode.ForceMovie);

                        break;
                    case 4: //Shutdown message
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
