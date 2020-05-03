package view.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Beboer;
import model.Studiekontrol;
import model.Studiekontrolstatus;
import view.GuiSingleton;
import view.popups.GUI_PopUps;

import java.time.LocalDate;
import java.util.ArrayList;

public class StudiekontrolMenu {
    static GUI_PopUps popUp = new GUI_PopUps();
    static GuiSingleton gui;



    @SuppressWarnings("unchecked")
    public void studieKontrolMenu(Stage primaryStage) {
        // overordnet layout genereres
        BorderPane borderP = new BorderPane();
        // venstre side
        Button tilbageButton = new Button("Tilbage");
        tilbageButton.setOnAction(e -> {
            try {
                gui.hovedmenu.hovedMenu(primaryStage);
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        });

        VBox vb = new VBox(tilbageButton);
        borderP.setLeft(vb);

        // Midten
        TabPane tP = new TabPane();
        borderP.setCenter(tP);

        TableView<Beboer> tView1 = new TableView<Beboer>();
        tView1.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView1, false);

                }
            });
            return row;
        });
        // kolloner til Tableviews
        TableColumn<Beboer, String> værelseColumn = new TableColumn<Beboer, String>("Værelse");
        værelseColumn.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn = new TableColumn<Beboer, String>("Navn");
        navnColumn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> uddRetningColumn = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddRetningColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, String> uddStedColumn = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb");
        lejeaftalensUdløbColumn.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));
        TableColumn<Beboer, String> studieKStatColumn = new TableColumn<Beboer, String>("Status på studiekontrol");
        studieKStatColumn.setCellValueFactory(new PropertyValueFactory<>("statusPåStudiekontrol"));

        // TableViews oprettes med kollonnerne
        tView1.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, uddRetningColumn, uddStedColumn,
                påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn, studieKStatColumn);
        tView1.getItems().addAll(getAlleStudiekontroller());

        Button opretButton = new Button("Start ny studiekontrol");
        opretButton.setOnAction(event -> {
            // Tab t =
            // opretStudiekontrolTab(ec.getStudiekontroller().get(ec.getStudiekontroller().size()));
            popUp.startStudiekontrol(tView1, gui.ec, tP);

        });

        VBox firstTabLayout = new VBox(tView1, opretButton);

        Tab tab1 = new Tab("Alle igangværende studiekontroller");
        tab1.setClosable(false);

        tab1.setContent(firstTabLayout);// TODO layout ikke kun tView1

        tP.getTabs().add(tab1);
        // Tabs herunder skal oprettes KUN "når de er i gang".
        ArrayList<Studiekontrol> list = gui.ec.getStudiekontroller();

        if (gui.ec.getStudiekontroller().size() > 0)// TODO - Af en eller anden grund overskriver loopet også indholdet i
            // det første tableview
            for (Studiekontrol sk : list) {
                Tab t = opretStudiekontrolTab(sk);
                // Tilføjer tabs til tabPane
                tP.getTabs().add(t);

            }
        Scene scene = gui.gui.getScene();
        scene = new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);

    }

    public static Tab opretStudiekontrolTab(Studiekontrol sk) {
        String s = gui.ec.findMånedsNavn(sk.getMånedsnummer());
        Tab t = new Tab(s);
        t.setClosable(false);

        Label skID = new Label(sk.getStudiekontrolID());
        Label skSTART = new Label(sk.getPåbegyndelsesdato().toString());
        Label skSLUT = new Label(sk.getAfleveringsfrist().toString());

        Label l1 = new Label("Studiekontrol ID");
        Label l2 = new Label("påbegyndt d. ");
        Label l3 = new Label("Afsluttes d. ");

        TableColumn<Beboer, String> værelseColumn1 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn1.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn1 = new TableColumn<Beboer, String>("Navn");
        navnColumn1.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn1 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn1.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> uddRetningColumn1 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddRetningColumn1.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, String> uddStedColumn1 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn1.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn1 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn1.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn1 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn1.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn1 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb");
        lejeaftalensUdløbColumn1.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));
        TableColumn<Beboer, String> studieKStatColumn1 = new TableColumn<Beboer, String>();
        studieKStatColumn1.setCellValueFactory(new PropertyValueFactory<>("statusPåStudiekontrol"));

        TableView<Beboer> tView = new TableView<Beboer>();

        // Dobbelt klik
        tView.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView, false);
                    tView.refresh(); // nødvendig?
                }
            });
            return row;
        });

        // Henter beboere fra studiekontrollen
        ObservableList<Beboer> beboereTilMåned = FXCollections.observableArrayList(sk.getBeboere());

        tView.getItems().addAll(beboereTilMåned);

        tView.getColumns().addAll(værelseColumn1, navnColumn1, indflytningColumn1, uddRetningColumn1, uddStedColumn1,
                påbegyndtUddColumn1, afslutningUddColumn1, lejeaftalensUdløbColumn1, studieKStatColumn1);

        Button bAfslut = new Button("Afslut studiekontrollen");
        bAfslut.setOnAction(event -> {
            popUp.afslutStudiekontrol(sk, gui.ec, tView, t);
        });

        GridPane gp = new GridPane();
        gp.add(l1, 3, 3);
        gp.add(l2, 6, 3);
        gp.add(l3, 9, 3);

        gp.add(skID, 3, 5);
        gp.add(skSTART, 6, 5);
        gp.add(skSLUT, 9, 5);
        gp.add(bAfslut, 3, 30, 3, 1);

        gp.add(tView, 1, 12, 10, 10);

        t.setContent(gp);
        return t;
    }

    private ObservableList<Beboer> getAlleStudiekontroller() {
        ArrayList<Beboer> alleBeboere = gui.ec.getBeboere();
        ArrayList<Beboer> temp = new ArrayList<Beboer>();
        for (Beboer b : alleBeboere) {
            if (b.getStudiekontrolstatus() != Studiekontrolstatus.IKKEIGANG)
                if (b.getStudiekontrolstatus() != Studiekontrolstatus.GODKENDT)
                    if (b.getStudiekontrolstatus() != Studiekontrolstatus.SENDTTILBOLIGSELSKAB)
                        temp.add(b);
        }
        ObservableList<Beboer> alleStudiekontroller = FXCollections.observableArrayList(temp);
        return alleStudiekontroller;
    }
}
