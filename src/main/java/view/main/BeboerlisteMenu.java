package view.main;

import controller.ExcelConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Beboer;
import view.GuiSingleton;
import view.popups.GUI_PopUps;

import java.time.LocalDate;
import java.util.ArrayList;

public class BeboerlisteMenu {

    private final GuiSingleton gui;


    static GUI_PopUps popUp = new GUI_PopUps();


    public BeboerlisteMenu() {
        this.gui = GuiSingleton.getInstance();
    }

    @SuppressWarnings("unchecked")
    public void beboerlisteMenu(Stage primaryStage) {

        // overordnet layout genereres
        BorderPane borderP = new BorderPane();
        // venstre side
        Button tilbageButton = new Button("Tilbage");
        tilbageButton.setOnAction(e -> {
            try {
                gui.gui.hovedMenu(primaryStage);
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        });

        VBox vb = new VBox(tilbageButton);
        borderP.setLeft(vb);

        // Midten
        TabPane tP = new TabPane();
        borderP.setCenter(tP);
        Tab tab1 = new Tab("Alle beboere");
        tab1.setClosable(false);
        Tab tab2 = new Tab("2. sal");
        tab2.setClosable(false);
        Tab tab3 = new Tab("3. sal");
        tab3.setClosable(false);
        Tab tab4 = new Tab("4. sal");
        tab4.setClosable(false);
        Tab tab5 = new Tab("5. sal");
        tab5.setClosable(false);
        Tab tab6 = new Tab("6. sal");
        tab6.setClosable(false);

        TableView<Beboer> tView1 = new TableView<Beboer>();
        TableView<Beboer> tView2 = new TableView<Beboer>();
        TableView<Beboer> tView3 = new TableView<Beboer>();
        TableView<Beboer> tView4 = new TableView<Beboer>();
        TableView<Beboer> tView5 = new TableView<Beboer>();
        TableView<Beboer> tView6 = new TableView<Beboer>();

        // Herunder oprettes en metode til at håndtere dobbeltklik på eksisterende
        // beboere

        tView1.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView1, false);

                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        // Start knappen
        Button bb1 = new Button("Kom i gang");
        bb1.setOnAction(e -> {
            popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
        });

        tView1.setPlaceholder(bb1);

        tView2.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView2, false);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        tView3.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView3, false);

                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        tView4.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView4, false);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        tView5.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView5, false);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        tView6.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView6, false);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(gui.ec, tView1, tView2, tView3, tView4, tView5, tView6);
                }
            });
            return row;
        });

        // kollonner til Tableviews //ALLE
        TableColumn<Beboer, String> værelseColumn = new TableColumn<Beboer, String>("Værelse");
        værelseColumn.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn = new TableColumn<Beboer, String>("Navn");
        navnColumn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb");
        lejeaftalensUdløbColumn.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));

        // 2. sal
        TableColumn<Beboer, String> værelseColumn2 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn2.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn2 = new TableColumn<Beboer, String>("Navn");
        navnColumn2.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn2 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn2.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn2 = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn2.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn2 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn2.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn2 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn2.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn2 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn2.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn2 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn2.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn2 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb"); // SLET? - Måske for forvirrende,
        lejeaftalensUdløbColumn2.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));

        // 3. sal
        TableColumn<Beboer, String> værelseColumn3 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn3.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn3 = new TableColumn<Beboer, String>("Navn");
        navnColumn3.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn3 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn3.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn3 = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn3.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn3 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn3.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn3 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn3.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn3 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn3.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn3 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn3.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn3 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb"); // SLET? - Måske for forvirrende,
        lejeaftalensUdløbColumn3.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));

        // 4. sal
        TableColumn<Beboer, String> værelseColumn4 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn4.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn4 = new TableColumn<Beboer, String>("Navn");
        navnColumn4.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn4 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn4.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn4 = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn4.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn4 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn4.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn4 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn4.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn4 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn4.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn4 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn4.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn4 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb"); // SLET? - Måske for forvirrende,
        lejeaftalensUdløbColumn4.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));

        // 5. sal
        TableColumn<Beboer, String> værelseColumn5 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn5.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn5 = new TableColumn<Beboer, String>("Navn");
        navnColumn5.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn5 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn5.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn5 = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn5.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn5 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn5.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn5 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn5.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn5 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn5.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn5 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn5.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn5 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb");
        lejeaftalensUdløbColumn5.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));

        TableColumn<Beboer, String> værelseColumn6 = new TableColumn<Beboer, String>("Værelse");
        værelseColumn6.setCellValueFactory(new PropertyValueFactory<>("værelse"));
        TableColumn<Beboer, String> navnColumn6 = new TableColumn<Beboer, String>("Navn");
        navnColumn6.setCellValueFactory(new PropertyValueFactory<>("navn"));
        TableColumn<Beboer, LocalDate> indflytningColumn6 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
        indflytningColumn6.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Beboer, String> telefonColumn6 = new TableColumn<Beboer, String>("Telefonnummer");
        telefonColumn6.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));
        TableColumn<Beboer, String> uddStedColumn6 = new TableColumn<Beboer, String>("Uddannelsessted");
        uddStedColumn6.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
        TableColumn<Beboer, String> uddannelseColumn6 = new TableColumn<Beboer, String>("Uddannelsesretning");
        uddannelseColumn6.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
        TableColumn<Beboer, LocalDate> påbegyndtUddColumn6 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
        påbegyndtUddColumn6.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
        TableColumn<Beboer, LocalDate> afslutningUddColumn6 = new TableColumn<Beboer, LocalDate>(
                "Uddannelse forventes afsluttet");
        afslutningUddColumn6.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
        TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn6 = new TableColumn<Beboer, LocalDate>(
                "Lejeaftalens udløb"); // SLET? - Måske for forvirrende,
        lejeaftalensUdløbColumn6.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));
        // TableViews oprettes med kollonnerne

        tView1.setItems(getBeboere('1'));
        tView2.setItems(getBeboere('2'));
        tView3.setItems(getBeboere('3'));
        tView4.setItems(getBeboere('4'));
        tView5.setItems(getBeboere('5'));
        tView6.setItems(getBeboere('6'));

        tView1.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, telefonColumn, uddStedColumn,
                uddannelseColumn, påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView1.getSortOrder().add(værelseColumn);


        tView2.getColumns().addAll(værelseColumn2, navnColumn2, indflytningColumn2, telefonColumn2, uddStedColumn2,
                uddannelseColumn2, påbegyndtUddColumn2, afslutningUddColumn2, lejeaftalensUdløbColumn2);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView2.getSortOrder().add(værelseColumn);

        tView3.getColumns().addAll(værelseColumn3, navnColumn3, indflytningColumn3, telefonColumn3, uddStedColumn3,
                uddannelseColumn3, påbegyndtUddColumn3, afslutningUddColumn3, lejeaftalensUdløbColumn3);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView3.getSortOrder().add(værelseColumn);

        tView4.getColumns().addAll(værelseColumn4, navnColumn4, indflytningColumn4, telefonColumn4, uddStedColumn4,
                uddannelseColumn4, påbegyndtUddColumn4, afslutningUddColumn4, lejeaftalensUdløbColumn4);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView4.getSortOrder().add(værelseColumn);

        tView5.getColumns().addAll(værelseColumn5, navnColumn5, indflytningColumn5, telefonColumn5, uddStedColumn5,
                uddannelseColumn5, påbegyndtUddColumn5, afslutningUddColumn5, lejeaftalensUdløbColumn5);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView5.getSortOrder().add(værelseColumn);

        tView6.getColumns().addAll(værelseColumn6, navnColumn6, indflytningColumn6, telefonColumn6, uddStedColumn6,
                uddannelseColumn6, påbegyndtUddColumn6, afslutningUddColumn6, lejeaftalensUdløbColumn6);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView6.getSortOrder().add(værelseColumn);

        // Første tab: Alle beboere
