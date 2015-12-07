import server.AxisWrapper;
import server.ProxyAxis;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Tank on 12/2/2015.
 */
public class Main {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->server.Main.start(5656, new ProxyAxis("argus-1.student.lth.se", 9191)));
        t1.start();
        Thread t2 = new Thread(()->server.Main.start(5657, new ProxyAxis("argus-2.student.lth.se", 9191)));
        t2.start();
        Thread t3 = new Thread(()->server.Main.start(5658, new ProxyAxis("argus-3.student.lth.se", 9191)));
        t3.start();
        Thread t4 = new Thread(()->server.Main.start(5659, new ProxyAxis("argus-4.student.lth.se", 9191)));
        t4.start();
        Thread t5 = new Thread(()->server.Main.start(5660, new ProxyAxis("argus-5.student.lth.se", 9191)));
        t5.start();
        Thread t6 = new Thread(()->server.Main.start(5661, new ProxyAxis("argus-6.student.lth.se", 9191)));
        t6.start();
        Thread t7 = new Thread(()->server.Main.start(5662, new ProxyAxis("argus-7.student.lth.se", 9191)));
        t7.start();
        Thread t8 = new Thread(()->server.Main.start(5663, new ProxyAxis("argus-8.student.lth.se", 9191)));
        t8.start();


        String[] hosts = IntStream.iterate(5656, i->i+1)
                .mapToObj(i->"localhost "+i)
                .limit(4)
                .collect(Collectors.joining(" "))
                .split(" ");
        client.gui.GUIMain.main(hosts);
    }
}
