package view.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Værelsesudlejning;
import view.GuiSingleton;
import view.popups.GUI_PopUps;

import java.time.LocalDate;
import java.util.ArrayList;

public class VærelsesUdlejningsMenu {
    private static GuiSingleton gui;
    static GUI_PopUps popUp = new GUI_PopUps();


    /**
     *
     * @param primaryStage
     *            : main stage
     */
    @SuppressWarnings("unchecked")
    public void værelsesUdlejningMenu(Stage primaryStage) {
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
        vb.setPadding(new Insets(0,10,0,0));
        borderP.setLeft(vb);

        // Midten
        TabPane tP = new TabPane();
        borderP.setCenter(tP);

        Tab tab1 = new Tab("Ledige værelser");
        tab1.setClosable(false);
        Tab tab2 = new Tab("Udlejede værelser");
        tab2.setClosable(false);

        tP.getTabs().addAll(tab1, tab2);

        TableView<Værelsesudlejning> tView1 = new TableView<Værelsesudlejning>();
        Button buttonStart = new Button("Kom i gang");
        buttonStart.setOnAction(event -> {
            popUp.opretLedigtVærelse(gui.ec, tView1);
        });

        tView1.setPlaceholder(buttonStart);
        TableView<Værelsesudlejning> tView2 = new TableView<Værelsesudlejning>();

        // Herunder oprettes en metode til at håndtere dobbeltklik
        tView1.setRowFactory(tv -> {
            TableRow<Værelsesudlejning> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    popUp.opretLedigtVærelse(gui.ec, tView1);
                    tView1.refresh();
                }
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Værelsesudlejning clickedRow = row.getItem();
                    popUp.udfyldLedigtVærelse(gui.ec, tView1, tView2, clickedRow, false);
                    tView1.refresh();
                }
            });
            return row;
        });

        TableColumn<Værelsesudlejning, String> værelseColumn = new TableColumn<Værelsesudlejning, String>("Værelse");
        værelseColumn.setCellValueFactory(new PropertyValueFactory<>("værelse"));

        TableColumn<Værelsesudlejning, LocalDate> indflytningColumn = new TableColumn<Værelsesudlejning, LocalDate>(
                "Overtagelsesdato");
        indflytningColumn.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));

        tView1.setItems(getVærelsesUdlejning(true));

        TableColumn<Værelsesudlejning, String> værelseColumn2 = new TableColumn<Værelsesudlejning, String>("Værelse");
        værelseColumn2.setCellValueFactory(new PropertyValueFactory<>("værelse"));

        TableColumn<Værelsesudlejning, String> navnColumn2 = new TableColumn<Værelsesudlejning, String>("Navn");
        navnColumn2.setCellValueFactory(new PropertyValueFactory<>("navn"));

        TableColumn<Værelsesudlejning, LocalDate> indflytningColumn2 = new TableColumn<Værelsesudlejning, LocalDate>(
                "Overtagelsesdato");
        indflytningColumn2.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
        TableColumn<Værelsesudlejning, String> behandletDatoColumn2 = new TableColumn<Værelsesudlejning, String>(
                "Behandlingsdato");
        behandletDatoColumn2.setCellValueFactory(new PropertyValueFactory<>("behandlingsdato"));
        TableColumn<Værelsesudlejning, String> behandlerInitialerColumn2 = new TableColumn<Værelsesudlejning, String>(
                "Behandler Initialer");
        behandlerInitialerColumn2.setCellValueFactory(new PropertyValueFactory<>("behandlerInitialer"));
        tView2.setItems(getVærelsesUdlejning(false));
        tView2.setRowFactory(tv -> {
            TableRow<Værelsesudlejning> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    // TODO Lav noget her der retter info ved værelsesudlejning
                    Værelsesudlejning clickedRow = row.getItem();
                    popUp.udfyldLedigtVærelse(gui.ec, tView1, tView2, clickedRow, true);
                    tView2.refresh();
                }

            });
            return row;
        });

        tView1.getColumns().addAll(indflytningColumn, værelseColumn);
        tView2.getColumns().addAll(indflytningColumn2, værelseColumn2, navnColumn2, behandletDatoColumn2,
                behandlerInitialerColumn2);

        tab1.setContent(tView1);
        tab2.setContent(tView2);
        Scene scene = gui.gui.getScene();
        scene = new Scene(borderP, 900, 700);
        scene.getStylesheets().add("hekostyling.css");
        primaryStage.setScene(scene);
    }

    /**
     *
     * @param ledigeVærelser:
     *            True hvis værelset er ledigt
     * @return ObservableList med enten udlejede værelser eller ikke lejede værelser
     */
    private ObservableList<Værelsesudlejning> getVærelsesUdlejning(boolean ledigeVærelser) {
        ArrayList<Værelsesudlejning> alleVærelser = gui.ec.getVærelsesudlejning();
        ArrayList<Værelsesudlejning> temp = new ArrayList<Værelsesudlejning>();
        if (ledigeVærelser == true) {
            for (Værelsesudlejning v : alleVærelser) {
                if (v.getNavn().length() == 0) {
                    temp.add(v);
                }
            }
        } else {
            for (Værelsesudlejning v : alleVærelser) {
                if (v.getNavn().length() > 0) {
                    temp.add(v);

                }
            }

        }
        ObservableList<Værelsesudlejning> værelser = FXCollections.observableArrayList(temp);
        return værelser;
    }
}
