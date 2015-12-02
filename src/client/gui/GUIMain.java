package client.gui;

import client.Animator;
import client.Mode;
import client.SyncMode;
import client.SystemMonitor;
import client.camera.Camera;
import common.Constants;
import common.LogUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by von on 2015-11-08.
 */
public class GUIMain extends JFrame implements Observer {
    public SyncModeControl syncButtons;
    public ModeControl modeButtons;
    public SystemMonitor monitor;
    public HashMap<Integer, CameraControl> cams;

    public GUIMain(String s, SystemMonitor monitor) throws HeadlessException {
        super(s);
        this.cams = new HashMap<>(8);
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

        syncButtons.add(syncAutoButton);
        syncButtons.add(syncButton);
        syncButtons.add(asyncButton);
        modeButtons.add(autoButton);
        modeButtons.add(idleButton);
        modeButtons.add(movieButton);

        addButtonActionListener(syncAutoButton, SyncMode.Auto);
        addButtonActionListener(syncButton, SyncMode.ForceSync);
        addButtonActionListener(asyncButton, SyncMode.ForceAsync);
        addButtonActionListener(autoButton, Mode.Auto);
        addButtonActionListener(idleButton, Mode.ForceIdle);
        addButtonActionListener(movieButton, Mode.ForceMovie);


        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BorderLayout());
        JPanel westMenuBar = new JPanel();
        westMenuBar.setLayout(new BorderLayout());
        JPanel eastMenuBar = new JPanel();
        eastMenuBar.setLayout(new BorderLayout());


        add(menuBar, BorderLayout.NORTH);


        menuBar.add(westMenuBar, BorderLayout.WEST);
        menuBar.add(eastMenuBar, BorderLayout.EAST);

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
        //TODO Add correct bordering. Now hardcoded
        String [] cameraPlacements = new String[]{"East", "West"};
        int i = 0;
        for (int id : monitor.getCameraIds()) {
            cams.put(id, new CameraControl(monitor, id));
            add(cams.get(id), cameraPlacements[i]);
            monitor.addObserver(cams.get(id));
            i++;

        }
//
//        for (int i = 0; i < monitor.getNrCameras(); i++) {
//            cams[i] = new CameraControl();
//            add(cams[i], BorderLayout.SOUTH);
//        }

        //update    (monitor, this);
        pack();
        setVisible(true);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void addButtonActionListener(JButton button, SyncMode syncMode) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        monitor.setSyncMode(syncMode);
                    }
                });

                System.out.println("Pressed AutoSync");
            }
        });
    }

    private void addButtonActionListener(JButton button, Mode mode) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        monitor.setMode(mode);
                    }
                });

                System.out.println("Pressed AutoSync");
            }
        });
    }

    public void update(Observable observable, Object o) {

//        SwingUtilities.invokeLater(this::render);
    }


    public static void main(String[] args) throws IOException {
        SystemMonitor monitor = new SystemMonitor();

//        Camera[] cameras = {new Camera(monitor, "localhost", 9191, 1)};
        Camera[] cameras = {new Camera(monitor, "localhost", 5656, 0),new Camera(monitor, "localhost", 5657, 1)};

        Animator anim = new Animator(monitor);
        monitor.init(cameras);
        anim.start();
        GUIMain gui = new GUIMain(Constants.GUI_TITLE, monitor);
        Random random = new Random();
//        byte[] initialImage = new byte[10000];
//        random.nextBytes(initialImage);
//        gui.cams[0].displayImage(initialImage);
        monitor.addObserver(gui);
    }
}
