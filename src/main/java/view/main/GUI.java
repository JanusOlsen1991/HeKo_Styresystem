package view.main;

import controller.ExcelConnection;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import view.GuiSingleton;
import view.popups.GUI_PopUps;
import view.popups.GUI_PopUps_Deadlines;

import java.io.*;


public class GUI {
	private Scene scene;
	static GUI_PopUps popUp = new GUI_PopUps();
	GUI_PopUps_Deadlines popUpDead = new GUI_PopUps_Deadlines();
	static GuiSingleton gui;

	public GUI() {
		gui = GuiSingleton.getInstance();
	}



	public void filplacering(Stage primaryStage) {

		GridPane gP = new GridPane();
		Label l = new Label("Filplacering:");
		TextField text = new TextField();
		// finder fil-stien
		String filename = "Excelplacering.txt";
		String filepath = null;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))){

			filepath = bufferedReader.readLine();
			text.setText(filepath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Button findFilButton = new Button("Ændr filplacering");
		findFilButton.setOnAction(event -> {
			DirectoryChooser dir = new DirectoryChooser();
			dir.setTitle("Vælg filplacering");
			File excelplacering = dir.showDialog(primaryStage);
			text.setText(excelplacering.getAbsolutePath() + "\\IndstillingsInfo.xlsx");

		});

		Button startButton = new Button("Start");
		startButton.setOnAction(event -> startButtonClicked(text, filename, primaryStage));
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

	private void startButtonClicked(TextField text, String filename, Stage primaryStage) {
		{
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

		}
	}

	public Scene getScene() {
		return scene;
	}

}
