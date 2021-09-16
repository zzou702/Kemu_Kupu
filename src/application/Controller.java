package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Controller {
	private Stage stage; //Declaration of core UI components
	private Scene scene;
	private Parent root;

	private String wordFile = "src/words/popular"; //Declaration of file paths
	private String uniqueFile = "src/words/.new";
	private String reviewFile = "src/words/.failed";
	private String reviewFileTemp = "src/words/.failed.tmp";
	private String statsFile = "src/words/.stats";
	private String faultedFile = "src/words/.faulted";
	private String masteredFile = "src/words/.mastered";

	@FXML //Declaration of widgets made in SceneBuilder
	private Label errorMsg;

	public void gameStart(ActionEvent e) { //Method that changes to the new game screen on button press

		try {
			File fileTest = new File(wordFile); //Tests if the file with the words exists
			if (!fileTest.exists()) {
				errorMsg.setText("No words to review");
			}

			else {	
				String command = "sort -u " + wordFile + " | shuf -n 3 > " + uniqueFile; //Sorts the word lists for unique words and then picks a maximum of 3 random words and adds to file
				buildProcess(command);

				String wordCountCommand = "wc -l " + uniqueFile; //Counts the number of words to be tested
				Process wordCountProcess = new ProcessBuilder("bash", "-c", wordCountCommand).start();
				Reader rdr = new InputStreamReader(wordCountProcess.getInputStream());
				int wordCountTotal = Character.getNumericValue(rdr.read());
				rdr.close();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("gameScene.fxml")); //Changes the reference FXML file
				root = loader.load();

				GameController newGame = loader.getController(); //Creates new instance of the controller for the corresponding scene
				newGame.displayCount(wordCountTotal); //Calls methods from other controller
				newGame.displayTitle("Spelling");

				stage = (Stage)((Node)e.getSource()).getScene().getWindow(); //Changes scenes to game scene
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

				readWord(); 
			}
		}

		catch (Exception f) {
			f.printStackTrace();
		}

	}


	public void gameReview(ActionEvent e) { //Method that changes to the new review screen on button press

		try {
			File fileTest = new File(reviewFile);

			if (!fileTest.exists()) {
				errorMsg.setText("No words to test");
			}

			else {
				String failedWords = "sort -u " + reviewFile + " > " + reviewFileTemp + " && mv " + reviewFileTemp + " " + reviewFile; //Removes duplicates from the file of words to be reviewed
				buildProcess(failedWords);

				String command = "shuf -n 3 " + reviewFile + " > " + uniqueFile; //Picks maximum 3 random words from words to be reviewed and adds to file
				buildProcess(command);

				String wordCountCommand = "wc -l " + uniqueFile; //Counts number of words to be tested
				Process wordCountProcess = new ProcessBuilder("bash", "-c", wordCountCommand).start();
				Reader rdr = new InputStreamReader(wordCountProcess.getInputStream());
				int wordCountTotal = Character.getNumericValue(rdr.read());

				FXMLLoader loader = new FXMLLoader(getClass().getResource("gameScene.fxml")); //Changes the reference FXML file
				root = loader.load();

				GameController newGame = loader.getController(); //Creates new instance of the controller for the corresponding scene
				newGame.displayCount(wordCountTotal); //Calls methods from other controller
				newGame.displayTitle("Review");

				stage = (Stage)((Node)e.getSource()).getScene().getWindow(); //Changes to review scene
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

				readWord();
			}
		}

		catch (Exception f) {
			f.printStackTrace();
		}

	}

	public void gameStats(ActionEvent e){ //Changes to scene that displays stats on button press

		try {
			File fileTest = new File(statsFile); //Tests if stats file exists

			if (!fileTest.exists()) {
				errorMsg.setText("No stats to show");
			}

			else { //Switches to stats scene
				root = FXMLLoader.load(getClass().getResource("statsScene.fxml"));
				stage = (Stage)((Node)e.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}

		catch (Exception f) {
			f.printStackTrace();
		}
	}

	public void gameClear(ActionEvent e) { //Clears all statistics on button press

		String clear = "rm -f " + reviewFile + " " + masteredFile + " " + faultedFile + " " + statsFile; //Deletes all files relating to stats
		buildProcess(clear);
		errorMsg.setText("Statistics cleared!");
	}

	public void readWord() { //Method reads the first line of a given file
		try {

			File testWords = new File(uniqueFile);
			Scanner reader = new Scanner(testWords);

			if (reader.hasNextLine()) {
				String word = reader.nextLine();
				String readWord = "echo " + word + " | festival --tts";
				buildProcess(readWord);
			}

			reader.close();
		}

		catch (Exception f) {
			f.printStackTrace();
		}
	}

	public void buildProcess(String command) { //Function that creates a process for bash

		try {
			ProcessBuilder processBuild = new ProcessBuilder("bash", "-c", command);
			Process processStart = processBuild.start();
			processStart.waitFor();
		}

		catch (Exception f) {
			f.printStackTrace();
		}
	}	
}
