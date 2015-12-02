package client.camera;

import common.protocol.NewFrame;

/**
 * Created by Tank on 12/2/2015.
 */
public class ImageFrame {
    private int camera;
    private NewFrame frame;

    public ImageFrame(int camera, NewFrame frame){
        this.camera = camera;
        this.frame = frame;
    }

    public int getCamera() {
        return camera;
    }
    public NewFrame getFrame(){
        return frame;
    }
}
