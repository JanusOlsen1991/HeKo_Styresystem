package view.main;

import controller.ExcelConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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

public class Hovedmenu {
    private static GuiSingleton gui;
    GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();


    public Hovedmenu() {
    gui = GuiSingleton.getInstance();
    }

    public void hovedMenu(Stage primaryStage) {

        primaryStage.setTitle("Herlevkollegiets Indstillingsudvalg");
        VBox venstreLayout = new VBox(5);
        venstreLayout.setPadding(new Insets(30, 20, 0, 10));


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
        //studieKontrolButton.setOnAction(e -> studieKontrolMenu(primaryStage));
        Button dispensationsButton = new Button("Dispensation");
        //dispensationsButton.setOnAction(e -> dispensationsMenu(primaryStage, tView));
        Button fremlejeButton = new Button("Fremleje");
        //fremlejeButton.setOnAction(e -> fremlejeMenu(primaryStage));
        Button værelsesudlejningsButton = new Button("Værelsesudlejning");
        //værelsesudlejningsButton.setOnAction(e -> værelsesUdlejningMenu(primaryStage));

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

    }
    public static ObservableList<Deadline> getDeadlines () {
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
