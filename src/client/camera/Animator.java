package client.camera;

/**
 * Created by von on 2015-11-08.
 */
public class Animator extends Thread {
    protected System system;
    protected Camera camera;

    public Animator(System system, Camera camera) {
        this.system = system;
        this.camera = camera;
    }
}
