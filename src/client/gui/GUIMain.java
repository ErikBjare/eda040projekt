package client.gui;

import client.SystemMonitor;
import client.camera.Camera;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by von on 2015-11-08.
 */
public class GUIMain extends JFrame implements Observer {
    List<CameraControl> cameras;

    public GUIMain(String s, SystemMonitor system) throws HeadlessException {
        super(s);
        this.system = system;
    }

    SyncModeControl syncButtons;
    ModeControl modeButtons;

    SystemMonitor system;

    public void update(Observable observable, Object o) {

    }

    public static void main(String[] args) throws IOException {
        SystemMonitor monitor = new SystemMonitor();
        Camera [] cameras = {new Camera(monitor, "a", 2222), new Camera(monitor, "b", 2222)};
        GUIMain gui = new GUIMain("title", monitor);
        monitor.setCameraList(cameras);
        monitor.addObserver(gui);
    }
}
