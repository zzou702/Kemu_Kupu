package application.helpers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Every Controller extends this class. This means you can access global variables
 * like the TTS speed.
 *
 * It also exposes a navigateTo method which makes it easier to change to a new page.
 */
public abstract class UIController {

	public enum Transition {
		FORWARDS,
		BACKWARDS,
	}

	/** the speed that festival will read the word at */
	public AppContext context;

	/**
	 * no-op by default, but can be overridden for classes that need a callback
	 * like initialize() that runs after the main context has been setup, and FXML
	 * is ready.
	 */
	public void onReady() {}

	// we provide two overloads for convenience. You can either supply an event or a node
	// this is required so that we can get a reference to the current stage.
	public UIController navigateTo(
		String page,
		ActionEvent event,
		Transition transition
	) {
		return navigateTo(page, (Node) event.getSource(), transition);
	}

	public UIController navigateTo(
		String page,
		Node node,
		Transition transition
	) {
		try {
			Pane oldRoot = (Pane) node.getScene().getRoot();

			FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
			Pane newRoot = loader.load();

			// creates new instance of the controller for the corresponding scene
			UIController newController = loader.getController();

			// copy the instance of the AppContext to the new controller
			newController.context = this.context;

			int direction = transition == Transition.FORWARDS ? 1 : -1;

			// initialize the scene to start off-screen
			newRoot.translateXProperty().set(direction * oldRoot.getWidth());
			oldRoot.getChildren().add(newRoot);

			// animate the new scene in
			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(
				newRoot.translateXProperty(),
				0,
				Interpolator.LINEAR
			);
			KeyFrame kf = new KeyFrame(Duration.seconds(0.3), kv);
			timeline.getKeyFrames().add(kf);
			// when the animation is finished, destroy the old root
			timeline.setOnFinished(event -> oldRoot.getChildren().remove(oldRoot));
			timeline.play();
			newController.onReady();

			return newController;
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}
}
