package application.helpers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.util.Duration;

/** static helpers for animations and other special effects */
public class FX {

	/** we use the success/warning/danger/info paradigm for alerts */
	public enum State {
		SUCCESS,
		WARNING,
		DANGER,
	}

	/** flash this many times */
	public static final int FLASH_COUNTS = 3;

	/** in milliseconds */
	public static final int DEFAULT_FADE_TIME = 500;

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

	/**
	 * Methods to fade in and out transition when switching scenes
	 * Heavily based off: https://www.youtube.com/watch?v=rMQrXSYHl8w
	 */
	public static FadeTransition fadeOut(Node pane, int duration) {
		FadeTransition sceneTransitionOut = new FadeTransition();
		sceneTransitionOut.setDuration(Duration.millis(duration));
		sceneTransitionOut.setNode(pane);
		sceneTransitionOut.setFromValue(1);
		sceneTransitionOut.setToValue(0);
		sceneTransitionOut.play();

		return sceneTransitionOut;
	}

	/** extra overload for convenience which uses the default fade time */
	public static FadeTransition fadeOut(Node pane) {
		return fadeOut(pane, DEFAULT_FADE_TIME);
	}

	public static FadeTransition fadeIn(Node pane, int duration) {
		pane.setOpacity(0);
		FadeTransition sceneTransitionIn = new FadeTransition();
		sceneTransitionIn.setDuration(Duration.millis(duration));
		sceneTransitionIn.setNode(pane);
		sceneTransitionIn.setFromValue(0);
		sceneTransitionIn.setToValue(1);
		sceneTransitionIn.play();

		return sceneTransitionIn;
	}

	/** extra overload for convenience which uses the default fade time */
	public static FadeTransition fadeIn(Node pane) {
		return fadeIn(pane, DEFAULT_FADE_TIME);
	}
}
