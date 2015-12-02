/**
 * Created by Tank on 12/2/2015.
 */
public class Main {
    public static void main(String[] args) {
        String[] s = new String[]{"5656", "argus-1.student.lth.se", "9191" };
        String[] s1 = new String[]{"5657", "argus-2.student.lth.se", "9191" };
        new Thread(()->server.Main.main(s)).start();
        new Thread(()->server.Main.main(s1)).start();
//        new Thread(()->client.gui.GUIMain.main());
    }
}
