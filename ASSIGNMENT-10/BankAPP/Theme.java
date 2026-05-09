import javax.swing.*;
import java.awt.*;

class Theme {

    static Color bg = new Color(30,30,30);
    static Color fg = new Color(220,220,220);
    static Color btn = new Color(50,50,50);

    static void apply(JFrame f) {
        f.getContentPane().setBackground(bg);
    }

    static JButton darkButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(btn);
        b.setForeground(fg);
        return b;
    }
}