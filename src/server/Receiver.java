package server;

import client.Mode;
import common.NetworkUtil;
import common.protocol.MsgType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.SwitchPoint;
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
    }
    public void run(){
        while (true){
            try{
                InputStream s = socket.getInputStream();
                int firstByte = s.read(); //Reads the first byte
                switch (firstByte){
                    case 0: //Connect
                        monitor.connect();
                        break;
                    case 3: //Change Mode
                        int nextByte = s.read();
                        if(nextByte == 0) monitor.setMode(Mode.ForceIdle);
                        else if( nextByte == 1) monitor.setMode(Mode.ForceMovie);

                        break;
                    case 4: //Shutdown message
                        monitor.shutdown();
                        break;
                    default:  //Wrong message type.
                        break;
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
