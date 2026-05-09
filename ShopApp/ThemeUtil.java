import javax.swing.*;
import java.awt.*;

public class ThemeUtil {

    static Color BG = new Color(245,245,245);
    static Color PRIMARY = new Color(0,120,215);

    public static void applyTheme(JFrame f){
        apply(f.getContentPane());
    }

    static void apply(Component c){

        if(c instanceof JPanel){
            c.setBackground(BG);
        }

        if(c instanceof JButton){
            c.setBackground(PRIMARY);
            c.setForeground(Color.WHITE);
            ((JButton)c).setFocusPainted(false);
        }

        if(c instanceof Container){
            for(Component child : ((Container)c).getComponents()){
                apply(child);
            }
        }
    }
}