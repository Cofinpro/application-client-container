package de.cofinpro.acc;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

// TODO Maybe rename to "DepotController"
public class TradeViewController extends AnchorPane implements Initializable, MessageListener {

	@FXML
	Button quit;
	
	@FXML
	Button buy;
	
	@FXML
	Button sell;
	
	@FXML
	Label balance;
	
	@FXML
	TableView<Security> securityTable;
	
	@FXML
	TableColumn<Security, String> nameColumn;
	
	@FXML
	TableColumn<Security, String> dateColumn;
	
	@FXML
	TableColumn<Security, String> rateColumn;
	
	private Depot depot;

	private List<Security> securities;

	private ObservableList<Security> observableSecurities;
	
	public TradeViewController() {
		setupJMS();
	}
	
	private void setupJMS() {
		try {
			TopicConnection connection = Main.connectionFactory.createTopicConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer messageConsumer = session.createConsumer(Main.securitiesTopic);
			messageConsumer.setMessageListener(this);
			connection.start();
		} catch (JMSException e) {
			System.out.println(e);
		}		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		securities = Main.securityStorage.getAllSecurities();
		observableSecurities = FXCollections.observableList(securities);
		updateSecurities();
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<Security, String>("isin"));
		
		dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Security,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Security, String> s) {
				Date date = s.getValue().getLatestRate().getDate();
				return new ReadOnlyObjectWrapper<String>(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
			}
		});

		rateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Security,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Security, String> s) {
				String initialValue = Float.toString(s.getValue().getLatestRate().getRate());
				return new ReadOnlyObjectWrapper<String>(initialValue);
			}
		});
		
		securityTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Security>() {

			@Override
			public void changed(ObservableValue<? extends Security> observableValue, Security oldSecurity, Security newSecurity) {
				if(newSecurity != null) {
					buy.setDisable(false);
				}
			}
		});
		
		securityTable.setItems(observableSecurities);
		
		buy.setDisable(true);
	}

	private void updateSecurities() {
		observableSecurities.clear();
		observableSecurities.addAll(Main.securityStorage.getAllSecurities());
	}

	public void buy(ActionEvent event) {
		Security selectedSecurity = securityTable.getSelectionModel().getSelectedItem();

		Stage stage = (Stage) securityTable.getScene().getWindow();
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("BuyOrderView.fxml");
		Scene tradeViewScene = sceneLoader.getScene();
		BuyOrderController buyOrderController = sceneLoader.getController();
		buyOrderController.showSecurityToBuy(selectedSecurity);
		buyOrderController.setDepot(depot);
		stage.setScene(tradeViewScene);
	}

	public void quit(ActionEvent event) {
		Platform.exit();
	}
	
	public void updateDepot() {
		balance.setText(depot.getBalance().toString() + "€");
	}
	
	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	@Override
	public void onMessage(Message message) {
		System.out.println("TradeViewController.onMessage()");
		try {
			if(message instanceof TextMessage) {
				String text = ((TextMessage) message).getText();
				if("Securities updated.".equals(text)) {
					updateSecurities();
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
