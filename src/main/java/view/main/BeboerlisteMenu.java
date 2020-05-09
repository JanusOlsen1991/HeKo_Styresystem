package view.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Collections;

public class BeboerlisteMenu implements IParentTable{

    TableView<Beboer> tView1;
    TableView<Beboer> tView2;
    TableView<Beboer> tView3;
    TableView<Beboer> tView4;
    TableView<Beboer> tView5;
    TableView<Beboer> tView6;

    private final GuiSingleton gui;


    static GUI_PopUps popUp = new GUI_PopUps();


    public BeboerlisteMenu() {
        this.gui = GuiSingleton.getInstance();
    }

    @SuppressWarnings("unchecked")
    public void beboerlisteMenu(Stage primaryStage) {


        BorderPane borderP = new BorderPane();

        Button tilbageButton = new Button("Tilbage");
        tilbageButton.setOnAction(e -> {
            try {
                gui.hovedMenu.hovedMenu(primaryStage);
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        });

        VBox vb = new VBox(tilbageButton);
        borderP.setLeft(vb);

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

        tView1 = createTableView('1');
        tView2 = createTableView('2');
        tView3 = createTableView('3');
        tView4 = createTableView('4');
        tView5 = createTableView('5');
        tView6 = createTableView('6');

        Button bb1 = new Button("Kom i gang");
        bb1.setOnAction(e -> {
            popUp.opretNyBeboeroplysninger(this);
        });

        tView1.setPlaceholder(bb1);


        TextField filter = new TextField();
        filter.setPromptText("Søg...");
        filter.setPrefWidth(150);
        filter.setMaxWidth(150);

/* //den her fejler i hovedmenuen
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

 */
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
        ArrayList<Beboer> alleBeboere = gui.ec.getBeboere();
        Collections.sort(alleBeboere);
        if (salnummer == '1') {
            return FXCollections.observableArrayList(alleBeboere);
        }

        ArrayList<Beboer> temp = new ArrayList<Beboer>();
        for (Beboer b : alleBeboere) {
                char sal = b.getVærelse().charAt(0);
                if (sal == salnummer) {
                    temp.add(b);
            }
        }
        return FXCollections.observableArrayList(temp);
    }

    private TableView<Beboer> createTableView(char salNummer) {
        TableView<Beboer> tableView = new TableView();

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

        tableView.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, telefonColumn, uddStedColumn,
                uddannelseColumn, påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn);
        værelseColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(værelseColumn);

        tableView.setItems(getBeboere(salNummer));

        tableView.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    popUp.redigerBeboeroplysninger(clickedRow, tableView, false);

                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyBeboeroplysninger(this);
                }
            });
            return row;
        });

        return tableView;
    }

    @Override
    public void update(Beboer b, String værelse) {
        tView1.getItems().add(b);
        tView1.refresh();

        char c = værelse.charAt(0);
        switch (c) {
            case '2':
                tView2.getItems().add(b);
                tView2.refresh();
                break;
            case '3':
                tView3.getItems().add(b);
                tView3.refresh();
                break;
            case '4':
                tView4.getItems().add(b);
                tView4.refresh();
                break;
            case '5':
                tView5.getItems().add(b);
                tView5.refresh();
                break;
            case '6':
                tView6.getItems().add(b);
                tView6.refresh();
                break;
            default:
                System.out.println("Værelset findes ikke");
        }
    }
}
