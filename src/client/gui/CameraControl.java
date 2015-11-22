package client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by von on 2015-11-08.
 */
public class CameraControl extends JPanel implements Observer {
    ImageIcon icon;
    JLabel delay;

    public CameraControl() {
        super();
        icon = new ImageIcon();
        delay = new JLabel();
        this.setSize(200, 200);
    }

    public void renderImage(byte[] image){
        Image img = getToolkit().createImage(image);
        getToolkit().prepareImage(img, -1, -1, null);
        icon.setImage(img);
        icon.paintIcon(this, this.getGraphics(), 5, 5);
    }

    public void update(Observable observable, Object o) {

    }
}
