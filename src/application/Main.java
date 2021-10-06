package application;

import application.helpers.*;
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			// this is the first controller, so we need to initialize the AppContext
			UIController initialController = loader.getController();
			initialController.context = new AppContext();

			Scene scene = new Scene(root);
			scene
				.getStylesheets()
				.add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false); // Disables resizing of window
			initialController.onReady();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
