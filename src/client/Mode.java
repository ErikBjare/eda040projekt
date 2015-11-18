package client;

/**
 * Created by von on 2015-11-08.
 */
public enum Mode {
    Auto, Idle, Movie, ForceIdle, ForceMovie;

    public static Mode fromInteger(int i) {
        return values()[i];
    }
}
