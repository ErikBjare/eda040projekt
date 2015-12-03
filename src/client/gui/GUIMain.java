package client.gui;

import client.*;
import client.camera.Camera;
import common.Constants;
import common.LogUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by von on 2015-11-08.
 */
public class GUIMain extends JFrame implements Observer {
    public SyncModeControl syncButtons;
    public ModeControl modeButtons;
    public SystemMonitor monitor;
    public HashMap<Integer, CameraControl> cams;
    private JLabel currentSyncMode;

    public GUIMain(String s, SystemMonitor monitor) {
        super(s);
        this.cams = new HashMap<>(8);
        this.monitor = monitor;
        syncButtons = new SyncModeControl();
        modeButtons = new ModeControl();
        setMinimumSize(new Dimension(500, 500));
//        this.createImage(460, 260);
        setLayout(new BorderLayout());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        addButtonActionListener(syncAutoButton, SyncMode.Sync);
        addButtonActionListener(syncButton, SyncMode.ForceSync);
        addButtonActionListener(asyncButton, SyncMode.ForceAsync);
        addButtonActionListener(autoButton, Mode.Idle);
        addButtonActionListener(idleButton, Mode.ForceIdle);
        addButtonActionListener(movieButton, Mode.ForceMovie);


        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BorderLayout());
        JPanel westMenuBar = new JPanel();
        westMenuBar.setLayout(new BorderLayout());
        JPanel eastMenuBar = new JPanel();
        eastMenuBar.setLayout(new BorderLayout());


        add(menuBar, BorderLayout.NORTH);

        currentSyncMode = new JLabel("Sync");
        menuBar.add(currentSyncMode, BorderLayout.CENTER);

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
        String[] cameraPlacements = new String[]{"East", "West"};
        int i = 0;
        for (int id : monitor.getCameraIds()) {
            LogUtil.info("Found id:" + id);
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
                        syncButtons.setEnabled(button);

                        syncButtons.clearSelection();
                        syncButtons.setSelected(button.getModel(), true);
                    }
                });
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
                        modeButtons.setEnabled(button);

                        modeButtons.clearSelection();
                        modeButtons.setSelected(button.getModel(), true);
                    }
                });
            }
        });
    }

    public void update(Observable observable, Object o) {
        if ((GUIUpdate) o == GUIUpdate.SyncModeUpdate)
            SwingUtilities.invokeLater(() -> {
                currentSyncMode.setText(monitor.getSyncMode().toString());

            });
//        SwingUtilities.invokeLater(this::render);
    }


    public static void main(String[] args) {
        SystemMonitor monitor = new SystemMonitor();

//        Camera[] cameras = {new Camera(monitor, "localhost", 9191, 1)};
        Camera[] cameras = new Camera[0];
        try {
            cameras = new Camera[]{new Camera(monitor, "localhost", 5656, 0), new Camera(monitor, "localhost", 5657, 1)};
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        Animator anim = new Animator(monitor);
        monitor.init(cameras);

        GUIMain gui = new GUIMain(Constants.GUI_TITLE, monitor);
        anim.start();
//        sleep(100);
        Random random = new Random();
//        byte[] initialImage = new byte[10000];
//        random.nextBytes(initialImage);
//        gui.cams[0].displayImage(initialImage);
        monitor.addObserver(gui);
    }
}
