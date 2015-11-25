package client.gui;

import common.LogUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by von on 2015-11-08.
 */
public class CameraControl extends JPanel implements Observer {
    public ImageIcon icon;
    public JLabel delay;

    public CameraControl() {
        super();
        icon = new ImageIcon();
        delay = new JLabel(icon);
        setLayout(new BorderLayout());
        add(delay, BorderLayout.CENTER);
        this.setSize(200, 200);
    }

    public void displayImage(byte[] image){
        Image img = getToolkit().createImage(image);
        LogUtil.info("Rendering Icon: "+image.length);
        getToolkit().prepareImage(img, -1, -1, null);
        icon.setImage(img);
        icon.paintIcon(this, this.getGraphics(), 5, 5);
    }

    public void displayDelay(long delay){
        this.delay.setText(Long.toString(delay));

    }

    public void update(Observable observable, Object o) {

    }
}
