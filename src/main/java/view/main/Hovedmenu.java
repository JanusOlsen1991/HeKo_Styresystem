package view.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Deadline;
import view.GuiSingleton;
import view.popups.GUI_PopUps_Deadlines;

import java.time.LocalDate;
import java.util.ArrayList;

public class HovedMenu {
    GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();
    static GuiSingleton gui;

    public void hovedMenu(Stage primaryStage) {


        primaryStage.setTitle("Herlevkollegiets Indstillingsudvalg");
        VBox venstreLayout = new VBox(5);
        venstreLayout.setPadding(new Insets(30,20,0,10));


        GridPane højreLayout = new GridPane();
        højreLayout.setVgap(5);
        højreLayout.setHgap(10);
        HBox hb = new HBox(venstreLayout, højreLayout);

//		borderP.setLeft(venstreLayout);
//		borderP.setCenter(højreLayout);

        TableView<Deadline> tView = new TableView<Deadline>();
        // Buttons til venstre side af menuen
        Button beboerlisteButton = new Button("Beboerliste");
        beboerlisteButton.setOnAction(e -> gui.beboerlisteMenu.beboerlisteMenu(primaryStage));
        Button studieKontrolButton = new Button("Studiekontrol");
        studieKontrolButton.setOnAction(e -> gui.studiekontrolMenu.studieKontrolMenu(primaryStage));
        Button dispensationsButton = new Button("Dispensation");
        //dispensationsButton.setOnAction(e -> gui.dispensationsmenu.dispensationsMenu(primaryStage, tView));
        Button fremlejeButton = new Button("Fremleje");
        //fremlejeButton.setOnAction(e -> fremlejeMenu(primaryStage));
        Button værelsesudlejningsButton = new Button("Værelsesudlejning");
        værelsesudlejningsButton.setOnAction(e -> gui.værelsesudlejningsmenu.værelsesUdlejningMenu(primaryStage));

        Label l = new Label("Ingen nuværende deadlines");
        tView.setPlaceholder(l);
        tView.setMinWidth(1000);
        // Tilføjer buttons til venstre side.
        venstreLayout.getChildren().addAll(beboerlisteButton, studieKontrolButton, dispensationsButton, fremlejeButton,
                værelsesudlejningsButton);

        // Buttons og "Påmindelser/deadlines" til højre side af menuen

        TableColumn<Deadline, LocalDate> hvornårColumn = new TableColumn<Deadline, LocalDate>("Hvornår");
        hvornårColumn.setCellValueFactory(new PropertyValueFactory<>("hvornår"));

        TableColumn<Deadline, String> hvadColumn = new TableColumn<Deadline, String>("Hvad:");
        hvadColumn.setCellValueFactory(new PropertyValueFactory<>("hvad"));

        TableColumn<Deadline, String> hvemColumn = new TableColumn<Deadline, String>("Hvem:");
        hvemColumn.setCellValueFactory(new PropertyValueFactory<>("hvem"));

        tView.setItems(getDeadlines());

        tView.getColumns().addAll(hvornårColumn, hvadColumn, hvemColumn);

        // Sorterer så første deadlines kommer først
        hvornårColumn.setSortType(TableColumn.SortType.ASCENDING);
        tView.getSortOrder().add(hvornårColumn);

        // Herunder oprettes en metode til at håndtere dobbeltklik og hente objekter
        // (hel række)
        tView.setRowFactory(tv -> {
            TableRow<Deadline> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Deadline clickedRow = row.getItem();
                    popUpDead.changeDeadline(clickedRow, gui.ec, tView);
                    tView.refresh();
                }
            });
            return row;
        });

        Button tilføjButton = new Button("Tilføj påmindelse");
        tilføjButton.setOnAction(event -> popUpDead.createDeadline(gui.ec, tView));

        Button fjernButton = new Button("Fjern påmindelse");
        fjernButton.setOnAction(event -> {
            ObservableList<Deadline> deadlineValgt, alleDeadlines;
            alleDeadlines = tView.getItems();
            deadlineValgt = tView.getSelectionModel().getSelectedItems();
            Deadline d = tView.getSelectionModel().getSelectedItem();
            d.setKlaret(true);
            gui.ec.opretDeadlineIExcel(d);
            //deadlineValgt.forEach(alleDeadlines::remove); //TODO den vil ikke opdatere gui uden for en JavaFX thread (tror jeg)

        });
        TextField filter = new TextField();
        filter.setPromptText("Søg...");
        filter.setPrefWidth(150);
        filter.setMaxWidth(150);


        // Tilføjer til højre side af menuen
        højreLayout.add(tView, 2, 3, 3, 6);
        højreLayout.add(tilføjButton, 2, 10);
        højreLayout.add(fjernButton, 3, 10);
        højreLayout.add(filter,4, 10);


        //SORTER DATA I TABLEVIEW
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Deadline> filteredData = new FilteredList<>(getDeadlines(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(deadline -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (deadline.getHvad().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (deadline.getHvem().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Deadline> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tView.setItems(sortedData);

        Scene scene = gui.gui.getScene();
        scene = new Scene(hb, 1300, 600);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static ObservableList<Deadline> getDeadlines() {
        // Der skal lægges ind og testes for 'isKlaret'
        ArrayList<Deadline> alleDeadlines = gui.ec.getDeadlines();
        ArrayList<Deadline> temp = new ArrayList<Deadline>();

        for (Deadline d : alleDeadlines) {
            if (d.isKlaret() == false)
                temp.add(d);
        }

        ObservableList<Deadline> deadlines = FXCollections.observableArrayList(temp);
        return deadlines;
    }
}
