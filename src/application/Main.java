package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the entry point to the program
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			// we start on the Home page
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml")); // Sets reference FXML
			Scene scene = new Scene(root);
			scene
				.getStylesheets()
				.add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false); // Disables resizing of window
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
