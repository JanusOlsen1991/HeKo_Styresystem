package view.main;

import controller.ExcelConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.*;
import view.GuiSingleton;
import view.popups.GUI_PopUps;
import view.popups.GUI_PopUps_Deadlines;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GUI {
	private Scene scene;
	static GUI_PopUps popUp = new GUI_PopUps();
	GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();
	static GuiSingleton gui;

	public GUI() {
		gui = GuiSingleton.getInstance();
		// this.ec = ec;
	}



	@SuppressWarnings("unchecked")
	public void fremlejeMenu(Stage primaryStage) {
		// Tabs og overordnet layout genereres
		BorderPane borderP = new BorderPane();
		// venstre side
		Button tilbageButton = new Button("Tilbage");
		tilbageButton.setOnAction(e -> {
			try {
				gui.hovedMenu.hovedMenu(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});
		TableView<Beboer> tView = new TableView<Beboer>();
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
					popUp.redigerBeboeroplysninger(clickedRow, gui.ec, tView, true);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyFremlejer(gui.ec, tView);
				}
			});
			return row;
		});

		Button buttonBegynd = new Button("Kom i gang");
		buttonBegynd.setOnAction(event -> {
			popUp.opretNyFremlejer(gui.ec, tView);
		});
		tView.setPlaceholder(buttonBegynd);
		tView.setItems(getFremlejere());

		tView.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, telefonColumn, uddStedColumn,
				uddannelseColumn, påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn);
		VBox vb = new VBox(tilbageButton);
		borderP.setLeft(vb);
		borderP.setCenter(tView);

		scene = new Scene(borderP, 900, 700);
		scene.getStylesheets().add("hekostyling.css");
		primaryStage.setScene(scene);
	}

	private ObservableList<Beboer> getFremlejere() {
		// Der skal lægges ind og testes for 'isKlaret'
		ArrayList<Beboer> alleFremlejere = gui.ec.getFremlejere();
		ArrayList<Beboer> temp = new ArrayList<Beboer>();

		for (Beboer b : alleFremlejere) {
			if (b.getLejeaftalensUdløb().isAfter(LocalDate.now())) {
				temp.add(b);
			}
		}

		ObservableList<Beboer> beboere = FXCollections.observableArrayList(temp);
		return beboere;
	}

	public void filplacering(Stage primaryStage) {

		GridPane gP = new GridPane();
		Label l = new Label("Filplacering:");
		TextField text = new TextField();
		// finder fil-stien
		String filename = "Excelplacering.txt";
		String filepath = null;
		try {

			FileReader fileReader = new FileReader(filename);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			filepath = bufferedReader.readLine();
			text.setText(filepath);

			bufferedReader.close();

		} catch (Exception ex) {
			//
		}

		Button findFilButton = new Button("Ændr filplacering");
		findFilButton.setOnAction(event -> {
			DirectoryChooser dir = new DirectoryChooser();
			dir.setTitle("Vælg filplacering");
			File excelplacering = dir.showDialog(primaryStage);
			text.setText(excelplacering.getAbsolutePath() + "\\IndstillingsInfo.xlsx");

		});

		Button startButton = new Button("Start");
		startButton.setOnAction(event -> {
			String s = text.getText();
			try {
				gui.ec = new ExcelConnection(s);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//Skriver  
			try {
				FileWriter fileWriter = new FileWriter(filename);

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(text.getText());

				// Always close files.
				bufferedWriter.close();
			} catch (IOException ex1) {

			}
				try {
					gui.hovedMenu.hovedMenu(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(event -> primaryStage.close());

		gP.add(l, 3, 3);
		gP.add(text, 3, 4);
		// gP.add(labelFil, 5, 4);
		gP.add(findFilButton, 4, 4);
		gP.add(startButton, 3, 5);
		gP.add(annullerButton, 4, 5);

		Scene scene = new Scene(gP);
		primaryStage.setScene(scene);
		
		// The name of the file to open.

		primaryStage.show();
	}

	public Scene getScene() {
		return scene;
	}

}
