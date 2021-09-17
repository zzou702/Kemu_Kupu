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
import application.helpers.*;

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
	
	@FXML
	private Label rewardLabel;
	
	
	public void switchHomeFromReward(ActionEvent e) throws IOException { //Switches back to home screen on button press
		root = FXMLLoader.load(getClass().getResource("A2Doc.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//the method changes to reward scene
	public void switchToReward(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("rewardScene.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		
		//count only for testing
		int count = 4;
		
		//two different messages
		if (count > 3) {
			rewardLabel.setText("Congragulations! You have scored " + count + " out of 5");
		}else if (count <= 3) {
			rewardLabel.setText("You have scored " + count + " out of 5, you can definitely do better");
		}
		
		stage.show();
	}
	
	

	public void gameStart(ActionEvent e) { //Method that changes to the new game screen on button press

		try {
			File fileTest = new File(wordFile); //Tests if the file with the words exists
			if (!fileTest.exists()) {
				errorMsg.setText("No words to review");
			}

			else {	
				String command = "sort -u " + wordFile + " | shuf -n 5 > " + uniqueFile; //Sorts the word lists for unique words and then picks a maximum of 5 random words and adds to file
				Bash.exec(command);

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



	public void readWord() { //Method reads the first line of a given file
		try {

			File testWords = new File(uniqueFile);
			Scanner reader = new Scanner(testWords);

			if (reader.hasNextLine()) {
				String word = reader.nextLine();
				Festival.speak(word, Festival.Language.ENGLISH);
			}

			reader.close();
		}

		catch (Exception f) {
			f.printStackTrace();
		}
	}
}
