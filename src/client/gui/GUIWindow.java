package client.gui;

import client.Animator;
import client.GUIUpdate;
import client.SyncMode;
import client.SystemMonitor;
import client.camera.Camera;
import common.Constants;
import common.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by von on 2015-11-08.
 */
public class GUIWindow extends JFrame implements Observer {
    public SyncModeControl syncButtons;
    public ModeControl modeButtons;
    public SystemMonitor monitor;
    public HashMap<Integer, CameraControl> cams;
    private JLabel currentSyncMode;

    public GUIWindow(String s, SystemMonitor monitor) {
        super(s);
        this.cams = new HashMap<>(8);
        this.monitor = monitor;
        syncButtons = new SyncModeControl();
        setMinimumSize(new Dimension(500, 500));
//        this.createImage(460, 260);
        setLayout(new BorderLayout());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //init buttons
        JButton syncAutoButton = new JButton("Auto");
        JButton syncButton = new JButton("Forced Sync");
        JButton asyncButton = new JButton("Forced Async");
        JButton autoButton = new JButton("Auto");
        JButton idleButton = new JButton("Forced Idle");
        JButton movieButton = new JButton("Forced Movie");

        syncButtons.add(syncAutoButton);
        syncButtons.add(syncButton);
        syncButtons.add(asyncButton);


        JPanel westMenuBar = new JPanel();

        JLabel modeType = new JLabel("Mode: Idle", JLabel.CENTER);
        modeType.setFont(new Font("Arial", Font.BOLD, 26));
        modeButtons = new ModeControl(westMenuBar, modeType);

        modeButtons.add(autoButton);
        modeButtons.add(idleButton);
        modeButtons.add(movieButton);
        monitor.addObserver(modeButtons);

        addButtonActionListener(syncAutoButton, SyncMode.Sync);
        addButtonActionListener(syncButton, SyncMode.ForceSync);
        addButtonActionListener(asyncButton, SyncMode.ForceAsync);
        addButtonActionListener(autoButton, Mode.Idle);
        addButtonActionListener(idleButton, Mode.ForceIdle);
        addButtonActionListener(movieButton, Mode.ForceMovie);


        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BorderLayout());
        westMenuBar.setLayout(new BorderLayout());
        JPanel eastMenuBar = new JPanel();
        eastMenuBar.setLayout(new BorderLayout());


        add(menuBar, BorderLayout.SOUTH);

        currentSyncMode = new JLabel("Cameras: <Not Synchronized>");
        currentSyncMode.setHorizontalAlignment(SwingConstants.CENTER);
        currentSyncMode.setFont(new Font("Arial", Font.BOLD, 30));
        menuBar.add(currentSyncMode, BorderLayout.CENTER);

        menuBar.add(westMenuBar, BorderLayout.WEST);
        menuBar.add(eastMenuBar, BorderLayout.EAST);

        eastMenuBar.add(syncAutoButton, BorderLayout.WEST);
        eastMenuBar.add(syncButton, BorderLayout.CENTER);
        eastMenuBar.add(asyncButton, BorderLayout.EAST);

        westMenuBar.add(autoButton, BorderLayout.WEST);
        westMenuBar.add(idleButton, BorderLayout.CENTER);
        westMenuBar.add(movieButton, BorderLayout.EAST);

        westMenuBar.add(modeType, BorderLayout.NORTH);
        JLabel title = new JLabel("Sync Mode", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        eastMenuBar.add(title, BorderLayout.NORTH);

        GridLayout cameraDisplay = new GridLayout(2, 4);
        JPanel cameraPlacement = new JPanel(cameraDisplay);
        add(cameraPlacement, BorderLayout.CENTER);
        for (int id : monitor.getCameraIds()) {
            cams.put(id, new CameraControl(monitor, id));
            cameraPlacement.add(cams.get(id));
            monitor.addObserver(cams.get(id));
        }
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

    private void addButtonActionListener(JButton button, int mode) {
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

        if ((GUIUpdate) o == GUIUpdate.SyncModeUpdate) {
            SwingUtilities.invokeLater(() -> {
                if (monitor.getSyncMode() == SyncMode.Async || monitor.getSyncMode() == SyncMode.ForceAsync ){
                currentSyncMode.setText("Cameras: <Not Synchronized>");
                }else{currentSyncMode.setText("Cameras: <Synchronized>");
                }


            });
//        SwingUtilities.invokeLater(this::render);
        }
    }


    public static void main(String[] args) {
        SystemMonitor monitor = new SystemMonitor();
        ArrayList<Camera> cameras = new ArrayList<>();
        try {
            for (int i = 0; i < args.length; i += 2) {
                cameras.add(new Camera(monitor, args[i], Integer.parseInt(args[i + 1]), i / 2));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        Animator anim = new Animator(monitor);
        monitor.init(cameras);

        GUIWindow gui = new GUIWindow(Constants.GUI_TITLE, monitor);
        anim.start();
        monitor.addObserver(gui);
    }
}
