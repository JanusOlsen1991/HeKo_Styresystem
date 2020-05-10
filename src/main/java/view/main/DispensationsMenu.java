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
import model.Dispensation;
import view.GuiSingleton;
import view.popups.GUI_PopUps;
import view.utils.TableColumnFormatter;

import java.time.LocalDate;
import java.util.ArrayList;

public class DispensationsMenu {
    static GuiSingleton gui;
    static GUI_PopUps popUp = new GUI_PopUps();



    public void dispensationsMenu(Stage primaryStage) {

        BorderPane borderP = new BorderPane();

        // Venstre side af menuen
        Button tilbageButton = new Button("Tilbage");
        tilbageButton.setOnAction(e -> gui.hovedMenu.hovedMenu(primaryStage));

        VBox vb = new VBox(tilbageButton);
        borderP.setLeft(vb);

        TabPane tP = new TabPane();
        borderP.setCenter(tP);

        TableView<Dispensation> tView = createTable();

        Tab tab1 = new Tab("Aktive dispensationer");
        tab1.setClosable(false);
        tab1.setContent(tView);
        tP.getTabs().add(tab1);

        Scene scene = gui.gui.getScene();

        scene = new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
}


    private TableView<Dispensation> createTable() {
        TableView<Dispensation> tView = new TableView<Dispensation>();

        Button startDispButton = new Button("Kom i gang");
        startDispButton.setOnAction(e -> {
            popUp.opretDispensation(tView, null, false);// , tView
        });

        tView.setPlaceholder(startDispButton);

        TableColumn<Dispensation, String> værelseColumn = new TableColumn<Dispensation, String>("Værelse");
        værelseColumn.setCellValueFactory(new PropertyValueFactory<>("beboerVærelse"));
        TableColumn<Dispensation, String> navnColumn = new TableColumn<Dispensation, String>("Navn");
        navnColumn.setCellValueFactory(new PropertyValueFactory<>("beboerNavn"));
        TableColumn<Dispensation, LocalDate> startdatoColumn = new TableColumn<Dispensation, LocalDate>("Start dato");
        startdatoColumn.setCellValueFactory(new PropertyValueFactory<>("startDato"));
        TableColumnFormatter.formatDate(startdatoColumn);

        TableColumn<Dispensation, LocalDate> slutdatoColumn = new TableColumn<Dispensation, LocalDate>("Slut dato");
        slutdatoColumn.setCellValueFactory(new PropertyValueFactory<>("slutDato"));
        TableColumnFormatter.formatDate(slutdatoColumn);


        tView.getColumns().addAll(værelseColumn, navnColumn, startdatoColumn, slutdatoColumn);
        tView.getItems().addAll(getDispensationer());

        tView.setRowFactory(tv -> {
            TableRow<Dispensation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Dispensation clickedRow = row.getItem();
                    popUp.opretDispensation(tView, clickedRow, true);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    popUp.opretDispensation(tView, null, false);// , tView
                }
            });
            return row;
        });
        return tView;
    }

    private ObservableList<Dispensation> getDispensationer() {
        ArrayList<Dispensation> alleDisps = gui.ec.getDispensationer();
        ArrayList<Dispensation> temp = new ArrayList<Dispensation>();

        for (Dispensation d : alleDisps) {
            if (d.isiGang() == true)
                temp.add(d);
        }

        return FXCollections.observableArrayList(temp);

    }
}
