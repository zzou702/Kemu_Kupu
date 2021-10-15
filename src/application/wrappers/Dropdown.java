package application.wrappers;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public abstract class Dropdown<T> {

	/**
	 * a class to make JavaFX's ComboBox more convenient to initialize.
	 *
	 * T is the generic type for the key
	 * @param comboBox the JavaFX element
	 * @param values the text to be shown to the user
	 * @param keys the internal ID
	 * @param initialValue
	 */
	public Dropdown(
		ComboBox<String> comboBox,
		ObservableList<String> values,
		T[] keys,
		T initialValue
	) {
		// populate the list of values
		comboBox.setItems(values);

		// set the initial value of the ComboBox to be the current TTSSpeed
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].equals(initialValue)) {
				comboBox.setValue(values.get(i));
			}
		}

		// register the onChange callback
		comboBox.setOnAction(event -> {
			int index = comboBox.getSelectionModel().getSelectedIndex();

			// when we update the language of the labels, JavaFX calls setOnAction with -1
			if (index == -1) return;

			onChange(keys[index]);
		});
	}

	/** called when a value is changed */
	public abstract void onChange(T newValue);
}
