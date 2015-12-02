//package tests;

import client.Animator;
import client.SystemMonitor;
import client.camera.Camera;
import client.gui.CameraControl;
import client.gui.GUIMain;
import common.Constants;
import org.junit.Test;
import server.CameraServer;

import java.net.ConnectException;
import java.net.UnknownHostException;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by erb on 2015-11-22.
 */
public class IntegrationTest {

    @Test
    public void startThenStopClient() {
        ClientWrap client = new ClientWrap();
        client.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.cancel();
    }

    @Test
    public void startThenStopServer() {
        ServerWrap server = new ServerWrap();
        server.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.cancel();
    }

    @Test
    public void startAndConnectThenStopBoth() {
        // TODO: Actually check if things go right and not just terminate...
        ServerWrap server = new ServerWrap();
        server.start();
        ClientWrap client = new ClientWrap();
        client.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO: Check if connection was successful and if the proper data was transmitted

        client.cancel();
        server.cancel();


    }

    @Test
    public void guiStateChanged() throws InterruptedException {
        SystemMonitor sm = spy(new SystemMonitor());
        Animator anim = spy(new Animator(sm));
        ServerWrap server = new ServerWrap(s->spy(new CameraServer(Constants.HOST, Constants.PORT, s)));
        server.start();
        ClientWrap client = new ClientWrap(()->sm, sysmon -> {
            try {
                //TODO Change camera ID to be variable
                return spy(new Camera(sysmon, Constants.HOST, Constants.PORT, 0));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (ConnectException e) {
                e.printStackTrace();
            }
            throw new Error("Camera creation failed");
        });
        client.start();
        Thread.sleep(200);
        GUIMain gui = spy(new GUIMain(Constants.GUI_TITLE, sm));
        sm.addObserver(gui);
        anim.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO: Check if connection was successful and if the proper data was transmitted

        client.cancel();
        server.cancel();

        // GUI's update method should have been called
        verify(gui, atLeastOnce()).update(any(), any());

        // This does the actual drawing, and should have been called by swing

        for(CameraControl c: gui.cams.values()) {
//            verify(c, atLeastOnce()).render();
            assertTrue(c.icon.getIconHeight() > 100);
            assertTrue(c.icon.getIconWidth() > 100);
        }
    }

}

