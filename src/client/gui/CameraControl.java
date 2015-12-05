package client.gui;

import client.GUIUpdate;
import client.SystemMonitor;
import client.camera.ImageFrame;
import common.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by von on 2015-11-08.
 */
public class CameraControl extends JPanel implements Observer {
    public ImageIcon icon;
    public JPanel panel;
    public JLabel imagePlacement;
    public JLabel delay;
    public JLabel motion;
    private SystemMonitor system;
    private int cameraId;
    private int nbrCameras;
    private long dt;
    private ImageFrame currentlyShownImage;

    public CameraControl(SystemMonitor system, int cameraId) {

        super();
        panel = new JPanel();

        motion = new JLabel("<No Motion Detected>");
        delay = new JLabel("Network Delay: 0ms");
        nbrCameras = system.getNrCameras();


        motion.setFont(new Font("Arial", Font.BOLD, 20));
        motion.setForeground(new Color(0, 255, 0));

        panel.setLayout(new BorderLayout());

        this.system = system;
        this.cameraId = cameraId;
        icon = new ImageIcon();
        imagePlacement = new JLabel(icon);
        setLayout(new BorderLayout());
        JPanel information = new JPanel();
        information.setLayout(new FlowLayout());
        delay.setAlignmentX(SwingConstants.NORTH);
        motion.setAlignmentX(SwingConstants.SOUTH);
        information.add(delay, BorderLayout.NORTH);
        information.add(motion, BorderLayout.SOUTH);
        panel.add(imagePlacement, BorderLayout.CENTER);
        panel.add(information, BorderLayout.NORTH);
//        panel.add(motion, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
//        setMinimumSize(new Dimension(640, 480));
//        setSize(new Dimension(640, 480));
//        setMaximumSize(new Dimension(640, 480));
//
//        setPreferredSize(new Dimension(640, 480));
    }
    private Dimension calcImgSize(){
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        if(nbrCameras == 1){

//            int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200;
//            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-200);
            return  null;
        }
        double widthScalar =(nbrCameras+1)/2;

        int width = (int) (screenWidth/widthScalar);

        double screenHeight = (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-200);
        double heightScalar = (nbrCameras < 2? 1 : 2);
        int height = (int) (screenHeight/heightScalar);


        return new Dimension(width, height);
    }

    public void displayImage(byte[] image) {
//        LogUtil.info("Byte Array contains: " + Arrays.toString(image));
        long before = System.currentTimeMillis();
        Image img = getToolkit().createImage(image);
//        LogUtil.info("Time for createImage: " + (System.currentTimeMillis()-before));

        before = System.currentTimeMillis();
//        LogUtil.info("Rendering Icon: " + image.length);
        getToolkit().prepareImage(img, -1, -1, null);
//        LogUtil.info("Time for prepareImage: " + (System.currentTimeMillis()-before));
//        Dimension imgsize = calcImgSize();

//        img = (calcImgSize() == null) ? img : img.getScaledInstance((int)imgsize.getWidth(),  (int)imgsize.getHeight(),Image.SCALE_DEFAULT);
//        System.out.println(getWidth() + "X" + getHeight());
         before = System.currentTimeMillis();
        icon.setImage(img);
//        LogUtil.info("Time for setImage: " + (System.currentTimeMillis()-before));
        // icon.paintIcon(this, this.getGraphics(), 100, 0);
//        revalidate();
        before = System.currentTimeMillis();

        repaint();
//        LogUtil.info("Time for repaint: " + (System.currentTimeMillis()-before));

    }

    public void displayDelay(long delay) {
        this.delay.setText("Network Delay: "+Long.toString((System.currentTimeMillis()-delay))+"ms" );

    }

    public void update(Observable observable, Object o) {

        if ((GUIUpdate) o == GUIUpdate.FrameUpdate ) {
            ImageFrame toBeShown = system.getDisplayFrame(cameraId);
          if(currentlyShownImage != toBeShown){

//            System.out.println("Rendering Icon");
//            renderImage();
            SwingUtilities.invokeLater(this::renderImage);
        }
        }
    }


    public void renderImage() {
//        LogUtil.info("Entering renderimage!");
        long before = System.currentTimeMillis();

        synchronized (system) {

            ImageFrame frame = system.getDisplayFrame(cameraId);
            currentlyShownImage = frame;
            if (frame.getFrame().getFrameAsBytes() != null) {
//                if(System.currentTimeMillis()-dt > 50){
//                    dt = System.currentTimeMillis();
                displayImage(frame.getFrame().getFrameAsBytes());

                displayMotion(frame);
                displayDelay(frame.getFrame().timestamp);
            }
        }
        LogUtil.info("Time for rendering: " + (System.currentTimeMillis()-before));
//        }
    }

    private void displayMotion(ImageFrame frame) {



            if (frame.getFrame().motionDetected) {
                motion.setForeground(new Color(255,0,0));
                motion.setText("<Motion Detected>");
            }
            else {
                motion.setForeground(new Color(0,255,0));
                motion.setText("<No Motion Detected>");
            }
        }
    }


