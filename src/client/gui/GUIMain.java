package client.gui;

import client.Animator;
import client.SystemMonitor;
import client.camera.Camera;
import common.LogUtil;

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
    SyncModeControl syncButtons;
    ModeControl modeButtons;
    SystemMonitor monitor;
    CameraControl[] cams;

    public GUIMain(String s, SystemMonitor monitor) throws HeadlessException {
        super(s);
        this.monitor = monitor;
        syncButtons = new SyncModeControl();
        modeButtons = new ModeControl();
        setMinimumSize(new Dimension(100, 100));
        this.createImage(460, 260);
        setLayout(new BorderLayout());

        //init buttons
        JButton syncAutoButton = new JButton("Auto");
        JButton syncButton = new JButton("Sync");
        JButton asyncButton = new JButton("Async");
        JButton autoButton = new JButton("Auto");
        JButton idleButton = new JButton("Idle");
        JButton movieButton = new JButton("Movie");

        syncButtons.add(syncAutoButton); syncButtons.add(syncButton); syncButtons.add(asyncButton);
        modeButtons.add(autoButton); modeButtons.add(idleButton); modeButtons.add(movieButton);

        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BorderLayout());
        JPanel titleBar = new JPanel();
        JPanel southTitleBar = new JPanel();
        JPanel westMenuBar = new JPanel();
        westMenuBar.setLayout(new BorderLayout());
        JPanel eastMenuBar = new JPanel();
        eastMenuBar.setLayout(new BorderLayout());
        JPanel westNorthMenuBar = new JPanel();
        JPanel eastNorthMenuBar = new JPanel();


        add(menuBar, BorderLayout.NORTH);

//        menuBar.add(titleBar, BorderLayout.NORTH);
//        menuBar.add(southTitleBar, BorderLayout.SOUTH);
//        southTitleBar.add(westMenuBar, BorderLayout.WEST);
//        southTitleBar.add(eastMenuBar, BorderLayout.EAST);

        menuBar.add(westMenuBar, BorderLayout.WEST);
        menuBar.add(eastMenuBar, BorderLayout.EAST);
//        eastMenuBar.add(eastNorthMenuBar, BorderLayout.NORTH);
//        westMenuBar.add(westNorthMenuBar, BorderLayout.NORTH);

        eastMenuBar.add(syncAutoButton, BorderLayout.WEST);
        eastMenuBar.add(syncButton, BorderLayout.CENTER);
        eastMenuBar.add(asyncButton, BorderLayout.EAST);

        westMenuBar.add(autoButton, BorderLayout.WEST);
        westMenuBar.add(idleButton, BorderLayout.CENTER);
        westMenuBar.add(movieButton, BorderLayout.EAST);

        JLabel title = new JLabel("Mode", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        westMenuBar.add(title, BorderLayout.NORTH);
        title = new JLabel("Sync Mode", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        eastMenuBar.add(title, BorderLayout.NORTH);
        cams = new CameraControl[monitor.getNrCameras()];
        for(int i = 0; i < monitor.getNrCameras(); i++){
            cams[i] = new CameraControl();
            add(cams[i], BorderLayout.SOUTH);
        }

        //update(monitor, this);
        pack();
        setVisible(true);


    }

    public void update(Observable observable, Object o) {
        SwingUtilities.invokeLater(this::render);
    }

    private void render() {
        LogUtil.info("Rendering GUI");
        for(int i = 0; i < cams.length; i++){
            renderImage(i);
        }
    }

    private void renderImage(int i) {
     synchronized (monitor) {
         byte[] img = monitor.getDisplayFrame(i);
         if (img != null){
         cams[i].displayImage(img);
     }
     }
    }

    public static void main(String[] args) throws IOException {
        SystemMonitor monitor = new SystemMonitor();
        //Camera [] cameras = {new Camera(monitor, "localhost", 5656), new Camera(monitor, "localhost", 5656)};
        Camera [] cameras = {new Camera(monitor, "localhost", 5656)};
        Animator anim = new Animator(monitor);
        monitor.init(cameras);
        anim.start();
        GUIMain gui = new GUIMain("title", monitor);
        monitor.addObserver(gui);
    }
}
