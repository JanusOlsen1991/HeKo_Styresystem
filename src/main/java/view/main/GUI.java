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

	/**
	 * Metoden anvendes til at oprette "hovedmenuen" for programmet.
	 * 
	 * @param primaryStage
	 *            : is the stage given to set up the GUI
	 */

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
		dispensationsButton.setOnAction(e -> dispensationsMenu(primaryStage, tView));
		Button fremlejeButton = new Button("Fremleje");
		fremlejeButton.setOnAction(e -> fremlejeMenu(primaryStage));
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


	private void dispensationsMenu(Stage primaryStage, TableView<Deadline> tViewHMenu) {

		BorderPane borderP = new BorderPane();

		// Venstre side af menuen
		Button tilbageButton = new Button("Tilbage");
		tilbageButton.setOnAction(e -> {
			try {
				hovedMenu(primaryStage);
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


	@SuppressWarnings("unchecked")
	public void fremlejeMenu(Stage primaryStage) {
		// Tabs og overordnet layout genereres
		BorderPane borderP = new BorderPane();
		// venstre side
		Button tilbageButton = new Button("Tilbage");
		tilbageButton.setOnAction(e -> {
			try {
				hovedMenu(primaryStage);
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
					hovedMenu(primaryStage);
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
