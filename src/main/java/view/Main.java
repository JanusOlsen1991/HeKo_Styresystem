package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		ExcelConnection ec = new ExcelConnection("");
		GUI g = new GUI();
	g.filplacering(primaryStage);
	}
}
