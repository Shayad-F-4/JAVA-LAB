import PAC2.App;
import PAC2.SupperBase;

public class MainApp {
    public static void main(String[] args) {
        SupperBase sb = new SupperBase();
        sb.accessingSchemaInSupperBase();

        App app = new App();
        app.runFrontend();
    }
}