//TODO - HER SKAL DU LAVE LAYOUTS MED TEXTFIELDS TIL AT SORTERE


        TextField filter = new TextField();
        filter.setPromptText("Søg...");
        filter.setPrefWidth(150);
        filter.setMaxWidth(150);


        //SORTER DATA I TABLEVIEW
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Beboer> filteredData = new FilteredList<>(getBeboere('1'), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(beboer -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (beboer.getVærelse().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (beboer.getNavn().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }else if (beboer.getTelefonnummer().contains(lowerCaseFilter)){
                    return true;
                }else if (beboer.getUddannelsesretning().contains(lowerCaseFilter)){
                    return true;
                }else if (beboer.getUddannelsessted().contains(lowerCaseFilter)){
                    return true;


                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Beboer> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tView1.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tView1.setItems(sortedData);
        tView1.setMinHeight(500);
        VBox vb1 = new VBox(tView1,filter);
        tab1.setContent(vb1);
        tab2.setContent(tView2);
        tab3.setContent(tView3);
        tab4.setContent(tView4);
        tab5.setContent(tView5);
        tab6.setContent(tView6);

        tP.getTabs().addAll(tab1, tab2, tab3, tab4, tab5, tab6);
        Scene scene = gui.gui.getScene();
        scene =  new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
    }
    /**
     *
     * @param salnummer
     *            - Character der gives for at finde salsnummer. angives '1' tages
     *            alle beboere
     * @return
     */
    private ObservableList<Beboer> getBeboere(char salnummer) {
        // Der skal lægges ind og testes for 'isKlaret'
        ArrayList<Beboer> alleBeboere = gui.ec.getBeboere();
        ArrayList<Beboer> temp = new ArrayList<Beboer>();
        if (salnummer != '1') {
            for (Beboer b : alleBeboere) {
                char sal = b.getVærelse().charAt(0);
                if (sal == salnummer) {
                    temp.add(b);
                }

            }
            ObservableList<Beboer> beboere = FXCollections.observableArrayList(temp);
            return beboere;
        } else {
            ObservableList<Beboer> beboere = FXCollections.observableArrayList(alleBeboere);
            return beboere;
        }
    }

}
