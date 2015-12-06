package client.gui;

import client.GUIUpdate;
import common.Mode;
import client.SystemMonitor;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by von on 2015-11-08.
 */
public class ModeControl extends ButtonGroup implements Observer {
    private final JPanel westMenuBar;
    private final JLabel modeType;

    public ModeControl(JPanel westMenuBar, JLabel modeType) {

        this.westMenuBar = westMenuBar;
        this.modeType = modeType;
    }

    public void setEnabled(JButton button) {
        Enumeration<AbstractButton> e = this.getElements();
        while(e.hasMoreElements()){
            e.nextElement().setEnabled(true);
        }
        button.setEnabled(false);
    }

    @Override
    public void update(Observable o, Object arg) {

        SystemMonitor monitor = (SystemMonitor) o;
        if((GUIUpdate) arg == GUIUpdate.ModeUpdate){
            // TODO: Print actual string of mode, rather than int
            modeType.setText("Mode:" + monitor.getMode());
            if(monitor.getMode() == Mode.Movie) {

                Enumeration<AbstractButton> e = this.getElements();
                while(e.hasMoreElements()){
                    e.nextElement().setEnabled(true);
                }
            }
        }
    }
}
