import client.gui.GUIWindow;
import common.Constants;
import server.ProxyAxis;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Tank on 12/2/2015.
 */
public class ProxyGUIMain {

    /**
     * Starts both the GUI and proxy servers
     * @param args The list of argus ids to connect to (
     *             Giving 0 arguments defaults to argus 1-4.
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) args = new String[]{"1", "2", "3", "4"};
        final String[] finalArgs = args;
        for (int i = 0; i < args.length; i++) {
            final int finalI = i;
            Thread t = new Thread(()->server.Main.start(Constants.PROXY_PORT_START + finalI, new ProxyAxis("argus-"+ finalArgs[finalI]+".student.lth.se", Constants.CAMERA_PORT)));
            t.start();
        }
        Thread.sleep(1000);
        String[] hosts = IntStream.iterate(Constants.PROXY_PORT_START, x->x+1)
                .limit(args.length)
                .mapToObj(i -> "localhost " + i)
                .collect(Collectors.joining(" "))
                .split(" ");
        GUIWindow.main(hosts);
    }
}
