import javax.swing.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Tank on 12/5/2015.
 */
public class MainGuiStart {
    public static void main(String[] args) {

        int nbrCameras = 0;
        while(nbrCameras < 1 || nbrCameras > 8){
            String s  = JOptionPane.showInputDialog(null, "How many cameras do you want to use? (Input a number 1 and 8)");
//            System.out.println(s);
            try{
                if(s == null) System.exit(0);
                nbrCameras = Integer.parseInt(s);
                if(nbrCameras < 1 || nbrCameras > 8){
                  JOptionPane.showMessageDialog(null, "You must enter a number between 1 and 8");

                }
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "You must enter a number between 1 and 8");
            }
        }


        String[] hosts = IntStream.iterate(5656, i -> i + 1)
                .mapToObj(i->"localhost "+i)
                .limit(nbrCameras)
                .collect(Collectors.joining(" "))
                .split(" ");
        client.gui.GUIMain.main(hosts);
    }
}
