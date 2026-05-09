import javax.swing.UIManager;

public class MainApp {
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        DBConnection.initializeDatabase();
        new LoginFrame();
    }
}