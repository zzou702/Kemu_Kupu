package application.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.util.Duration;

public class FX {

	public enum State {
		SUCCESS,
		WARNING,
		DANGER,
	}

	/** flash this many times */
	public static final int FLASH_COUNTS = 2;

	/**
	 * flashes the background colour of an element.
	 * For this to work, the element must have the class `.flashable`
	 */
	public static Timeline flashElement(Node element, State state) {
		// based on https://stackoverflow.com/a/32168257/5470183
		PseudoClass flashHighlight = PseudoClass.getPseudoClass(
			"flash_" + state.name().toLowerCase()
		);
		Timeline flasher = new Timeline(
			new KeyFrame(
				Duration.seconds(0.25),
				e -> element.pseudoClassStateChanged(flashHighlight, true)
			),
			new KeyFrame(
				Duration.seconds(0.5),
				e -> element.pseudoClassStateChanged(flashHighlight, false)
			)
		);
		flasher.setCycleCount(FLASH_COUNTS);
		flasher.play();
		
		return flasher;
	}
}
