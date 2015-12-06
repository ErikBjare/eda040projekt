package client.gui;

import javax.swing.*;
import java.util.Enumeration;

/**
 * Created by von on 2015-11-08.
 */
public class ModeControl extends ButtonGroup {

    public void setEnabled(JButton button) {
        Enumeration<AbstractButton> e = this.getElements();
        while(e.hasMoreElements()){
            e.nextElement().setEnabled(true);
        }
        button.setEnabled(false);
    }
}
