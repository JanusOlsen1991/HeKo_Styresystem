package view.main;

import controller.excel.ExcelConnection;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import view.GuiSingleton;

import java.io.*;


public class GUI {
	private Scene scene;
	static GuiSingleton gui;

	public GUI() {
		gui = GuiSingleton.getInstance();
	}
	public String excel;



	public void start(Stage primaryStage) {

		GridPane gP = new GridPane();
		Label l = new Label("Filplacering:");
		TextField text = new TextField();

		excel = gui.filplaceringer.getPath(gui.excel);
		text.setText(excel);

		Button findFilButton = new Button("Ændr filplacering");
		findFilButton.setOnAction(event -> {
			DirectoryChooser dir = new DirectoryChooser();
			dir.setTitle("Vælg filplacering");
			File excelplacering = dir.showDialog(primaryStage);
			text.setText(excelplacering.getAbsolutePath() + "\\IndstillingsInfo.xlsx");

		});

		Button startButton = new Button("Start");
		startButton.setOnAction(event -> startButtonClicked(text.getText(), primaryStage));
		Button annullerButton = new Button("Annuller");
		annullerButton.setOnAction(event -> primaryStage.close());

		gP.add(l, 3, 3);
		gP.add(text, 3, 4);
		// gP.add(labelFil, 5, 4);
		gP.add(findFilButton, 4, 4);
		gP.add(startButton, 3, 5);
		gP.add(annullerButton, 4, 5);

		scene = new Scene(gP);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private void startButtonClicked(String text, Stage primaryStage) {
		gui.filplaceringer.updateFilePath("excel", text);

			try {
				gui.ec = new ExcelConnection(text);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

				gui.hovedMenu.hovedMenu(primaryStage);
	}

	public Scene getScene() {
		return scene;
	}

}
