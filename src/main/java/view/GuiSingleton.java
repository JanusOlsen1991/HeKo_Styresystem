package view;

import controller.ExcelConnection;
import model.Værelsesudlejning;
import view.main.*;

public class GuiSingleton {

    private static GuiSingleton instance = new GuiSingleton();

    public static BeboerlisteMenu beboerlisteMenu = new BeboerlisteMenu();
    public static Hovedmenu hovedmenu = new Hovedmenu();
    public static GUI gui= new GUI();
    public static ExcelConnection ec;
    public static VærelsesUdlejningsMenu værelsesudlejningsmenu = new VærelsesUdlejningsMenu();
    public static StudiekontrolMenu studiekontrolMenu = new StudiekontrolMenu();

    private GuiSingleton(){}

    public static GuiSingleton getInstance() {
        return instance;
    }

}
