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

    public void addFrame(NewFrame frame){
        frames.add(frame);


    }

//    public byte[] getFirstFrame(){
//        return frames.get(0).getFrame();
//    }
    public byte[] removeFirstFrame(){
        if(frames.size() < 1) return null;
        return frames.remove(0).getFrame();
    }
    public byte[] getFrame(int i){
        if(frames.size() < 1) return null;
        return frames.get(i).getFrame();
    }
    public long getNextTime(){
        if(frames.size() < 1) return -1;
        return frames.get(0).getTimestamp();
    }

}
