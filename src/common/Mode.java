package common;

/**
 * Created by von on 2015-11-08.
 */
public class Mode {
    public static final int Auto = 0;
    public static final int Idle = 1;
    public static final int Movie = 2;
    public static final int ForceIdle = 3;
    public static final int ForceMovie = 4;

    public static String toString(int mode) {
        switch (mode){
            case Mode.Auto:
                return "Auto";
            case Mode.Idle:
                return "Idle";
            case Mode.Movie:
                return "Movie";
            case Mode.ForceIdle:
                return "ForceIdle";
            case Mode.ForceMovie:
                return "ForceMovie";
            default:
                return Integer.toString(mode);
        }
    }
}
