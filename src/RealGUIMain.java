import client.gui.GUIWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tank on 12/5/2015.
 */
public class RealGUIMain {

    /**
     * Starts the GUI, connecting to the real cameras.
     * @param args The list of argus ids to connect to (
     *             Giving 0 arguments defaults to argus 1-4.
     */
    public static void main(String[] args) {
        if (args.length == 0) args = new String[]{"1", "2", "3", "4"};

        List<String> guiArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            guiArgs.add("argus-"+args[i]+".student.lth.se");
            guiArgs.add("9191");
        }
        GUIWindow.main(guiArgs.toArray(new String[]{}));
    }
}
