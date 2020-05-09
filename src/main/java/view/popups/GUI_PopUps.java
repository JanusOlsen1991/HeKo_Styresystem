package view.popups;

import controller.ExcelConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.*;
import view.GuiSingleton;
import view.main.GUI;
import view.main.IParentTable;
import view.main.StudiekontrolMenu;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class GUI_PopUps {
	private Stage stage = new Stage();
	private GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();
	private GuiSingleton gui = GuiSingleton.getInstance();

	public void opretNyBeboeroplysninger(IParentTable baseGui) {
		stage.setTitle("Rediger beboeroplysninger");

		GridPane layout = new GridPane();
		layout.setHgap(5);
		layout.setVgap(5);
		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Navn:");
		Label l3 = new Label("Indflytningsdato:");
		Label l4 = new Label("Telefonnummer:");
		Label l5 = new Label("Uddannelsessted");
		Label l6 = new Label("Uddannelsesretning");
		Label l7 = new Label("Uddannelse påbegyndt");
		Label l8 = new Label("Forventet uddannelsesafslutning");
		Label l9 = new Label("Lejeaftalens udløb:");
		Label l10 = new Label("Studiekontrol status:");

		TextField værelse = new TextField();
		TextField navn = new TextField();
		DatePicker indflytningsdato = new DatePicker();
		TextField telefonnummer = new TextField();
		TextField uddannelsessted = new TextField();
		TextField uddannelsesretning = new TextField();
		DatePicker uddStart = new DatePicker();
		DatePicker uddSlut = new DatePicker();
		DatePicker lejeaftalensUdløb = new DatePicker();
		ComboBox<String> studiekontrolStatus = new ComboBox<String>();
		studiekontrolStatus.getItems().addAll("Ikke i gang", "Modtaget, ikke godkendt", "Ikke Modtaget",
				"Sendt til boligselskab", "Godkendt");

		Button gemButton = new Button("Opret beboer");
		gemButton.setOnAction(e -> {
			Studiekontrolstatus status = (Studiekontrolstatus) gui.ec
					.konverterStringTilEnum(studiekontrolStatus.getValue());
			Beboer b = new Beboer(navn.getText(), værelse.getText(), indflytningsdato.getValue(),
					lejeaftalensUdløb.getValue(), telefonnummer.getText(), status, uddannelsessted.getText(),
					uddannelsesretning.getText(), uddStart.getValue(), uddSlut.getValue());

			gui.ec.opretBeboerIExcel(b);
			gui.ec.getBeboere().clear();
			gui.ec.hentBeboereFraExcel();
			baseGui.update(b, værelse.getText());


			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 6);
		layout.add(l3, 3, 9);
		layout.add(l4, 3, 12);
		layout.add(l5, 3, 15);
		layout.add(l6, 3, 18);
		layout.add(l7, 3, 21);
		layout.add(l8, 3, 24);
		layout.add(l9, 3, 27);
		layout.add(l10, 3, 30);

		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 5, 3);
		layout.add(navn, 5, 6);
		layout.add(indflytningsdato, 5, 9);
		layout.add(telefonnummer, 5, 12);
		layout.add(uddannelsessted, 5, 15);
		layout.add(uddannelsesretning, 5, 18);
		layout.add(uddStart, 5, 21);
		layout.add(uddSlut, 5, 24);
		layout.add(lejeaftalensUdløb, 5, 27);
		layout.add(studiekontrolStatus, 5, 30);

		// Sætter buttons på layout
		layout.add(gemButton, 3, 33);
		layout.add(annullerButton, 5, 33);
		layout.setPrefSize(500, 700);

		Scene scene = new Scene(layout);

		stage.setScene(scene);
		stage.showAndWait();
	}

	public void opretNyFremlejer(ExcelConnection ec, TableView<Beboer> tView) {
		stage.setTitle("Opret Fremlejer");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setVgap(5);
		layout.setHgap(5);
		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Navn:");
		Label l3 = new Label("Indflytningsdato:");
		Label l4 = new Label("Telefonnummer:");
		Label l5 = new Label("Uddannelsessted");
		Label l6 = new Label("Uddannelsesretning");
		Label l7 = new Label("Uddannelse påbegyndt");
		Label l8 = new Label("Forventet uddannelsesafslutning");
		Label l9 = new Label("Lejeaftalens udløb:");

		TextField værelse = new TextField();
		TextField navn = new TextField();
		DatePicker indflytningsdato = new DatePicker();
		TextField telefonnummer = new TextField();
		TextField uddannelsessted = new TextField();
		TextField uddannelsesretning = new TextField();
		DatePicker uddStart = new DatePicker();
		DatePicker uddSlut = new DatePicker();
		DatePicker lejeaftalensUdløb = new DatePicker();

		Button gemButton = new Button("Opret beboer");
		gemButton.setOnAction(e -> {
			Beboer b = new Beboer(navn.getText(), værelse.getText(), indflytningsdato.getValue(),
					lejeaftalensUdløb.getValue(), telefonnummer.getText(), Studiekontrolstatus.IKKEIGANG,
					uddannelsessted.getText(), uddannelsesretning.getText(), uddStart.getValue(), uddSlut.getValue());

			ec.opretFremlejerIExcel(b);
			ec.getFremlejere().clear();
			ec.hentFremlejerFraExcel();
			tView.getItems().add(b);
			tView.refresh();
			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 6);
		layout.add(l3, 3, 9);
		layout.add(l4, 3, 12);
		layout.add(l5, 3, 15);
		layout.add(l6, 3, 18);
		layout.add(l7, 3, 21);
		layout.add(l8, 3, 24);
		layout.add(l9, 3, 27);

		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 5, 3);
		layout.add(navn, 5, 6);
		layout.add(indflytningsdato, 5, 9);
		layout.add(telefonnummer, 5, 12);
		layout.add(uddannelsessted, 5, 15);
		layout.add(uddannelsesretning, 5, 18);
		layout.add(uddStart, 5, 21);
		layout.add(uddSlut, 5, 24);
		layout.add(lejeaftalensUdløb, 5, 27);

		// Sætter buttons på layout
		layout.add(gemButton, 3, 33);
		layout.add(annullerButton, 5, 33);
		layout.setPrefSize(500, 700);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();
	}

	@SuppressWarnings("unchecked")
	public void opretDispensation(ExcelConnection ec, TableView<Dispensation> tableView, Dispensation dispensation, boolean rediger) {
		stage.setTitle("Rediger beboeroplysninger");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setVgap(5);
		layout.setHgap(5);

		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Navn:");
		Label l3 = new Label("Begrundelse for dispensation:");
		Label l4 = new Label("Start dato:");
		Label l5 = new Label("Slut dato");
		Label l6 = new Label("Betingelser");
		Label l7 = new Label("Indstillingsformandens navn");


		TextField værelse = new TextField();

		TextField navn = new TextField();
		TextArea begrundelse = new TextArea();
		begrundelse.setMaxSize(250, 100);
		DatePicker startDato = new DatePicker();
		DatePicker slutDato = new DatePicker();
		TextField formandsNavn = new TextField();
		ComboBox<String> studiekontrolStatus = new ComboBox<String>();

		//Text listener
		værelse.textProperty().addListener((observable, oldValue, newValue) -> {
			if(observable.getValue().toString().length()==3){
				Beboer b = ec.findBeboer(observable.getValue().toString());
				navn.setText(b.getNavn());


			}
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
		});
		


		studiekontrolStatus.getItems().addAll("Ikke i gang", "Modtaget, ikke godkendt", "Ikke Modtaget",
				"Sendt til boligselskab", "Godkendt");

		// Venstre side af menuen

		TableColumn<Deadline, LocalDate> hvornårColumn = new TableColumn<Deadline, LocalDate>("Dato");
		hvornårColumn.setCellValueFactory(new PropertyValueFactory<>("hvornår")); // ændr i format så dato bliver:
																					// dd/MM/YYYY
		TableColumn<Deadline, String> hvadColumn = new TableColumn<Deadline, String>("Hvad:");
		hvadColumn.setCellValueFactory(new PropertyValueFactory<>("hvad"));
		TableColumn<Deadline, String> hvemColumn = new TableColumn("Hvem:");
		hvemColumn.setCellValueFactory(new PropertyValueFactory<>("hvem"));

		TableView<Deadline> tView = new TableView<Deadline>();
		// tView.setItems(getDeadlines());

		tView.getColumns().addAll(hvornårColumn, hvadColumn, hvemColumn);

		tView.setRowFactory(tv -> {
			TableRow<Deadline> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

					Deadline clickedRow = row.getItem();
					popUpDead.changeDeadline(clickedRow, ec, tView);
				}
				if (row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					popUpDead.createDeadline(ec, tView);

				}
			});
			return row;
		});

		if(rediger == true) {
			værelse.setText(dispensation.getBeboerVærelse());
			navn.setText(dispensation.getBeboerNavn() + " (Denne del er nu låst)");
			navn.setEditable(false);
			
			begrundelse.setText("Denne del er nu låst");
			begrundelse.setEditable(false);
			startDato.setValue(dispensation.getStartDato());
			slutDato.setValue(dispensation.getSlutDato());
			
			//Finder deadlines og tilføjer dem til 
			ArrayList<Deadline> list = ec.getDeadlines();
			ArrayList<Deadline> display = new ArrayList<Deadline>();
			String disps = dispensation.getDeadlinesNumbers();
			String[] dispnumre = disps.split("\\-");
			
				for(int j = 0; j<dispnumre.length; j++) {
					String s = dispnumre[j];
					for(int i=0; i<list.size(); i++){
						if(s.equals(list.get(i).getID()))
							display.add(list.get(i));
				}
			}
			
			ObservableList<Deadline> deadlines = FXCollections.observableArrayList(display);
			
			tView.getItems().addAll(display);
			
//			formandsNavn.setText(arg0); TODO Her kan der evt. hentes formandsnavn
			//TODO Hent tilhørende deadlines ind
		}
		
		Button tilføjButton = new Button("Tilføj deadline");
		tilføjButton.setOnAction(event -> popUpDead.createDeadline(ec, tView));

		Button fjernButton = new Button("Fjern deadline");
		fjernButton.setOnAction(event -> {
			ObservableList<Deadline> deadlineValgt, alleDeadlines;
			alleDeadlines = tView.getItems();
			deadlineValgt = tView.getSelectionModel().getSelectedItems();
			Deadline d = tView.getSelectionModel().getSelectedItem();
			d.setKlaret(true);
			ec.opretDeadlineIExcel(d);
			deadlineValgt.forEach(alleDeadlines::remove);

		});

		// Bunden af menuen
		Button gemButton = new Button("Opret Dispensation");
		gemButton.setOnAction(e -> {
			ObservableList<Deadline> list = tView.getItems();
			ArrayList<Deadline> listDeads = new ArrayList<Deadline>();
			
			// Deadlines
			for (Deadline d : list) {
				Deadline deadline = new Deadline(d.getHvem(), d.getHvad(), d.getHvornår(), null);
				ec.opretDeadlineIExcel(deadline);
				ec.getDeadlines().clear();
				ec.hentDeadlinesFraExcel();
				listDeads.add(ec.getDeadlines().get(ec.getDeadlines().size() - 1));
			}


			boolean iGang = false;

			if (slutDato.getValue().isAfter(LocalDate.now()) || slutDato.getValue().equals(LocalDate.now())) {
				iGang = true;
				
			}

			Beboer b = ec.findBeboer(værelse.getText());
			Dispensation disp;
			if(rediger == true) {
				
			disp = new Dispensation(b, startDato.getValue(), slutDato.getValue(), iGang, null, listDeads,
					ec);
			} else //TODO Der skal findes frem til hvorfor der ikke oprettes og gemmes en ny
				disp = new Dispensation(b, startDato.getValue(), slutDato.getValue(), iGang, dispensation.getID(), listDeads,
						ec);

			ec.opretDispensationIExcel(disp);
			ec.getDispensationer().clear();
			ec.hentDispensationerFraExcel();
			tableView.getItems().add(disp);
			tableView.refresh();

			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {

			stage.close();
		});

		// Højre layout
		layout.add(l1, 7, 3);
		layout.add(l2, 7, 6);
		layout.add(l3, 7, 9);
		layout.add(l4, 7, 12);
		layout.add(l5, 7, 15);
		layout.add(l7, 7, 18);

		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 9, 3);
		layout.add(navn, 9, 6);
		layout.add(begrundelse, 9, 9);
		layout.add(startDato, 9, 12);
		layout.add(slutDato, 9, 15);
		layout.add(formandsNavn, 9, 18);
		// layout.add(studiekontrolStatus, 9, 30);

		layout.add(l6, 2, 3, 3, 1);
		layout.add(tView, 2, 5, 3, 10);
		layout.add(tilføjButton, 2, 20);
		layout.add(fjernButton, 4, 20);

		// Sætter Bunden af layout
		layout.add(gemButton, 7, 24);
		layout.add(annullerButton, 9, 24);

		layout.setPrefSize(500, 700);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void redigerBeboeroplysninger(Beboer beboer, ExcelConnection ec, TableView<Beboer> tView, boolean fremleje) {
		stage.setTitle("Rediger beboeroplysninger");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setHgap(5);
		layout.setVgap(5);

		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Navn:");
		Label l3 = new Label("Indflytningsdato:");
		Label l4 = new Label("Telefonnummer:");
		Label l5 = new Label("Uddannelsessted");
		Label l6 = new Label("Uddannelsesretning");
		Label l7 = new Label("Uddannelse påbegyndt");
		Label l8 = new Label("Forventet uddannelsesafslutning");
		Label l9 = new Label("Lejeaftalens udløb:");
		Label l10 = new Label("Status på studiekontrol");

		TextField værelse = new TextField();
		TextField navn = new TextField();
		DatePicker indflytningsdato = new DatePicker();
		TextField telefonnummer = new TextField();
		TextField uddannelsessted = new TextField();
		TextField uddannelsesretning = new TextField();
		DatePicker uddStart = new DatePicker();
		DatePicker uddSlut = new DatePicker();
		DatePicker lejeaftalensUdløb = new DatePicker();
		ComboBox<String> studiekontrolStatus = new ComboBox<String>();
		studiekontrolStatus.getItems().addAll("Ikke i gang", "Modtaget, ikke godkendt", "Ikke Modtaget",
				"Sendt til boligselskab", "Godkendt");

		værelse.setText(beboer.getVærelse());
		navn.setText(beboer.getNavn());
		indflytningsdato.setValue(beboer.getIndflytningsdato());
		telefonnummer.setText(beboer.getTelefonnummer());
		uddannelsessted.setText(beboer.getUddannelsessted());
		uddannelsesretning.setText(beboer.getUddannelsesretning());
		uddStart.setValue(beboer.getPåbegyndtDato());
		uddSlut.setValue(beboer.getForventetAfsluttetDato());
		lejeaftalensUdløb.setValue(beboer.getLejeaftalensUdløb());

		studiekontrolStatus.setValue(ec.konverterEnumTilString((Studiekontrolstatus) beboer.getStudiekontrolstatus()));

		Button gemButton = new Button("Gem ændringer");
		gemButton.setOnAction(e -> {
			Studiekontrolstatus status = (Studiekontrolstatus) ec
					.konverterStringTilEnum(studiekontrolStatus.getValue());
			beboer.setStudiekontrolstatus(status);

			beboer.setNavn(navn.getText());
			beboer.setVærelse(værelse.getText());
			beboer.setIndflytningsdato(indflytningsdato.getValue());
			beboer.setLejeaftalensUdløb(lejeaftalensUdløb.getValue());
			beboer.setTelefonnummer(telefonnummer.getText());

			beboer.setUddannelsessted(uddannelsessted.getText());
			beboer.setUddannelsesretning(uddannelsesretning.getText());
			beboer.setPåbegyndtDato(uddStart.getValue());
			beboer.setForventetAfsluttetDato(uddSlut.getValue());

			ec.opretBeboerIExcel(beboer);
			ec.getBeboere().clear();
			ec.hentBeboereFraExcel();
			
			if(status == Studiekontrolstatus.GODKENDT) {
				ObservableList<Beboer> beboerValgt, alleSKBeboer;
				alleSKBeboer = tView.getItems();
				beboerValgt = tView.getSelectionModel().getSelectedItems();
				beboerValgt.forEach(alleSKBeboer::remove);	
			}
			
			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 6);
		layout.add(l3, 3, 9);
		layout.add(l4, 3, 12);
		layout.add(l5, 3, 15);
		layout.add(l6, 3, 18);
		layout.add(l7, 3, 21);
		layout.add(l8, 3, 24);
		layout.add(l9, 3, 27);
		if (fremleje != true) {
			layout.add(l10, 3, 30);
			layout.add(studiekontrolStatus, 5, 30);
		}
		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 5, 3);
		layout.add(navn, 5, 6);
		layout.add(indflytningsdato, 5, 9);
		layout.add(telefonnummer, 5, 12);
		layout.add(uddannelsessted, 5, 15);
		layout.add(uddannelsesretning, 5, 18);
		layout.add(uddStart, 5, 21);
		layout.add(uddSlut, 5, 24);
		layout.add(lejeaftalensUdløb, 5, 27);

		// Sætter buttons på layout
		layout.add(gemButton, 3, 33);
		layout.add(annullerButton, 5, 33);
		layout.setPrefSize(500, 700);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void startStudiekontrol(TableView<Beboer> tView, ExcelConnection ec, TabPane tP) {
		stage.setTitle("Påbegynd studiekontrol");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setPrefSize(600, 300);
		layout.setHgap(5);
		layout.setVgap(5);

		// Valgmuligheder og labels for at påbegynde studiekontrol oprettes

		Label l1 = new Label("Vælg måned der påbegyndes\nstudiekontrol for:");
		Label l2 = new Label("Startdato");
		Label l3 = new Label("Påmindelsesdato");
		Label l4 = new Label("Afleveringsfrist/Afslutningsdato");

		DatePicker startDato = new DatePicker();
		startDato.setValue(LocalDate.now());
		DatePicker påmindelsesdato = new DatePicker();
		DatePicker afleveringsfrist = new DatePicker();
		ComboBox<String> udløbsmåned = new ComboBox<String>();
		udløbsmåned.getItems().addAll("Januar", "Februar", "Marts", "April", "Maj", "Juni", "Juli", "August",
				"September", "Oktober", "November", "December");
		udløbsmåned.setValue(ec.findMånedsNavn((LocalDate.now().getMonthValue() + 4) % 12));

		Button påbegyndButton = new Button("Påbegynd studiekontrol");
		påbegyndButton.setOnAction(e -> {
			// TODO Skal også skrives til Word eller excel

			ArrayList<Beboer> temp = ec
					.findBeboereTilOpretStudiekontrol(ec.findMånedsNummer(udløbsmåned.getValue().toString()));

			Studiekontrol studiekontrol = new Studiekontrol(temp, afleveringsfrist.getValue(),
					påmindelsesdato.getValue(), startDato.getValue(),
					ec.findMånedsNummer(udløbsmåned.getValue().toString()), false, null);
			
			ec.opretStudiekontrollerIExcel(studiekontrol);
			ec.getStudiekontroller().clear();// Eller add
			ec.hentStudiekontrollerfraExcel();
			tView.getItems().addAll(
					ec.findBeboereTilOpretStudiekontrol(ec.findMånedsNummer(udløbsmåned.getValue().toString())));
			tView.refresh();//TODO BEHØVES DEN?
			Tab t = StudiekontrolMenu.opretStudiekontrolTab(ec.getStudiekontroller().get(ec.getStudiekontroller().size()-1));
			tP.getTabs().add(t);
			
			//laver deadlines til hovedmenu:
			String påmind = "Påmind beboere der indgår i " + udløbsmåned.getValue().toString() + "s studiekontrol om at de skal aflevere studiedokumentaion senest d. " + afleveringsfrist.getValue().toString();
			String afslut = " Afslut studiekontrol for " + udløbsmåned.getValue().toString();

			//TODO der skker måske noget her? -
			Deadline dPåmind = new Deadline("Indstillingen", påmind, påmindelsesdato.getValue(), null);
			ec.opretDeadlineIExcel(dPåmind); 
			ec.getDeadlines().clear();
			ec.hentDeadlinesFraExcel();
			
			Deadline dAfslut = new Deadline("Indstillingen", afslut, afleveringsfrist.getValue(), null);
			ec.opretDeadlineIExcel(dAfslut);
			ec.getDeadlines().clear();
			ec.hentDeadlinesFraExcel();
			
			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> stage.close());
		layout.add(l1, 3, 3);
		layout.add(l2, 6, 3);
		layout.add(l3, 9, 3);
		layout.add(l4, 12, 3);

		layout.add(udløbsmåned, 3, 5);
		layout.add(startDato, 6, 5);
		layout.add(påmindelsesdato, 9, 5);
		layout.add(afleveringsfrist, 12, 5);

		layout.add(påbegyndButton, 9, 7);
		layout.add(annullerButton, 12, 7);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * popUp menu til at tjekke studiekontrol af.
	 */
	public void modtagetStudiekontrol() {
		stage.setTitle("Studiekontrol for beboer:");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setHgap(5);
		layout.setVgap(5);
		Label l1 = new Label("Værelse");
		Label l2 = new Label("Navn");
		Label l3 = new Label("Uddannelsessted");
		Label l4 = new Label("Uddannelsesretning");
		Label l5 = new Label("uddannelse påbegyndt:");
		Label l6 = new Label("Forventet studieafslutning:");
		Label l7 = new Label("Status på studiedokumentation");

		TextField værelse = new TextField();
		TextField navn = new TextField();
		TextField uddannelsessted = new TextField();
		TextField uddannelsesretning = new TextField();
		DatePicker uddStart = new DatePicker();
		DatePicker uddSlut = new DatePicker();
		ComboBox<String> kontrolstatus = new ComboBox<String>();
		kontrolstatus.getItems().addAll("Godkendt", "Modtaget. Ikke godkendt", "Ikke modtaget");
		kontrolstatus.setPromptText("Status...");

		Button gemButton = new Button("Gem ændringer");
		Button annulerButton = new Button("Annuller");

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 5);
		layout.add(l3, 3, 7);
		layout.add(l4, 3, 9);
		layout.add(l5, 3, 11);
		layout.add(l6, 3, 13);
		layout.add(l7, 3, 15);

		layout.add(værelse, 5, 3);
		layout.add(navn, 5, 5);
		layout.add(uddannelsessted, 5, 7);
		layout.add(uddannelsesretning, 5, 9);
		layout.add(uddStart, 5, 11);
		layout.add(uddSlut, 5, 13);
		layout.add(kontrolstatus, 5, 15);

		layout.add(gemButton, 4, 17);
		layout.add(annulerButton, 6, 17);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
	}

	public void afslutStudiekontrol(Studiekontrol studiekontrol, ExcelConnection ec, TableView<Beboer> tView, Tab t) {
		stage.setTitle("Afslut studiekontrol for " + studiekontrol.getStudiekontrolID());
//		 stage.initModality(Modality.APPLICATION_MODAL);
		 GridPane layout = new GridPane();
		 layout.setHgap(5);
		 layout.setVgap(5);
		 
		TableColumn<Beboer, String> værelse = new TableColumn<Beboer, String>("Værelse");
		værelse.setCellValueFactory(new PropertyValueFactory<>("værelse"));
		TableColumn<Beboer, String> navn = new TableColumn<Beboer, String>("Navn");
		navn.setCellValueFactory(new PropertyValueFactory<>("navn"));
		TableColumn<Beboer, String> studiekontrolStatus = new TableColumn<Beboer, String>("Status på studiekontrol");
		studiekontrolStatus.setCellValueFactory(new PropertyValueFactory<>("statusPåStudiekontrol"));

		TableView<Beboer> tView2 = new TableView();
		tView2.getColumns().addAll(værelse, navn, studiekontrolStatus);
		
		Label l = new Label("Hvor skal filen sendes til?");
		TextField filepath = new TextField();
		Button findFilButton = new Button("Vælg filplacering");
		findFilButton.setOnAction(event -> {
			DirectoryChooser dir = new DirectoryChooser();
			dir.setTitle("Vælg filplacering");
			File excelplacering = dir.showDialog(stage);
			filepath.setText(excelplacering.getAbsolutePath());

		});
		//TODO kan laves til separat metode
		ObservableList<Beboer> beboere = tView.getItems();
		ObservableList<Beboer> beboereOpsiges = FXCollections.observableArrayList();
		ArrayList<Beboer> beboereTilFil = new ArrayList<Beboer>();
		ObservableList<Beboer> beboereOpsigesIkke = FXCollections.observableArrayList();

		for(Beboer b: beboere) {
			if(b.getStudiekontrolstatus()!= Studiekontrolstatus.GODKENDT ) { //&&Status != IKKEIGANG
				beboereOpsiges.add(b);
			beboereTilFil.add(b);
			} else
				beboereOpsigesIkke.add(b);
		}
		tView2.getItems().addAll(beboereOpsiges);
		
		Label l1 = new Label("Er du sikker på du vil afslutte studiekontrollen for " + studiekontrol.getStudiekontrolID()
				+ "\nog sende de herunder listede beboeres oplysninger videre til boligselskabet?");
		
		Button jaButton = new Button("Ja");
		jaButton.setOnAction(event -> {
			// TODO skriv navnene til fil så de kan sendes til KAB - ÆNDR alle deres
			ec.createStudiekontrolsExcelFile(filepath.getText()+ "\\beboereOpsiges.xlsx", beboereTilFil);

			// TODO REDIGER studiekontrolsobjektet til afsluttet i excel

			String måned = t.getText();
			t.setText(måned + "(afsluttet)");
			studiekontrol.setAfsluttet(true);
			ec.opretStudiekontrollerIExcel(studiekontrol);
			//Opdaterer i Excel
			for (Beboer b: beboereOpsigesIkke) {
				b.setStudiekontrolstatus(Studiekontrolstatus.IKKEIGANG);
				ec.opretBeboerIExcel(b);
			}
			for(Beboer b: beboereOpsiges) {
				b.setStudiekontrolstatus(Studiekontrolstatus.SENDTTILBOLIGSELSKAB);
				ec.opretBeboerIExcel(b);
			}
			ec.getBeboere().clear();
			ec.hentBeboereFraExcel();
			tView.refresh();
			
			stage.close();
		});
		Button nejButton = new Button("Nej");
		nejButton.setOnAction(event -> stage.close());

		

		
		
		layout.add(l1, 3, 3, 5, 1);
		layout.add(tView2, 3, 4, 3, 2);
		layout.add(jaButton, 4, 7);
		layout.add(nejButton, 6, 7);
		
		VBox vb = new VBox(l, filepath);
		//Højre side //QuickFix
		VBox vbh = new VBox(findFilButton);
		vbh.setPadding(new Insets(20,0,0,0));
		
		HBox hb = new HBox(vb, vbh);
		layout.add(hb, 8, 5);
//		layout.add(findFilButton, 9, 5);
//		layout.add(filepath, 8, 5);
		
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
	}

	public void opretLedigtVærelse(ExcelConnection ec, TableView<Værelsesudlejning> tView) {
		stage.setTitle("Rediger beboeroplysninger");
//		 stage.initModality(Modality.APPLICATION_MODAL);

		GridPane layout = new GridPane();
		layout.setVgap(5);
		layout.setHgap(5);
		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Overtagelsesdato:");

		TextField værelse = new TextField();
		DatePicker overtagelsesdato = new DatePicker();

		Button opretButton = new Button("Gem ændringer");
		opretButton.setOnAction(e -> {
			Værelsesudlejning vu = new Værelsesudlejning(overtagelsesdato.getValue(), værelse.getText(), null, null,
					null, null, ec);

			System.out.println(overtagelsesdato.getValue().getMonthValue() + "!!!!!!!!!!!!!!!!!!!!");
			ec.opretVærelsesudlejningIExcel(vu);
			ec.getVærelsesudlejning().clear();
			ec.hentVærelsesudlejningFraExcel();
			tView.getItems().add(vu);

			//Opretter deadline til hovedmenu
			String hvem = "Indstillingen";
			String hvad = "Udlej værelse " + værelse.getText() + " til d. " + overtagelsesdato.getValue().toString();
			LocalDate hvornår = overtagelsesdato.getValue().minusDays(14);
			System.out.println("Jeg kommer tilbage hertil " + hvornår.toString() + hvem + hvad);
			Deadline d = new Deadline(hvem , hvad, hvornår, null);
			ec.opretDeadlineIExcel(d);
			ec.getDeadlines().clear();
			ec.hentDeadlinesFraExcel();
			
			
			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 6);

		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 5, 3);
		layout.add(overtagelsesdato, 5, 6);

		// Sætter buttons på layout
		layout.add(opretButton, 3, 9);
		layout.add(annullerButton, 5, 9);
		layout.setPrefSize(500, 200);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.show();
	}

	public void udfyldLedigtVærelse(ExcelConnection ec, TableView<Værelsesudlejning> tView1,
                                    TableView<Værelsesudlejning> tView2, Værelsesudlejning værelsesudlejning, boolean retBeboerOplysninger) {
		
		GridPane layout = new GridPane();
		layout.setVgap(5);
		layout.setHgap(5);
//		 stage.initModality(Modality.APPLICATION_MODAL);

		Label l1 = new Label("Værelse:");
		Label l2 = new Label("Navn:");
		Label l3 = new Label("Overtagelsesdato:");
		Label l4 = new Label("Behandlingsdato:");
		Label l5 = new Label("behandler initialer:");
		Label l6 = new Label("Telefonnummer:");
		Label l7 = new Label("Uddannelsessted");
		Label l8 = new Label("Uddannelsesretning");
		Label l9 = new Label("Uddannelse påbegyndt");
		Label l10 = new Label("Forventet uddannelsesafslutning");
		Label l11 = new Label("Lejeaftalens udløb:");
		Label l12 = new Label("Studiekontrol status:");// Bør der her være mulighed for at sige at beboeren ikke
														// allerede er optaget på studie

		TextField værelse = new TextField();
		værelse.setText(værelsesudlejning.getVærelse());
		TextField navn = new TextField();
		DatePicker indflytningsdato = new DatePicker();
		indflytningsdato.setValue(værelsesudlejning.getIndflytningsdato());
		DatePicker behandlingsdato = new DatePicker();
		behandlingsdato.setValue(LocalDate.now()); // Evt. lad være tom
		TextField behandlerInit = new TextField();
		TextField telefonnummer = new TextField();
		TextField uddannelsessted = new TextField();
		TextField uddannelsesretning = new TextField();
		DatePicker uddStart = new DatePicker();
		DatePicker uddSlut = new DatePicker();
		DatePicker lejeaftalensUdløb = new DatePicker();
		ComboBox<String> studiekontrolStatus = new ComboBox<String>();
		studiekontrolStatus.getItems().addAll("Ikke i gang", "Modtaget, ikke godkendt", "Ikke Modtaget",
				"Sendt til boligselskab", "Godkendt");
		if(retBeboerOplysninger == true) {
			//TODO Udfyld de forskellige info
			Beboer b = ec.findBeboer(værelse.getText());
			navn.setText(b.getNavn());
			indflytningsdato.setValue(b.getIndflytningsdato());
			behandlingsdato.setValue(værelsesudlejning.getBehandlingsdato());
			behandlerInit.setText(værelsesudlejning.getBehandlerInitialer());
			telefonnummer.setText(b.getTelefonnummer());
			uddannelsessted.setText(b.getUddannelsessted());
			uddannelsesretning.setText(b.getUddannelsesretning());
			uddStart.setValue(b.getPåbegyndtDato());
			uddSlut.setValue(b.getForventetAfsluttetDato());
			lejeaftalensUdløb.setValue(b.getLejeaftalensUdløb());
			studiekontrolStatus.setValue(b.getStatusPåStudiekontrol());
		}
		
		Button opretButton = new Button("Gem ændringer");
		opretButton.setOnAction(e -> {
			Værelsesudlejning vu;
			//tjekker at værelsesnummer ikke er ændret
//			if(værelsesudlejning.getNavn().equals(værelse.getText())) {
			vu = new Værelsesudlejning(indflytningsdato.getValue(), værelse.getText(), navn.getText(),
					behandlingsdato.getValue(), behandlerInit.getText(), værelsesudlejning.getID() , null);
//			}
//			else {
//				vu = new Værelsesudlejning(indflytningsdato.getValue(), værelse.getText(), navn.getText(),
//						behandlingsdato.getValue(), behandlerInit.getText(), null , ec);
//			}
			
			ec.opretVærelsesudlejningIExcel(vu);
			ec.getVærelsesudlejning().clear();
			ec.hentVærelsesudlejningFraExcel();
			
//			Fjerner fra første TableView
			ObservableList<Værelsesudlejning> værelseValgt, alleVærelser;
			alleVærelser = tView1.getItems();
			værelseValgt = tView1.getSelectionModel().getSelectedItems();
			værelseValgt.forEach(alleVærelser::remove);
			
			
			tView1.getItems().remove(vu);// evt. fremgangsmåde som vist ved
			tView1.refresh();
			tView2.getItems().add(vu);
			tView2.refresh();
			// Lejeaftalens udløb skal beregnes et sted.
			Beboer b = new Beboer(navn.getText(), værelse.getText(), indflytningsdato.getValue(),
					lejeaftalensUdløb.getValue(), telefonnummer.getText(),
					ec.konverterStringTilEnum(studiekontrolStatus.getValue()), uddannelsessted.getText(),
					uddannelsesretning.getText(), uddStart.getValue(), uddSlut.getValue());

			System.out.println(b.getNavn() + " bor på " + b.getVærelse());
			ec.opretBeboerIExcel(b);
			ec.getBeboere().clear();
			ec.hentBeboereFraExcel();
		


			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(l2, 3, 6);
		layout.add(l3, 3, 9);
		layout.add(l4, 3, 12);
		layout.add(l5, 3, 15);
		layout.add(l6, 3, 18);
		layout.add(l7, 3, 21);
		layout.add(l8, 3, 24);
		layout.add(l9, 3, 27);
		layout.add(l10, 3, 30);
		layout.add(l11, 3, 33);
		layout.add(l12, 3, 36);

		// Sætter tekstfelter og datepicker på layout
		layout.add(værelse, 5, 3);
		layout.add(navn, 5, 6);
		layout.add(indflytningsdato, 5, 9);
		layout.add(behandlingsdato, 5, 12);
		layout.add(behandlerInit, 5, 15);
		layout.add(telefonnummer, 5, 18);
		layout.add(uddannelsessted, 5, 21);
		layout.add(uddannelsesretning, 5, 24);
		layout.add(uddStart, 5, 27);
		layout.add(uddSlut, 5, 30);
		layout.add(lejeaftalensUdløb, 5, 33);
		layout.add(studiekontrolStatus, 5, 36);

		// Sætter buttons på layout
		layout.add(opretButton, 3, 39);
		layout.add(annullerButton, 5, 39);
		layout.setPrefSize(500, 670);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();

	}
	private void opretVærelsesButtonClicked(){

	}

}
