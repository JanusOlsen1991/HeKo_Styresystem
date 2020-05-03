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
import model.Deadline;
import model.Dispensation;
import view.GuiSingleton;
import view.popups.GUI_PopUps;

import java.time.LocalDate;
import java.util.ArrayList;

public class DispensationsMenu {
    static GuiSingleton gui;
    static GUI_PopUps popUp = new GUI_PopUps();



    public void dispensationsMenu(Stage primaryStage, TableView<Deadline> tViewHMenu) {

        BorderPane borderP = new BorderPane();

        // Venstre side af menuen
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

        TableView<Dispensation> tView = new TableView<Dispensation>();
        // Start knappen
        Button startDispButton = new Button("Kom i gang");
        startDispButton.setOnAction(e -> {
            popUp.opretDispensation(gui.ec, tView, tViewHMenu, null, false);// , tView
        });

        tView.setPlaceholder(startDispButton);

        TableColumn<Dispensation, String> værelseColumn = new TableColumn<Dispensation, String>("Værelse");
        værelseColumn.setCellValueFactory(new PropertyValueFactory<>("beboerVærelse"));
        TableColumn<Dispensation, String> navnColumn = new TableColumn<Dispensation, String>("Navn");
        navnColumn.setCellValueFactory(new PropertyValueFactory<>("beboerNavn"));
        TableColumn<Dispensation, LocalDate> startdatoColumn = new TableColumn<Dispensation, LocalDate>("Start dato");
        startdatoColumn.setCellValueFactory(new PropertyValueFactory<>("startDato"));
        TableColumn<Dispensation, LocalDate> slutdatoColumn = new TableColumn<Dispensation, LocalDate>("Slut dato");
        slutdatoColumn.setCellValueFactory(new PropertyValueFactory<>("slutDato"));

        tView.getColumns().addAll(værelseColumn, navnColumn, startdatoColumn, slutdatoColumn);
        tView.getItems().addAll(getDispensationer());

        tView.setRowFactory(tv -> {
            TableRow<Dispensation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    // TODO Opret - rediger i dispensations metode.
                    Dispensation clickedRow = row.getItem();
                    popUp.opretDispensation(gui.ec, tView, tViewHMenu, clickedRow, true);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    popUp.opretDispensation(gui.ec, tView, tViewHMenu, null, false);// , tView
                }
            });
            return row;
        });

        Tab tab1 = new Tab("Aktive dispensationer");
        tab1.setClosable(false);
        tab1.setContent(tView);
        tP.getTabs().add(tab1);

        Scene scene = gui.gui.getScene();

        scene = new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
    }

    private ObservableList<Dispensation> getDispensationer() {
        // Der skal lægges ind og testes for 'isKlaret'
        ArrayList<Dispensation> alleDisps = gui.ec.getDispensationer();
        ArrayList<Dispensation> temp = new ArrayList<Dispensation>();

        for (Dispensation d : alleDisps) {
            if (d.isiGang() == true)
                temp.add(d);
        }

        ObservableList<Dispensation> dispensationer = FXCollections.observableArrayList(temp);
        return dispensationer;

    }
}
