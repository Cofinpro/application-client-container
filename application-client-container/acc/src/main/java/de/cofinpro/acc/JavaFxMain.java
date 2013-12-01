package de.cofinpro.acc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("CreateDepot.fxml");
		Scene scene = sceneLoader.getScene();
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}

}
