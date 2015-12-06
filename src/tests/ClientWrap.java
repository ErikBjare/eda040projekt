import client.SystemMonitor;
import client.camera.Camera;
import common.Constants;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by von on 2015-11-25.
 */
public class ClientWrap extends Thread {
    boolean crash = false;
    Camera camera;
    Supplier<SystemMonitor> systemMonitorConstructor;
    Function<SystemMonitor, Camera> cameraConstructor;

    public ClientWrap(Supplier<SystemMonitor> systemMonitorConstructor, Function<SystemMonitor, Camera> cameraConstructor) {
        this.systemMonitorConstructor = systemMonitorConstructor;
        this.cameraConstructor = cameraConstructor;
    }
    public ClientWrap() {
        this.systemMonitorConstructor = ()->new SystemMonitor();
        this.cameraConstructor = sm -> {
            try {
                //TODO change camera ID to be variable
                return new Camera(sm, Constants.HOST, Constants.PORT, 0);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                e.printStackTrace();
            }
            throw new Error("exception in client (camera) creation");
        };
    }

    public void run() {
        SystemMonitor monitor = systemMonitorConstructor.get();
        System.out.println("Trying to connect...");
        while(!Thread.currentThread().isInterrupted()) {
            camera = cameraConstructor.apply(monitor);
            monitor.init(new Camera[]{camera});
            System.out.println("Connected to client");
            break;
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
        System.out.println("ClientWrap has been stopped");
    }
}
