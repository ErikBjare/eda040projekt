/**
 * Created by Tank on 12/2/2015.
 */
public class Main {
    public static void main(String[] args) {
        String[] s = new String[]{"5656", "argus-3.student.lth.se", "9191" };
        String[] s1 = new String[]{"5657", "argus-2.student.lth.se", "9191" };
        String[] s3 = new String[]{"5658", "argus-4.student.lth.se", "9191" };
        String[] s4 = new String[]{"5659", "argus-5.student.lth.se", "9191" };
        String[] s5 = new String[]{"5660", "argus-6.student.lth.se", "9191" };
        String[] s6 = new String[]{"5661", "argus-7.student.lth.se", "9191" };
        Thread t3 = new Thread(()->server.Main.main(s3));
        Thread t4 = new Thread(()->server.Main.main(s4));
        Thread t1 = new Thread(()->server.Main.main(s));
        Thread t2 = new Thread(()->server.Main.main(s1));
        Thread t5 = new Thread(()->server.Main.main(s5));
        Thread t6 = new Thread(()->server.Main.main(s6));
        t5.start();
        t6.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        client.gui.GUIMain.main(new String[]{});
    }
}
