package application.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** all this boilerplate code is required for the table on the Reward page */
public class AnswerTableModel {

	private SimpleStringProperty teReo;
	private SimpleStringProperty english;
	private SimpleStringProperty status;

	public AnswerTableModel(String teReo, String english, String status) {
		this.teReo = new SimpleStringProperty(teReo);
		this.english = new SimpleStringProperty(english);
		this.status = new SimpleStringProperty(status);
	}

	public String getTeReo() {
		return teReo.get();
	}

	public void setTeReo(String word) {
		this.teReo.set(word);
	}

	public StringProperty teReoProperty() {
		return teReo;
	}

	public String getEnglish() {
		return english.get();
	}

	public void setEnglish(String english) {
		this.english.set(english);
	}

	public StringProperty englishProperty() {
		return english;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public StringProperty statusProperty() {
		return status;
	}
}
