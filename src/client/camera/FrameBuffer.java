package client.camera;

import common.protocol.NewFrame;

import java.util.ArrayList;

/**
 * Created by von on 2015-11-05.
 */
public class FrameBuffer {

    private ArrayList<NewFrame> frames;

    public FrameBuffer(){

        this.frames = new ArrayList<NewFrame>();
    }

    public synchronized void addFrame(NewFrame frame){
        frames.add(frame);
        // System.out.println("Frame added , current size = " + frames.size());
    }

//    public byte[] getFirstFrame(){
//        return frames.get(0).getFrame();
//    }

    public NewFrame removeFirstFrame(){
        if(frames.size() < 1) return null;
        System.out.println("Frame removed, size = " + (frames.size()-1));
        System.out.println("The removed frame: " + frames.get(0).toString().substring(300, 350) + " timestamp: " + frames.get(0).timestamp);
        return frames.remove(0);
    }

    public NewFrame getFrame(int i){
        if(frames.size() < 1) return null;
        return frames.get(i);
    }
}
