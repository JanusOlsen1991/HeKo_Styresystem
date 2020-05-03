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
import view.popups.GUI_PopUps;
import view.popups.GUI_PopUps_Deadlines;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GUI {
	private Scene scene;
	static ExcelConnection ec;
	static GUI_PopUps popUp = new GUI_PopUps();
	GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();

	public GUI() {
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
		beboerlisteButton.setOnAction(e -> beboerlisteMenu(primaryStage));
		Button studieKontrolButton = new Button("Studiekontrol");
		studieKontrolButton.setOnAction(e -> studieKontrolMenu(primaryStage));
		Button dispensationsButton = new Button("Dispensation");
		dispensationsButton.setOnAction(e -> dispensationsMenu(primaryStage, tView));
		Button fremlejeButton = new Button("Fremleje");
		fremlejeButton.setOnAction(e -> fremlejeMenu(primaryStage));
		Button værelsesudlejningsButton = new Button("Værelsesudlejning");
		værelsesudlejningsButton.setOnAction(e -> værelsesUdlejningMenu(primaryStage));

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
					popUpDead.changeDeadline(clickedRow, ec, tView);
					tView.refresh();
				}
			});
			return row;
		});

		Button tilføjButton = new Button("Tilføj påmindelse");
		tilføjButton.setOnAction(event -> popUpDead.createDeadline(ec, tView));

		Button fjernButton = new Button("Fjern påmindelse");
		fjernButton.setOnAction(event -> {
			ObservableList<Deadline> deadlineValgt, alleDeadlines;
			alleDeadlines = tView.getItems();
			deadlineValgt = tView.getSelectionModel().getSelectedItems();
			Deadline d = tView.getSelectionModel().getSelectedItem();
			d.setKlaret(true);
			ec.opretDeadlineIExcel(d);
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
		ArrayList<Deadline> alleDeadlines = ec.getDeadlines();
		ArrayList<Deadline> temp = new ArrayList<Deadline>();

		for (Deadline d : alleDeadlines) {
			if (d.isKlaret() == false)
				temp.add(d);
		}

		ObservableList<Deadline> deadlines = FXCollections.observableArrayList(temp);
		return deadlines;
	}

	/**
	 *
	 * @param columnProps
	 * @return
	 */

	/**
	 * 
	 * @param primaryStage
	 *            : main stage
	 */
	@SuppressWarnings("unchecked")
	private void værelsesUdlejningMenu(Stage primaryStage) {
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
			popUp.opretLedigtVærelse(ec, tView1);
		});

		tView1.setPlaceholder(buttonStart);
		TableView<Værelsesudlejning> tView2 = new TableView<Værelsesudlejning>();

		// Herunder oprettes en metode til at håndtere dobbeltklik
		tView1.setRowFactory(tv -> {
			TableRow<Værelsesudlejning> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					popUp.opretLedigtVærelse(ec, tView1);
					tView1.refresh();
				}
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Værelsesudlejning clickedRow = row.getItem();
					popUp.udfyldLedigtVærelse(ec, tView1, tView2, clickedRow, false);
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
					popUp.udfyldLedigtVærelse(ec, tView1, tView2, clickedRow, true);
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
		ArrayList<Værelsesudlejning> alleVærelser = ec.getVærelsesudlejning();
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
			popUp.opretDispensation(ec, tView, tViewHMenu, null, false);// , tView
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
					popUp.opretDispensation(ec, tView, tViewHMenu, clickedRow, true);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					popUp.opretDispensation(ec, tView, tViewHMenu, null, false);// , tView
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
		ArrayList<Dispensation> alleDisps = ec.getDispensationer();
		ArrayList<Dispensation> temp = new ArrayList<Dispensation>();

		for (Dispensation d : alleDisps) {
			if (d.isiGang() == true)
				temp.add(d);
		}

		ObservableList<Dispensation> dispensationer = FXCollections.observableArrayList(temp);
		return dispensationer;

	}

	@SuppressWarnings("unchecked")
	private void studieKontrolMenu(Stage primaryStage) {
		// overordnet layout genereres
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

		VBox vb = new VBox(tilbageButton);
		borderP.setLeft(vb);

		// Midten
		TabPane tP = new TabPane();
		borderP.setCenter(tP);

		TableView<Beboer> tView1 = new TableView<Beboer>();
		tView1.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView1, false);

				}
			});
			return row;
		});
		// kolloner til Tableviews
		TableColumn<Beboer, String> værelseColumn = new TableColumn<Beboer, String>("Værelse");
		værelseColumn.setCellValueFactory(new PropertyValueFactory<>("værelse"));
		TableColumn<Beboer, String> navnColumn = new TableColumn<Beboer, String>("Navn");
		navnColumn.setCellValueFactory(new PropertyValueFactory<>("navn"));
		TableColumn<Beboer, LocalDate> indflytningColumn = new TableColumn<Beboer, LocalDate>("indflytningsdato");
		indflytningColumn.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
		TableColumn<Beboer, String> uddRetningColumn = new TableColumn<Beboer, String>("Uddannelsesretning");
		uddRetningColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
		TableColumn<Beboer, String> uddStedColumn = new TableColumn<Beboer, String>("Uddannelsessted");
		uddStedColumn.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
		TableColumn<Beboer, LocalDate> påbegyndtUddColumn = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
		påbegyndtUddColumn.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
		TableColumn<Beboer, LocalDate> afslutningUddColumn = new TableColumn<Beboer, LocalDate>(
				"Uddannelse forventes afsluttet");
		afslutningUddColumn.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
		TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn = new TableColumn<Beboer, LocalDate>(
				"Lejeaftalens udløb");
		lejeaftalensUdløbColumn.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));
		TableColumn<Beboer, String> studieKStatColumn = new TableColumn<Beboer, String>("Status på studiekontrol");
		studieKStatColumn.setCellValueFactory(new PropertyValueFactory<>("statusPåStudiekontrol"));

		// TableViews oprettes med kollonnerne
		tView1.getColumns().addAll(værelseColumn, navnColumn, indflytningColumn, uddRetningColumn, uddStedColumn,
				påbegyndtUddColumn, afslutningUddColumn, lejeaftalensUdløbColumn, studieKStatColumn);
		tView1.getItems().addAll(getAlleStudiekontroller());

		Button opretButton = new Button("Start ny studiekontrol");
		opretButton.setOnAction(event -> {
			// Tab t =
			// opretStudiekontrolTab(ec.getStudiekontroller().get(ec.getStudiekontroller().size()));
			popUp.startStudiekontrol(tView1, ec, tP);

		});

		VBox firstTabLayout = new VBox(tView1, opretButton);

		Tab tab1 = new Tab("Alle igangværende studiekontroller");
		tab1.setClosable(false);

		tab1.setContent(firstTabLayout);// TODO layout ikke kun tView1

		tP.getTabs().add(tab1);
		// Tabs herunder skal oprettes KUN "når de er i gang".
		ArrayList<Studiekontrol> list = ec.getStudiekontroller();

		if (ec.getStudiekontroller().size() > 0)// TODO - Af en eller anden grund overskriver loopet også indholdet i
												// det første tableview
			for (Studiekontrol sk : list) {
				Tab t = opretStudiekontrolTab(sk);
				// Tilføjer tabs til tabPane
				tP.getTabs().add(t);

			}

		scene = new Scene(borderP, 900, 700);
		scene.getStylesheets().add("hekostyling.css");
		primaryStage.setScene(scene);
		
	}

	public static Tab opretStudiekontrolTab(Studiekontrol sk) {
		String s = ec.findMånedsNavn(sk.getMånedsnummer());
		Tab t = new Tab(s);
		t.setClosable(false);

		Label skID = new Label(sk.getStudiekontrolID());
		Label skSTART = new Label(sk.getPåbegyndelsesdato().toString());
		Label skSLUT = new Label(sk.getAfleveringsfrist().toString());

		Label l1 = new Label("Studiekontrol ID");
		Label l2 = new Label("påbegyndt d. ");
		Label l3 = new Label("Afsluttes d. ");

		TableColumn<Beboer, String> værelseColumn1 = new TableColumn<Beboer, String>("Værelse");
		værelseColumn1.setCellValueFactory(new PropertyValueFactory<>("værelse"));
		TableColumn<Beboer, String> navnColumn1 = new TableColumn<Beboer, String>("Navn");
		navnColumn1.setCellValueFactory(new PropertyValueFactory<>("navn"));
		TableColumn<Beboer, LocalDate> indflytningColumn1 = new TableColumn<Beboer, LocalDate>("indflytningsdato");
		indflytningColumn1.setCellValueFactory(new PropertyValueFactory<>("indflytningsdato"));
		TableColumn<Beboer, String> uddRetningColumn1 = new TableColumn<Beboer, String>("Uddannelsesretning");
		uddRetningColumn1.setCellValueFactory(new PropertyValueFactory<>("uddannelsesretning"));
		TableColumn<Beboer, String> uddStedColumn1 = new TableColumn<Beboer, String>("Uddannelsessted");
		uddStedColumn1.setCellValueFactory(new PropertyValueFactory<>("uddannelsessted"));
		TableColumn<Beboer, LocalDate> påbegyndtUddColumn1 = new TableColumn<Beboer, LocalDate>("Uddannelse påbegyndt");
		påbegyndtUddColumn1.setCellValueFactory(new PropertyValueFactory<>("påbegyndtDato"));
		TableColumn<Beboer, LocalDate> afslutningUddColumn1 = new TableColumn<Beboer, LocalDate>(
				"Uddannelse forventes afsluttet");
		afslutningUddColumn1.setCellValueFactory(new PropertyValueFactory<>("forventetAfsluttetDato"));
		TableColumn<Beboer, LocalDate> lejeaftalensUdløbColumn1 = new TableColumn<Beboer, LocalDate>(
				"Lejeaftalens udløb");
		lejeaftalensUdløbColumn1.setCellValueFactory(new PropertyValueFactory<>("lejeaftalensUdløb"));
		TableColumn<Beboer, String> studieKStatColumn1 = new TableColumn<Beboer, String>();
		studieKStatColumn1.setCellValueFactory(new PropertyValueFactory<>("statusPåStudiekontrol"));

		TableView<Beboer> tView = new TableView<Beboer>();

		// Dobbelt klik
		tView.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView, false);
					tView.refresh(); // nødvendig?
				}
			});
			return row;
		});

		// Henter beboere fra studiekontrollen
		ObservableList<Beboer> beboereTilMåned = FXCollections.observableArrayList(sk.getBeboere());

		tView.getItems().addAll(beboereTilMåned);

		tView.getColumns().addAll(værelseColumn1, navnColumn1, indflytningColumn1, uddRetningColumn1, uddStedColumn1,
				påbegyndtUddColumn1, afslutningUddColumn1, lejeaftalensUdløbColumn1, studieKStatColumn1);

		Button bAfslut = new Button("Afslut studiekontrollen");
		bAfslut.setOnAction(event -> {
			popUp.afslutStudiekontrol(sk, ec, tView, t);
		});

		GridPane gp = new GridPane();
		gp.add(l1, 3, 3);
		gp.add(l2, 6, 3);
		gp.add(l3, 9, 3);

		gp.add(skID, 3, 5);
		gp.add(skSTART, 6, 5);
		gp.add(skSLUT, 9, 5);
		gp.add(bAfslut, 3, 30, 3, 1);

		gp.add(tView, 1, 12, 10, 10);

		t.setContent(gp);
		return t;
	}

	private ObservableList<Beboer> getAlleStudiekontroller() {
		ArrayList<Beboer> alleBeboere = ec.getBeboere();
		ArrayList<Beboer> temp = new ArrayList<Beboer>();
		for (Beboer b : alleBeboere) {
			if (b.getStudiekontrolstatus() != Studiekontrolstatus.IKKEIGANG)
				if (b.getStudiekontrolstatus() != Studiekontrolstatus.GODKENDT)
					if (b.getStudiekontrolstatus() != Studiekontrolstatus.SENDTTILBOLIGSELSKAB)
						temp.add(b);
		}
		ObservableList<Beboer> alleStudiekontroller = FXCollections.observableArrayList(temp);
		return alleStudiekontroller;
	}

	@SuppressWarnings("unchecked")
	public void beboerlisteMenu(Stage primaryStage) {

		// overordnet layout genereres
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
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView1, false);

				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
				}
			});
			return row;
		});

		// Start knappen
		Button bb1 = new Button("Kom i gang");
		bb1.setOnAction(e -> {
			popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
		});

		tView1.setPlaceholder(bb1);

		tView2.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView2, false);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
				}
			});
			return row;
		});

		tView3.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView3, false);

				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
				}
			});
			return row;
		});

		tView4.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView4, false);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
				}
			});
			return row;
		});

		tView5.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView5, false);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
				}
			});
			return row;
		});

		tView6.setRowFactory(tv -> {
			TableRow<Beboer> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Beboer clickedRow = row.getItem();
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView6, false);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyBeboeroplysninger(ec, tView1, tView2, tView3, tView4, tView5, tView6);
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
		scene = new Scene(borderP, 900, 700);
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
		ArrayList<Beboer> alleBeboere = ec.getBeboere();
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
					popUp.redigerBeboeroplysninger(clickedRow, ec, tView, true);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUp.opretNyFremlejer(ec, tView);
				}
			});
			return row;
		});

		Button buttonBegynd = new Button("Kom i gang");
		buttonBegynd.setOnAction(event -> {
			popUp.opretNyFremlejer(ec, tView);
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
		ArrayList<Beboer> alleFremlejere = ec.getFremlejere();
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
				GUI.ec = new ExcelConnection(s);
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

}
