package common;

/**
 * Created by von on 2015-11-24.
 */
public class LogUtil {
    protected static final String format = "%-10s %-20s %s\n";
    public static void exception(String message, Exception e){
        logErr(Thread.currentThread().getName(), "EXCEPTION", message + ": " + e.getMessage());
    }
    public static void exception(Exception e){
        logErr(Thread.currentThread().getName(), "EXCEPTION", e.getMessage());
    }
    public static void info(String message){
        log(Thread.currentThread().getName(), "INFO", message);
    }
    public static void log(String thread, String level, String message){
        System.out.printf(format, level, thread, message);
    }
    public static void logErr(String thread, String level, String message){
        System.err.printf(format, level, thread, message);
    }
}
