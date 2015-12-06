import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Tank on 12/5/2015.
 */
public class MainGuiStart {
    public static void main(String[] args) {

        String[] hosts = IntStream.iterate(5657, i -> i + 1)
                .mapToObj(i->"localhost "+i)
                .limit(2)
                .collect(Collectors.joining(" "))
                .split(" ");
        client.gui.GUIMain.main(hosts);
    }
}
