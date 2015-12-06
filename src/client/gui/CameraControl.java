package client.gui;

import client_util.LogUtil;
import client.GUIUpdate;
import client.SystemMonitor;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by von on 2015-11-08.
 */
public class CameraControl extends JPanel implements Observer {
    public ImageIcon icon;
    public JLabel delay;
    private SystemMonitor system;
    private int cameraId;

    public CameraControl(SystemMonitor system, int cameraId) {
        super();
        this.system = system;
        this.cameraId = cameraId;
        icon = new ImageIcon();
        delay = new JLabel(icon);
        setLayout(new BorderLayout());
        add(delay, BorderLayout.CENTER);
        this.setSize(200, 200);
    }

    public void displayImage(byte[] image) {
//        LogUtil.info("Byte Array contains: " + Arrays.toString(image));
        Image img = getToolkit().createImage(image);
        LogUtil.info("Rendering Icon: " + image.length);
        getToolkit().prepareImage(img, -1, -1, null);
        icon.setImage(img);
        // icon.paintIcon(this, this.getGraphics(), 100, 0);
        revalidate();
        repaint();
    }

    public void displayDelay(long delay) {
        this.delay.setText(Long.toString(delay));

    }

    public void update(Observable observable, Object o) {
        if ((GUIUpdate) o == GUIUpdate.FrameUpdate) {
            System.out.println("Rendering Icon");
//        LogUtil.info("Rendering Icon");
            SwingUtilities.invokeLater(this::renderImage);
        }
    }


    public void renderImage() {
//        LogUtil.info("Entering renderimage!");
        synchronized (system) {

            byte[] img = system.getDisplayFrame(cameraId);
            if (img != null) {
                displayImage(img);
            } else {
                LogUtil.info("Tried to render image, but no image available.");
            }
        }
    }
}
