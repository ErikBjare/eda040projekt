/**
 * Created by Tank on 12/2/2015.
 */
public class Main {
    public static void main(String[] args) {
        String[] s = new String[]{"5656", "argus-3.student.lth.se", "9191" };
        String[] s1 = new String[]{"5657", "argus-2.student.lth.se", "9191" };
        Thread t1 = new Thread(()->server.Main.main(s));
        Thread t2 = new Thread(()->server.Main.main(s1));
        t1.start();
        t2.start();
        client.gui.GUIMain.main(new String[]{});
    }
}
