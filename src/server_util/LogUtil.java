package server_util;

/**
 * Simpler version of LogUtil for crosscompilation (printf is not available)
 */
public class LogUtil {
    protected static final String format = "%-10s %-20s %s\n";
    public static void exception(String message, Exception e){
        logErr(Thread.currentThread().getName(), "EXCEPTION", e.getMessage() + "(" +message+ ")\n");
        e.printStackTrace();
    }
    public static void exception(Exception e){
        logErr(Thread.currentThread().getName(), "EXCEPTION", e.getMessage()+"\n");
        e.printStackTrace();
    }
    public static void info(String message){
        log(Thread.currentThread().getName(), "INFO", message);
    }
    public static void log(String thread, String level, String message){
       System.out.println(message);
    }
    public static void logErr(String thread, String level, String message){
       System.err.println(message);
    }
}
