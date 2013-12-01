package de.cofinpro.acc;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class SceneLoader {

	private FXMLLoader loader;
	private Scene scene;

	public void load(String fxml) {
		loader = new FXMLLoader();
		InputStream inputStream = JavaFxMain.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(JavaFxMain.class.getResource(fxml));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		scene = new Scene(page);
	}

	public <T> T getController() {
		return loader.getController();
	}

	public Scene getScene() {
		return scene;
	}
	
}
