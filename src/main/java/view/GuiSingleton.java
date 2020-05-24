package view;

import controller.Filplaceringer;
import controller.excel.ExcelConnection;
import controller.word.WordConnection;
import view.main.*;

public class GuiSingleton {

    private static GuiSingleton instance = new GuiSingleton();
    public static HovedMenu hovedMenu = new HovedMenu();
    public static DispensationsMenu dispensationsMenu = new DispensationsMenu();
    public static BeboerlisteMenu beboerlisteMenu = new BeboerlisteMenu();
    public static FremlejeMenu fremlejeMenu = new FremlejeMenu();
    public static GUI gui= new GUI();
    public static ExcelConnection ec;
    public static VærelsesUdlejningsMenu værelsesudlejningsmenu = new VærelsesUdlejningsMenu();
    public static StudiekontrolMenu studiekontrolMenu = new StudiekontrolMenu();
    public static Filplaceringer filplaceringer = Filplaceringer.getInstance();

    public String base = "Base.docx";
    public String excel = "excel";
    public String studiekontrolSeddel = "Studiekontrolsblanket.docx";
    public String studieKontrolFølgeSeddel = "Følgeseddel.docx";
    public String skabeloner = "skabeloner";


    private GuiSingleton(){
    }

    public static GuiSingleton getInstance() {
        return instance;
    }

}
