package view.popups;

import controller.ExcelConnection;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Deadline;
import view.main.GUI;

import java.util.ArrayList;

public class GUI_PopUps_Deadlines {
	private Stage stage = new Stage();
	private ArrayList<Deadline> list = new ArrayList<Deadline>();

	public ArrayList<Deadline> getList() {
		return list;
	}
	public void setList(ArrayList<Deadline> list) {
		this.list = list;
	}
	public void createDeadline(ExcelConnection ec, TableView<Deadline> tView) {

		// stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Opret Deadline");

		Label l1 = new Label("Hvem?");
		Label l2 = new Label("Hvad?");
		Label l3 = new Label("Hvornår?");
		DatePicker hvornår = new DatePicker();
		TextField hvad = new TextField();
		TextField hvem = new TextField();

		Button opretButton = new Button("Tilføj påmindelse");
		opretButton.setOnAction(e -> {
			Deadline d = new Deadline(hvem.getText(), hvad.getText(), hvornår.getValue(), null, ec);
			ec.opretDeadlineIExcel(d);
			//tView.getItems().add(d); //TODO this makes it fail
			list.add(d);
			//TableView tv = new TableView();
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					tView.setItems(GUI.getDeadlines());
				}
			});

			//tView.refresh();
			//TODO sæt opdatering i gui i gang
			stage.close();
		});
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(e -> { 
		
			stage.close();
		
		});

		GridPane layout = new GridPane();
		layout.add(l1, 3, 3);
		layout.add(hvem, 3, 5);
		layout.add(l2, 5, 3);
		layout.add(hvad, 5, 5);
		layout.add(l3, 7, 3);
		layout.add(hvornår, 7, 5);
		layout.add(opretButton, 5, 7);
		layout.add(annullerButton, 7, 7);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();
	}
	public void changeDeadline(Deadline deadline, ExcelConnection ec, TableView<Deadline> tView) {

		stage.setTitle("Rediger deadline");

		GridPane layout = new GridPane();

		Label l1 = new Label("Hvem?");
		Label l2 = new Label("Hvad?");
		Label l3 = new Label("Hvornår?");

		DatePicker hvornår = new DatePicker();
		hvornår.setValue(deadline.getHvornår());
		TextArea hvad = new TextArea();
		hvad.setText(deadline.getHvad());
		TextField hvem = new TextField();
		hvem.setText(deadline.getHvem());

		Button ændrButton = new Button("Gem ændringer");
		ændrButton.setOnAction(event -> {
			deadline.setHvad(hvad.getText());
			deadline.setHvornår(hvornår.getValue());
			deadline.setHvem(hvem.getText());
			ec.opretDeadlineIExcel(deadline);
			tView.refresh();
			stage.close();

		});
		Button annullerButton = new Button("Anuller");
		annullerButton.setOnAction(e -> {
			stage.close();
		});

		layout.add(l1, 3, 3);
		layout.add(hvem, 3, 5);
		layout.add(l2, 5, 3);
		layout.add(hvad, 5, 5);
		layout.add(l3, 7, 3);
		layout.add(hvornår, 7, 5);
		layout.add(ændrButton, 7, 7);
		layout.add(annullerButton, 5, 7);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();
	}
//	public Object createDeadlineWithoutAdding(ExcelConnection ec, TableView<Deadline> tView, ) {
//
//		// stage.initModality(Modality.APPLICATION_MODAL);
//		stage.setTitle("Opret Deadline");
//
//		Label l1 = new Label("Hvem?");
//		Label l2 = new Label("Hvad?");
//		Label l3 = new Label("Hvornår?");
//		DatePicker hvornår = new DatePicker();
//		TextField hvad = new TextField();
//		TextField hvem = new TextField();
//
//		Button opretButton = new Button("Tilføj påmindelse");
//		opretButton.setOnAction(e -> {
//			Deadline d = new Deadline(hvem.getText(), hvad.getText(), hvornår.getValue(), null, ec);// ec. get
//			ec.opretDeadlineIExcel(d);
//			ec.getDeadlines().clear();
//			ec.hentDeadlinesFraExcel();
//			this.list.add(d);
//			tView.getItems().add(d);
//			stage.close();
//		});
//		Button annullerButton = new Button("Annuller");
//		annullerButton.setOnAction(e -> {
//			for(Deadline d: list) {
//				d.setKlaret(true);
//			}
//			stage.close();
//		});
//
//		GridPane layout = new GridPane();
//		layout.add(l1, 3, 3);
//		layout.add(hvem, 3, 5);
//		layout.add(l2, 5, 3);
//		layout.add(hvad, 5, 5);
//		layout.add(l3, 7, 3);
//		layout.add(hvornår, 7, 5);
//		layout.add(opretButton, 5, 7);
//		layout.add(annullerButton, 7, 7);
//
//		Scene scene = new Scene(layout);
//		stage.setScene(scene);
//		stage.showAndWait();
//		return null;
//	}

}
