package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;



public class StatsController implements Initializable {
	private Stage stage; //Declaration of core UI components
	private Scene scene;
	private Parent root;
	
	private String currentWord; //Declaration of variables
	
	@FXML //Declares widgets created in SceneBuilder
	private Label masteredLabel;
	@FXML
	private Label faultedLabel;
	@FXML
	private Label failedLabel;
	@FXML
	private ListView<String> statsView; 
		
	public void initialize(URL arg0, ResourceBundle arg1) { //Initialises when scene is changed to stats scene
		
		try {
			List<String> wordsList = new ArrayList<String>();
			String statsUrl = "src/words/.stats";
			String getWords = "sort -u " + statsUrl + " | cut -f1 -d\" \" | sort -u"; //Sorts the lines in the stats file, splits it from the space and sorts for unique words
			Process words = new ProcessBuilder("bash", "-c", getWords).start(); 
			BufferedReader rdr = new BufferedReader(new InputStreamReader(words.getInputStream())); //Reader to read the output of this command
			String line;
			
			while ((line = rdr.readLine()) != null) { //Adds all words into arraylist
				   wordsList.add(line);
				}
			rdr.close();
			
			statsView.getItems().addAll(wordsList); //Adds all the words into the list view
			statsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { //Checks for user selecting the word

				@Override
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					
					try {
						currentWord = statsView.getSelectionModel().getSelectedItem(); //Gets the word current selected
						String masteredCount = "grep -cw \"" + currentWord + " mastered\" src/words/.stats"; //Commands to count the number of mastered, faulted and failed words
						String faultedCount ="grep -cw \"" + currentWord + " faulted\" src/words/.stats";
						String failedCount ="grep -cw \"" + currentWord + " failed\" src/words/.stats";
						
						Process masteredProcess = new ProcessBuilder("bash", "-c", masteredCount).start(); //Processes to run and read the command
						BufferedReader masteredRead = new BufferedReader(new InputStreamReader(masteredProcess.getInputStream()));
						
						Process faultedProcess = new ProcessBuilder("bash", "-c", faultedCount).start();
						BufferedReader faultedRead = new BufferedReader(new InputStreamReader(faultedProcess.getInputStream()));
						
						Process failedProcess = new ProcessBuilder("bash", "-c", failedCount).start();
						BufferedReader failedRead = new BufferedReader(new InputStreamReader(failedProcess.getInputStream()));
						
						masteredLabel.setText("Mastered: " + Integer.toString(Character.getNumericValue(masteredRead.read()))); //Changes the labels corresponding to the words and the number of mastered, faulted and failed words.
						faultedLabel.setText("Faulted: " + Integer.toString(Character.getNumericValue(faultedRead.read())));
						failedLabel.setText("Failed: " + Integer.toString(Character.getNumericValue(failedRead.read())));
					}
					catch (Exception f) {
						f.printStackTrace();
					}
				}
			});
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
	
	public void buildProcess(String command) { //Process builder for bash commands
		
		try {
			ProcessBuilder processBuild = new ProcessBuilder("bash", "-c", command);
			Process processStart = processBuild.start();
			processStart.waitFor();
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
	
	public void switchHome(ActionEvent e) throws IOException { //Switches to home on button press
		
		root = FXMLLoader.load(getClass().getResource("A2Doc.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
