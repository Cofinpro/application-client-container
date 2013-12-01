package de.cofinpro.acc;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TopicConnection;

public class CreateDepotController extends AnchorPane implements Initializable, MessageListener {

	@FXML
	TextField eMailAddress;
	
	@FXML
	PasswordField password;
	
	@FXML
	Button create;
	
	@FXML
	Label label;
	
	public CreateDepotController() {
		setupJMS();
	}

	private void setupJMS() {
		try {
			TopicConnection connection = Main.connectionFactory.createTopicConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer messageConsumer = session.createConsumer(Main.topic);
			messageConsumer.setMessageListener(this);
			connection.start();
		} catch (JMSException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void checkEMailAddress(KeyEvent event) {
		if(Main.depotStorage.eMailAddressUsed(eMailAddress.getText())) {
			label.setText("Die E-Mail Adresse ist schon registriert.");
			create.setDisable(true);
		}
		else {
			label.setText("");
			create.setDisable(false);
		}
	}
	
	public void createDepot(ActionEvent event) {
		Depot depot = Main.depotStorage.register(eMailAddress.getText(), password.getText());
		if(depot != null) {
			Stage stage = (Stage) label.getScene().getWindow();
			SceneLoader sceneLoader = new SceneLoader();
			sceneLoader.load("TradeView.fxml");
			Scene tradeViewScene = sceneLoader.getScene();
			TradeViewController tradeViewController = sceneLoader.getController();
			tradeViewController.setDepot(depot);
			tradeViewController.updateDepot();
			stage.setScene(tradeViewScene);
		}
		else {
			label.setText("Es ist ein Fehler aufgetreten!");
		}
	}

	public void quit(ActionEvent event) {
		Platform.exit();
	}
	
	@Override
	public void onMessage(Message message) {
		System.out.println("CreateDepotController.onMessage()");
		System.out.println(message);
		try {
			System.out.println("Destination: " + message.getJMSDestination());
			if(message instanceof ObjectMessage) {
				ObjectMessage objectMessage = (ObjectMessage) message;
				Depot depot = (Depot) objectMessage.getObject();
				updateLabel(depot);
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateLabel(final Depot depot) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(label.getText() + " " + "Depot erhalten: " + depot.geteMailAddress());
			}
		});		
	}
	
}
