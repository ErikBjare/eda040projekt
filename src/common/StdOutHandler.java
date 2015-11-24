package common;

import java.io.OutputStream;
import java.util.Formatter;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Created by von on 2015-11-24.
 */
public class StdOutHandler extends StreamHandler {
    public StdOutHandler() {
        super(System.out, new SimpleFormatter());
    }
}
