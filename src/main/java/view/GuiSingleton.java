package view;

import controller.ExcelConnection;
import view.main.BeboerlisteMenu;
import view.main.GUI;
import view.main.Hovedmenu;

public class GuiSingleton {

    private static GuiSingleton instance = new GuiSingleton();

    public static BeboerlisteMenu beboerlisteMenu = new BeboerlisteMenu();
    public static Hovedmenu hovedmenu = new Hovedmenu();
    public static GUI gui= new GUI();
    public static ExcelConnection ec;

    private GuiSingleton(){}

    public static GuiSingleton getInstance() {
        return instance;
    }

}
