package view.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Beboer;
import view.GuiSingleton;
import view.popups.GUI_PopUps;
import view.popups.GUI_PopUps_Deadlines;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class FremlejeMenu {
    static GUI_PopUps popUp = new GUI_PopUps();
    static GuiSingleton gui;

    @SuppressWarnings("unchecked")
    public void fremlejeMenu(Stage primaryStage) {
        // Tabs og overordnet layout genereres
        BorderPane borderP = new BorderPane();
        // venstre side
        Button tilbageButton = new Button("Tilbage");
        tilbageButton.setOnAction(e -> gui.hovedMenu.hovedMenu(primaryStage));
        TableView<Beboer> tView = createTable();
        VBox vb = new VBox(tilbageButton);
        borderP.setLeft(vb);
        borderP.setCenter(tView);

        Scene scene = gui.gui.getScene();
        scene = new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
    }

    private TableView<Beboer> createTable() {
        TableView<Beboer> tView = new TableView();
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

        tView.setRowFactory(tv -> {
            TableRow<Beboer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Beboer clickedRow = row.getItem();
                    // TODO Hvad skal ske ved fremleje
                    popUp.redigerBeboeroplysninger(clickedRow, tView, true);
                }
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    popUp.opretNyFremlejer(tView);
                }
            });
            return row;
        });

        Button buttonBegynd = new Button("Kom i gang");
        buttonBegynd.setOnAction(event -> {
            popUp.opretNyFremlejer(tView);
        });
        tView.setPlaceholder(buttonBegynd);
        tView.setItems(getFremlejere());

        tView.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, telefonColumn, uddStedColumn,
                uddannelseColumn, påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn);
        return tView;
    }

    private ObservableList<Beboer> getFremlejere() {
        ArrayList<Beboer> alleFremlejere = gui.ec.getFremlejere();
        ArrayList<Beboer> temp = new ArrayList();

        for (Beboer b : alleFremlejere) {
            if (b.getLejeaftalensUdløb().isAfter(LocalDate.now())) {
                temp.add(b);
            }
        }
        Collections.sort(temp);
        return FXCollections.observableArrayList(temp);
    }
}
